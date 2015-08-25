(ns shouter.views.shouts
  (:require [shouter.views.layout :as layout]
            [hiccup.core :refer [h]]
            [hiccup.form :as form]))

(defn shout-form []
  [:div {:id "shout-form" :class "sixteen columns alpha omega"}
   (form/form-to [:post "/"]
                 (form/label "shout" "What do you want to SHOUT?")
                 (form/text-area "shout")
                 (form/submit-button "SHOUT!"))
   (form/form-to [:delete "/"]
                 (form/submit-button "CLEAR!"))])

(defn display-shouts [shouts]
  [:div {:class "shouts sixteen columns alpha omega"}
   (map
    (fn [shout]
      [:h2 {:class "shout"} (h (:body shout))])
    shouts)])

(defn index [shouts]
  (layout/common "SHOUTER"
                 (shout-form)
                 #_[:div {:class "g-signin2" :data-onsuccess "onSignIn"} "SIGN IN!"]
                 [:a {:class "signin-button"
                      :href "/google_login"} "SIGN IN!"]
                 [:div {:class "clear"}]
                 (display-shouts shouts)))
