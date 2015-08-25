(defproject shouter "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/java.jdbc "0.4.1"]
                 [postgresql "9.1-901.jdbc4"]
                 [ring/ring-jetty-adapter "1.2.1"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.4"]
                 [yesql "0.4.2"]
                 [environ "1.0.0"]
                 [migratus "0.8.3"]
                 [clj-http "1.1.2"]
                 [org.clojure/data.json "0.2.6"]]
  :main shouter.web
  :migratus {:store :database}
  :uberjar-name "shouter-standalone.jar"
  :plugins [[lein-environ "1.0.0"]
            [migratus-lein "0.1.1"]
            [lein-ring "0.9.6"]]
  :ring {:handler shouter.web/application}
  :profiles {:uberjar {:aot :all}}
  :min-lein-version "2.0.0"
  :aot [shouter.web])
