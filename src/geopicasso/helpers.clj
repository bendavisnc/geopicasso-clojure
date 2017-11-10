(ns geopicasso.helpers
  (:require
    [common.math.helpers :refer [to-fixed]]
    [geopicasso.util.util :refer [map->ShapeModel, copy, svg]]
    [hiccup.core :refer [html]])

  (:import
    (org.apache.batik.transcoder.image PNGTranscoder)
    (org.apache.batik.transcoder TranscoderInput)
    (org.apache.batik.transcoder TranscoderOutput)))


(defmacro projected-shape-fn [config]
  "Creates a function that transforms a shape model from a unit space to a space defined by config parameters."
  `(fn [shape#]
    (let [
          config-unit-scale# (fn [d#] (* d# (/ (:r ~config) 0.5)))
          config-unit-xmove# (fn [d#] (+ d# (- (:cx ~config) (config-unit-scale# 0.5))))
          config-unit-ymove# (fn [d#] (+ d# (- (:cy ~config) (config-unit-scale# 0.5))))
          unit-to-projected-xscale# (fn [d#] (* d# (:x-res ~config)))
          unit-to-projected-yscale# (fn [d#] (* d# (:y-res ~config)))
          x-transform# (comp unit-to-projected-xscale# config-unit-xmove# config-unit-scale#)
          y-transform# (comp unit-to-projected-yscale# config-unit-ymove# config-unit-scale#)
          r-transform# (comp unit-to-projected-xscale# config-unit-scale#)]

      (copy shape# 
        {
         :cx (x-transform# (:cx shape#))
         :cy (y-transform# (:cy shape#))
         :r (r-transform# (:r shape#))}))))



;(defmacro first-and-last-shapes-fn [config]
;  `(fn []
;    (let [
;          little-r# (/ 0.5 (:n ~config))]
;
;      [
;       (map->ShapeModel {:cx little-r#, :cy 0.5, :r little-r#}),
;       (map->ShapeModel {:cx 0.5, :cy 0.5, :r 0.5})])))


;(defn get-next-shape-fn [get-first-and-last-shapes]
;  (fn [previous-shape]
;    (let [
;          [first-shape, last-shape] (get-first-and-last-shapes)
;          make-bigger
;          (>
;            (to-fixed
;              (+
;                (:cx previous-shape)
;                (*
;                  3
;                  (:r previous-shape)))
;              4)
;            (to-fixed
;              (+
;                (:cx last-shape)
;                (:r last-shape))
;              4))
;          bigger-r ; val biggerR = (previousShape.r / firstShape.r + 1) * firstShape.r
;          (*
;            (+
;              (/
;                (:r previous-shape)
;                (:r first-shape))
;              1)
;            (:r first-shape))]
;
;      (if
;        make-bigger
;          (copy previous-shape {:cx bigger-r, :r bigger-r})
;        ;else
;          (copy
;            previous-shape
;            {
;             :cx
;               (+
;                 (:cx previous-shape)
;                 (* 2 (:r previous-shape)))})))))



