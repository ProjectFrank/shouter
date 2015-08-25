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
            [hiccup.page :as page]
            [clojure.pprint :refer [pprint]])
  (:gen-class))

(defonce server (atom nil))

(defroutes routes
  google-auth/routes
  shouts/routes
  (route/not-found (layout/four-oh-four)))

(defn wrap-logging [handler]
  (fn [{session :session :as request}]
    (let [response (handler request)
          count (:count session 0)
          session (assoc session :count (inc count))]
      ;; (pprint session)
      ;; (pprint request)
      (assoc response :session session))))

(def application (-> (handler/site routes)
                     (wrap-logging)
                     (session/wrap-session)
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

(defn -main []
  (schema/migrate)
  (let [port (Integer. (or (System/getenv "PORT") "8080"))]
    (start port))
  (and (= (System/getenv "ci") "true") (System/exit 0)))
