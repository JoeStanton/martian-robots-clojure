(defproject martian-robots-clojure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"], [expectations "1.4.52"]]
  :main ^:skip-aot martian-robots-clojure.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
