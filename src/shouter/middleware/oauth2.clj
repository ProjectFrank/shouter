(ns shouter.middleware.oauth2
  (:require [clj-http.client :as client]
            [ring.util.response :refer [redirect]]
            ring.util.codec
            [ring.middleware.params :refer [wrap-params]]
            [clojure.data.json :as json]
            [clojure.pprint :refer [pprint]]))

(defn- -error-handler-default [error]
  (pprint error)
  (redirect "/404"))

(defn- -oauth-handler-maker [{:keys [client-id
                                     client-secret
                                     grant-type
                                     redirect-uri
                                     token-endpoint
                                     error-handler
                                     success-handler]
                              :or {error-handler -error-handler-default
                                   grant-type "authorization_code"
                                   redirect-uri "http://localhost:8080/oauth2callback"}
                              :as config}]
  (fn [{{:strs [error code] :as query-params} :query-params :as request}]
    (if error
      (error-handler error)
      (if-not code
        (redirect "/404")
        (let [form-params {:code code
                           :client_id client-id
                           :client_secret client-secret
                           :redirect_uri redirect-uri
                           :grant_type grant-type}
              options {:accept :json
                       :form-params form-params
                       :content-type :x-www-form-urlencoded}
              {:strs [access_token refresh_token expires_in token_type]} (json/read-str (:body (client/post token-endpoint options)))
              result {:access-token access_token
                      :refresh-token refresh_token
                      :expires-in expires_in
                      :token-type token_type}
              modified-request (assoc request :oauth2 result)]
          (success-handler modified-request))))))

(defn wrap-oauth2 [handler config]
  "Oauth2 ring middleware"
  (let [oauth-handler (wrap-params (-oauth-handler-maker config))]
    (fn [{uri :uri :as request}]
      (if (re-find #"^/oauth2callback" uri)
        (oauth-handler request)
        ;; else
        (handler request)))))
