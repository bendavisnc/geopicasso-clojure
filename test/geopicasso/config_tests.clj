(ns geopicasso.config-tests
  (:require [clojure.test :refer :all]
            [geopicasso.config :as config]))

(deftest config-tests
  (testing "minimalist"
    (is
      (=
        (config/from "minimalist.edn")
        (config/->Config
          "minimalist",
          0.5,
          0.5,
          0.8,
          4,
          "rgb(0, 0, 0)",
          [{:color "blue", :opacity 0.5},
           {:color "red", :opacity 0.5}],
          [{:color "green", :opacity 1.0, :width 2.0}],
          [0],
          800,
          600))))

  (testing "superminimalist"
    (is
      (=
        (config/from "superminimalist.edn")
        (config/create
          (assoc
            config/default-config
            :id "superminimalist"))))))

