(ns shouter.web
  (:require [compojure.core :refer [defroutes]]
            [ring.adapter.jetty :as ring]
            [ring.middleware.session :as session]
            [ring.middleware.resource :as resource]
            [ring.middleware.content-type :as content-type]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [shouter.controllers.shouts :as shouts]
            [shouter.controllers.google-auth :as google-auth]
            [shouter.views.layout :as layout]
            [shouter.models.migration :as schema]
            [clojure.pprint :refer [pprint]]
            [shouter.middleware.oauth2 :refer [wrap-oauth2]]
            [environ.core :refer [env]])
  (:gen-class))

(defonce server (atom nil))

(defroutes routes
  google-auth/routes
  shouts/routes
  (route/not-found (layout/four-oh-four)))

(defn wrap-count [handler]
  (fn [{incoming-session :session :as request}]
    (let [response (handler request)
          outgoing-session (:session response)
          count (:count incoming-session 0)
          session (merge incoming-session (assoc outgoing-session :count (inc count)))]
      (assoc response :session session))))

(defn wrap-logging [handler]
  (fn [request]
    (let [response (handler request)]
      (println "request session" (:session request))
      (println "response session" (:session response))
      response)))

(def application (-> routes
                     (wrap-count)
                     (wrap-oauth2 google-auth/oauth-config)
                     (wrap-logging)
                     (handler/site)
                     (resource/wrap-resource "/public")
                     (content-type/wrap-content-type)))

(defn start [port]
  (when-not @server
    (reset!
     server
     (ring/run-jetty application {:port port
                                  :join? false}))))

(defn stop []
  (when-let [instance @server]
    (.stop instance)
    (reset! server nil)))

(defn -main [& args]
  (schema/migrate)
  (let [port (Integer. (or (System/getenv "PORT") "8080"))]
    (start port))
  (and (= (System/getenv "ci") "true") (System/exit 0)))
