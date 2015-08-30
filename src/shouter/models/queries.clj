(ns shouter.models.queries
  (:require [yesql.core :refer [defqueries]]))

(def spec (or (System/getenv "DATABASE_URL")
              "postgresql://localhost/shouter"))

(defqueries "queries.sql")
