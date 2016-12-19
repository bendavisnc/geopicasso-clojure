(ns geopicasso.playground
  (:require [clojure.test :refer :all]
            [geopicasso.config]
            [geopicasso.config :refer [map->Config, default-config, from, with-fallback]]
            )
  (:import [geopicasso.config Config])
  )

(defn outer-fn [a]
  (fn []
    (str "good" @a)))

(def outer-a (atom nil))
(def inner-fn (outer-fn outer-a))

(reset! outer-a "bats")

(deftest all-tests
  (testing "playground"
   (is
     (= 
      "goodbats"
      (inner-fn)))))
