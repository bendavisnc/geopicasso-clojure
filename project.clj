(defproject geopicasso "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [hiccup "1.0.5"]
                 [org.apache.xmlgraphics/batik-transcoder "1.7"]
                 [org.apache.xmlgraphics/batik-codec "1.7"]
                 ]
  :plugins [[lein-auto "0.1.3"]]
  :main ^:skip-aot geopicasso.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :auto {:default {:paths ["resources", "src"]}}
  )

