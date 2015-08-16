(ns shouter.web
  (:require [compojure.core :refer [defroutes]]
            [ring.adapter.jetty :as ring]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [shouter.controllers.shouts :as shouts]
            [shouter.views.layout :as layout]
            [shouter.models.migration :as schema]
            [hiccup.page :as page])
  (:gen-class))

(defonce server (atom nil))

(defroutes routes
  shouts/routes
  (route/resources "/")
  (route/not-found (layout/four-oh-four)))

(def application (handler/site routes))

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
    (start port)))
