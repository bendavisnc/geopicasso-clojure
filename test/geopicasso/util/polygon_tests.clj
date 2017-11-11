(ns geopicasso.util.polygon-tests
  (:require 
    [clojure.test :refer :all]
    [geopicasso.util.polygon :as polygon]
    [common.math.helpers :refer [apply-scale, to-fixed]]))


(defn trim-all-x-places [all, x]
  (map
    (fn [c]
      (map 
        (fn [n]
          (to-fixed n x))
        c))
    all))

(deftest polygon-tests

  (testing "unit-polygon-points"
   (is
     (= 
      (trim-all-x-places (polygon/unit-polygon-points 3) 3)
      [[0.933 0.75] [0.067 0.75] [0.5 0.0]])))

  (testing "points-serializer"
   (is
     (= 
      (polygon/points-serialized [[0 1] [2, 3]])
      "0,1 2,3"))))

