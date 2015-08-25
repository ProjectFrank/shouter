(ns shouter.models.migration
  (:require [clojure.java.jdbc :as sql]
            [shouter.models.shout :as shout]
            [migratus.core :as migratus]
            [clojure.java.classpath :as cp]))

(def config {:store                :database
             :db {:connection-uri (str "jdbc:" shout/spec)}
             ;; :migration-dir "migrations"
             :migration-table-name "foo_bar"})

(defn migrated? []
  (-> (sql/query shout/spec
                 [(str "select count(*) from information_schema.tables "
                       "where table_name='shouts'")])
      (first)
      (:count)
      (pos?)))

(->> (cp/classpath-directories)
     (map #(clojure.java.io/file % "migrations"))
     (filter #(.exists %))
     first)

(defn migrate []
  (migratus/migrate config)  
  #_(when (not (migrated?))
    (print "Creating database structure...")
    (flush)
    #_(shout/create-shouts! shout/spec)
    (migratus/migrate config)
    (println "done")))

