(ns geopicasso.config-test
  (:require [clojure.test :refer :all]
            [geopicasso.config]
            [geopicasso.config :refer [map->Config, default-config, from]])
  (:import [geopicasso.config Config]))



(deftest all-tests
   (testing "minimalist"
     (is
       (= 
         (from "minimalist.edn")
         (Config.
           "minimalist",
           0.5,
           0.5,
           0.8,
           4,
           "rgb(0, 0, 0)",
           [
             {:color "blue", :opacity 0.5},
             {:color "red", :opacity 0.5}]
           ,
           [{:color "green", :opacity 1.0, :width 2.0}],
           [0],
           800,
           600))))
   
   (testing "superminimalist"
     (is
       (= 
         (from "superminimalist.edn")
         (map->Config
           (assoc
             default-config
             :id "superminimalist"))))))

