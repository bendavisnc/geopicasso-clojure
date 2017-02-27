(ns geopicasso.util.polygon-tests
  (:require 
    [clojure.test :refer :all]
    [geopicasso.util.polygon :refer [unit-polygon-points, points-serialized, points]]
    [common.math.helpers :refer [apply-scale]]
    [geopicasso.util.util :refer [create-shapemodel]]
    )
  )

(deftest all-tests

  (testing "unit-polygon-points"
   (is
     (= 
      (unit-polygon-points 3)
      [[0.933 0.75] [0.067 0.75] [0.5 0]]
      )))

  (testing "points-serializer"
   (is
     (= 
      (points-serialized [[0 1] [2, 3]])
      "0,1 2,3")))
  
  (testing "points"
   (is
     (= 
      (points (create-shapemodel 0.5, 0.5, 0.5) 3)
      "0.933,0.75 0.067,0.75 0.5,0.0")))
  )
