(ns shouter.models.queries
  (:require [yesql.core :refer [defqueries]]
            [environ.core :refer [env]]))

(def spec (:database-url env))

(defqueries "queries.sql")
