(defproject geopicasso "0.1.0-SNAPSHOT"
  :description "A sort of art project based on a continuous drawing of circles or polygons."
  :url ""
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
                 ;[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojure "1.9.0-RC1"]
                 [org.clojure/test.check "0.9.0"]
                 [com.velisco/strgen "0.1.4"]
                 [org.clojure/data.json "0.2.6"]
                 [hiccup "1.0.5"]
                 ;[crumpets "0.1.4"]
                 [org.apache.xmlgraphics/batik-transcoder "1.7"]
                 [org.apache.xmlgraphics/batik-codec "1.7"]]

  :jvm-opts ["-Xmx4g" "-XX:+UseConcMarkSweepGC"]
  :plugins [[lein-auto "0.1.3"]]
  :main ^:skip-aot geopicasso.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
  ; :auto {:default {:paths ["resources", "src"]}}


