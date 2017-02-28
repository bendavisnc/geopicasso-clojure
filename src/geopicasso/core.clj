(ns geopicasso.core
  (:require 
    [common.math.helpers :refer [to-fixed]]
    [geopicasso.config :as config]
    [geopicasso.util.util :refer [map->ShapeModel, copy, svg, create-png!]]
    [geopicasso.helpers :refer [first-and-last-shapes-fn, get-next-shape-fn, projected-shape-fn]]
    [hiccup.core :refer [html]]
    [clojure.java.io :refer [file]]
    [geopicasso.settings :as settings]
    )
  (:gen-class))


;;
;; 
;; A sort of art project based on a continuous drawing of circles or polygons.
;; Based on: http://www.sievesofchaos.com/

; the contemporary config object that holds all of the data for the render to happen.
(def ^:dynamic session-config nil) 

(defn first-and-last-shapes [] 
  ((first-and-last-shapes-fn session-config)))

(def get-next-shape (get-next-shape-fn first-and-last-shapes))

(defn projected-shape [shape] 
  ((projected-shape-fn session-config) shape))

(defn unit-shapes []
  (let [
      [first-shape, last-shape] (first-and-last-shapes)
    ]
    (loop [
        acc nil
        previous-shape first-shape
      ]
      (let [ 
          next-shape (get-next-shape previous-shape)
        ]
        (do
          (if
            (>
              (to-fixed (:r next-shape) 4)
              (to-fixed (:r last-shape) 4))
            ; (cons previous-shape acc)
            (reverse (cons previous-shape acc))
            ;else
            (recur (cons previous-shape acc) next-shape)))))))

(defn fills []
  (cycle (:fills session-config)))

(defn strokes []
  (cycle (:strokes session-config)))

(defn shapes []
  (cycle (:shapes session-config)))

(defn ready-svg-doc [dynamic-content]
  (html
    [:svg
      {
        :xmlns "http://www.w3.org/2000/svg"
        :width (:x-res session-config)
        :height (:y-res session-config)
      }
      [:rect
        {
          :x 0
          :y 0
          :width (:x-res session-config)
          :height (:y-res session-config)
          :fill (:bg session-config)
        }]
      dynamic-content
      ]))

(defn ready-shapes []
  (let [raw-shapemodels (unit-shapes)]
    (reverse
      (map
        (fn [i, shape, fill-data, stroke-data, shape-ind]
          (svg 
            (str "element" i)
            (projected-shape shape),
            fill-data,
            stroke-data,
            shape-ind))
        (range 0, (count raw-shapemodels)), raw-shapemodels, (fills), (strokes), (shapes)))))


(defn spit-svg [svg-doc]
  (spit
    (str "./renders/" (:id session-config) ".svg")
    svg-doc))

(defn spit-png [svg-doc]
  (create-png! svg-doc (:id session-config)))

(defn create-svg-doc []
  (ready-svg-doc (ready-shapes)))

(defn go []
  (time
    (let [
        svg-doc (create-svg-doc)
      ]
      (do
        (spit-svg svg-doc)
        (spit-png svg-doc)))))

(defn -main
  [& args]
  (binding [session-config (config/from (first args))]
    (go)
    ))
