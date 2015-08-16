(ns shouter.models.shout
  (:require [clojure.java.jdbc :as sql]
            [yesql.core :refer [defqueries]]))

(defonce spec (or (System/getenv "DATABASE_URL")
                  "postgresql://localhost:5432/shouter"))

(defqueries "queries.sql")

(defn all []
  (into [] (sql/query spec ["select * from shouts order by id desc"])))

(defn create [shout]
  (sql/insert! spec :shouts [:body] [shout]))
