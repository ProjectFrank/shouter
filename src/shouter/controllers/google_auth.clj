(ns shouter.controllers.google-auth
  (:require [compojure.core :refer [defroutes GET]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :refer [response redirect]]
            [environ.core :refer [env]]
            [clj-http.client :as client]
            [clojure.pprint :refer [pprint]]
            ring.util.codec
            [clojure.data.json :as json]))

(def CLIENT_ID "369140593080-4mklon14gkoec4gn4jrthcuf1ad7vb31.apps.googleusercontent.com")

(def REDIRECT_URI "http://localhost:8080/oauth2callback")

(def login-uri "https://accounts.google.com")

(def CLIENT_SECRET "7OEFgc2QGOFCe2DUhEhw8_SZ")

(def red (str "https://accounts.google.com/o/oauth2/auth?"
              "scope=email%20profile&"
              "redirect_uri=" (ring.util.codec/url-encode REDIRECT_URI) "&"
              "response_type=code&"
              "client_id=" (ring.util.codec/url-encode CLIENT_ID) "&"
              "approval_prompt=force&"
              "access_type=offline"))

;; user clicks sign in button
;; button goes to /google_login
;; /google_login route redirects to https://accounts.google.com/...
;; user enters credentials and clicks "log in"
;; user gets redirected to REDIRECT_URI with auth code inside query params
;; I hold onto auth code and redirect user from REDIRECT_URI to wherever I want
;; I use the given auth code to POST to google as form data
;; google returns a JSON containing the access token
;; use access token to get user data
;; redirect user back to home page with user data

(defn get-access-token [code]
  "returns an access token map given an auth code"
  (let [form-params {:code code
                     :client_id CLIENT_ID
                     :client_secret CLIENT_SECRET
                     :redirect_uri REDIRECT_URI
                     :grant_type "authorization_code"}
        encoded-form-params (into {}
                                  (map (fn [[key val]]
                                            [key (ring.util.codec/url-encode val)]) form-params))
        options {:accept :json
                 :form-params form-params
                 :content-type :x-www-form-urlencoded}
        _ (pprint options)
        json-str (:body (client/post "https://www.googleapis.com/oauth2/v3/token"
                                     options))]
    (let [result (json/read-str json-str)]
      (pprint result)
      result)))

(defn auth-handler [request]
  (let [params (:query-params request)]
    (if-let [error (get params "error")]
      (do (pprint error)
          (redirect "/404"))
      (let [code (get params "code")]
        (if-not code
          (redirect "/404")
          ;; get access token using auth code
          (let [{:strs [access_token
                        refresh_token
                        expires_in
                        token_type]} (get-access-token code)]
            (pprint "something happened")
            (pprint code)
            ;; finally, redirect to home
            (redirect "/")))))))

(def wrapped-auth-handler (-> auth-handler
                              wrap-params))

(defroutes routes
  (GET "/google_login" [] (redirect red))
  (GET "/oauth2callback" request (wrapped-auth-handler request)))

;; (defn handler [request]
;;   (let [token (get (:form-params request) "idtoken")]
;;     (println (env :client-id))
;;     (response token)))



