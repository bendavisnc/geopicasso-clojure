(ns geopicasso.core
  (:require 
    [common.math.helpers :refer [to-fixed]]
    [geopicasso.config :as config]
    [geopicasso.util.util :refer [map->ShapeModel, copy, svg, create-png!]]
    [hiccup.core :as hiccup]
    [clojure.java.io :refer [file]]
    [geopicasso.settings :as settings])

  (:gen-class))


;;
;; 
;; A sort of art project based on a continuous drawing of circles or polygons.
;; Based on: http://www.sievesofchaos.com/

; the contemporary config object that holds all of the data for the render to happen.
(def ^:dynamic session-config nil) 

(def first-and-last-shapes
  (memoize
    #(let [little-r (/ 0.5 (:n session-config))]
      [(map->ShapeModel {:cx little-r, :cy 0.5, :r little-r}),
       (map->ShapeModel {:cx 0.5, :cy 0.5, :r 0.5})])))

(defn bigger-shape [first-shape, previous-shape]
  (let [bigger-r (* (+ (/ (:r previous-shape) (:r first-shape)) 1) (:r first-shape))]
    (copy previous-shape {:cx bigger-r, :r bigger-r})))

(defn right-of-shape [previous-shape]
  (copy previous-shape {:cx (+ (:cx previous-shape) (* 2 (:r previous-shape)))}))

(defn right-of-shape?
  "Is shape sa to the right of shape sb?"
  [sa, sb]
  (>
    (to-fixed (+ (:cx sa) (:r sa)), 4)
    (to-fixed (+ (:cx sb) (:r sb)), 4)))

(defn bigger-shape?
  "Is shape sa bigger than shape sb?"
  [sa, sb]
  (>
    (to-fixed (:r sa), 4)
    (to-fixed (:r sb), 4)))

(defn next-shape [previous-shape]
  (let [[first-shape, last-shape] (first-and-last-shapes)
        [r-shape, b-shape] ((juxt right-of-shape (partial bigger-shape first-shape)) previous-shape)]
    (if (right-of-shape? r-shape last-shape)
      b-shape
      ;else
      r-shape)))

(defn projected-shape [shape]
  (let [config-unit-scale (fn [d] (* d (/ (:r session-config) 0.5)))
        config-unit-xmove (fn [d] (+ d (- (:cx session-config) (config-unit-scale 0.5))))
        config-unit-ymove (fn [d] (+ d (- (:cy session-config) (config-unit-scale 0.5))))
        unit-to-projected-xscale (fn [d] (* d (:x-res session-config)))
        unit-to-projected-yscale (fn [d] (* d (:y-res session-config)))
        x-transform (comp unit-to-projected-xscale config-unit-xmove config-unit-scale)
        y-transform (comp unit-to-projected-yscale config-unit-ymove config-unit-scale)
        r-transform (comp unit-to-projected-xscale config-unit-scale)]
    (map->ShapeModel
      (-> shape
        (update :cx x-transform)
        (update :cy y-transform)
        (update :r r-transform)))))

(defn unit-shapes []
  (let [[first-shape, last-shape] (first-and-last-shapes)]
    (loop [acc [], previous-shape first-shape]
      (let [next-shape (next-shape previous-shape)
            next-acc (conj acc previous-shape)]
        (if (bigger-shape? next-shape last-shape)
          next-acc
          ;else
          (recur next-acc next-shape))))))

(defn fills []
  (cycle (:fills session-config)))

(defn strokes []
  (cycle (:strokes session-config)))

(defn shapes []
  (cycle (:shapes session-config)))

(defn ready-svg-doc [dynamic-content]
  (hiccup/html
    [:svg
      {:xmlns "http://www.w3.org/2000/svg"
       :width (:x-res session-config)
       :height (:y-res session-config)}
      [:rect
        {:x 0
         :y 0
         :width (:x-res session-config)
         :height (:y-res session-config)
         :fill (:bg session-config)}]
      dynamic-content]))

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

(defn spit-svg! [svg-doc]
  (spit
    (str "./renders/" (:id session-config) ".svg")
    svg-doc))

(defn spit-png! [svg-doc]
  (create-png! svg-doc (:id session-config)))

(defn create-svg-doc []
  (ready-svg-doc (ready-shapes)))

(defmacro with-session-config [config & form]
  `(binding [session-config ~config]
     ~@form))


(defn -main
  [& args]
  (time
    (with-session-config (config/from (first args))
      (doto
        (create-svg-doc)
        (spit-png!)
        (spit-svg!)))))





