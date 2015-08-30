(ns shouter.models.shout
  (:require [clojure.java.jdbc :as sql]
            [yesql.core :refer [defqueries]]
            [shouter.models.queries :as queries]))

(defn create [shout]
  (queries/add-shout! queries/spec shout))

(defn get-all-shouts []
  (queries/get-all-shouts queries/spec))

(defn clear-shouts! []
  (queries/clear-shouts! queries/spec))
