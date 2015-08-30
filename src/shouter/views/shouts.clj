(ns shouter.views.shouts
  (:require [shouter.views.layout :as layout]
            [hiccup.core :refer [h]]
            [hiccup.form :as form]
            ring.util.codec
            [shouter.controllers.google-auth :as google-auth]))

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
                      :href (str "https://accounts.google.com/o/oauth2/auth?"
                                 "scope=email%20profile&"
                                 "redirect_uri=" (ring.util.codec/url-encode google-auth/REDIRECT_URI) "&"
                                 "response_type=code&"
                                 "client_id=" (ring.util.codec/url-encode google-auth/CLIENT_ID) "&"
                                 "approval_prompt=force&"
                                 "access_type=offline")} "SIGN IN!"]
                 [:div {:class "clear"}]
                 (display-shouts shouts)))
