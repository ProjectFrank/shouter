(ns shouter.models.user
  (:import java.util.UUID)
  (:require [shouter.models.queries :as queries]))

(defn get-user-by-email [email]
  (first (queries/get-user queries/spec email)))

(defn create-user! [{:keys [first-name last-name email]}]
  "creates new user and returns the created user"
  (let [uuid (java.util.UUID/randomUUID)]
    (queries/create-user! queries/spec uuid first-name last-name email)
    {:uuid (str uuid)
     :first-name first-name
     :last-name last-name
     :email email}))
