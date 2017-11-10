(ns geopicasso.util.fixednfiles
  (:require [clojure.java.io :as io])
  (:import (java.io File FileWriter)))

(def files-to-fix
  (->>
    (file-seq (io/file "./resources"))
    (filter (fn [^File f] (.isFile f)))))

(def test-data
  (map
    #(.getName %)
    files-to-fix))

(defn perform-fix! []
  (doseq [^File f files-to-fix]
    (let [data (read-string (slurp f))
          new-data (clojure.walk/keywordize-keys data)
          out (new FileWriter f)]
      (clojure.pprint/pprint new-data out))))
