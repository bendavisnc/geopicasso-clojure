(ns geopicasso.core-tests
  (:require
    [clojure.test :refer :all]
    [geopicasso.core :as gp]
    [geopicasso.config :as config]))


(deftest core-tests

  (testing "basic doc generation"
    (is
      (=
        (gp/with-session-config (config/from-resource "minimalist.edn")
          (gp/create-svg-doc))
        "<svg height=\"600\" width=\"800\" xmlns=\"http://www.w3.org/2000/svg\"><rect fill=\"rgb(0, 0, 0)\" height=\"600\" width=\"800\" x=\"0\" y=\"0\"></rect><circle cx=\"400.0\" cy=\"300.0\" fill-opacity=\"0.5\" fill=\"red\" id=\"element7\" r=\"640.0\" stroke-opacity=\"1.0\" stroke-width=\"2.0\" stroke=\"green\"></circle><circle cx=\"240.0\" cy=\"300.0\" fill-opacity=\"0.5\" fill=\"blue\" id=\"element6\" r=\"480.0\" stroke-opacity=\"1.0\" stroke-width=\"2.0\" stroke=\"green\"></circle><circle cx=\"720.0\" cy=\"300.0\" fill-opacity=\"0.5\" fill=\"red\" id=\"element5\" r=\"320.0\" stroke-opacity=\"1.0\" stroke-width=\"2.0\" stroke=\"green\"></circle><circle cx=\"80.0\" cy=\"300.0\" fill-opacity=\"0.5\" fill=\"blue\" id=\"element4\" r=\"320.0\" stroke-opacity=\"1.0\" stroke-width=\"2.0\" stroke=\"green\"></circle><circle cx=\"880.0\" cy=\"300.0\" fill-opacity=\"0.5\" fill=\"red\" id=\"element3\" r=\"160.0\" stroke-opacity=\"1.0\" stroke-width=\"2.0\" stroke=\"green\"></circle><circle cx=\"560.0\" cy=\"300.0\" fill-opacity=\"0.5\" fill=\"blue\" id=\"element2\" r=\"160.0\" stroke-opacity=\"1.0\" stroke-width=\"2.0\" stroke=\"green\"></circle><circle cx=\"240.0\" cy=\"300.0\" fill-opacity=\"0.5\" fill=\"red\" id=\"element1\" r=\"160.0\" stroke-opacity=\"1.0\" stroke-width=\"2.0\" stroke=\"green\"></circle><circle cx=\"-80.0\" cy=\"300.0\" fill-opacity=\"0.5\" fill=\"blue\" id=\"element0\" r=\"160.0\" stroke-opacity=\"1.0\" stroke-width=\"2.0\" stroke=\"green\"></circle></svg>"))))

