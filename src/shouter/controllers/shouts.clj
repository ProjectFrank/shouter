(ns shouter.controllers.shouts
  (:require [compojure.core :refer [defroutes GET POST DELETE]]
            [clojure.string :as str]
            [ring.util.response :as ring]
            [shouter.views.shouts :as view]
            [shouter.models.shout :as model]
            [shouter.models.migration :as migration]))

(defn index []
  (view/index (model/get-all-shouts model/spec)))

(defn create
  [shout]
  (when-not (str/blank? shout)
    (model/create shout)
    (ring/redirect "/")))

(defn clear! []
  (model/clear-shouts! model/spec)
  (ring/redirect "/"))

(defroutes routes
  (GET "/" [] (index))
  (POST "/" [shout] (create shout))
  (DELETE "/" [] (clear!)))
