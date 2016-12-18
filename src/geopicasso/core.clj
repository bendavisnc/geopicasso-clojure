(ns geopicasso.core
  (:require 
    [common.math.helpers :refer [to-fixed]]
    [geopicasso.config :as config]
    [geopicasso.util :refer [map->ShapeModel, copy, svg]]
    [hiccup.core :refer [html]]
    [clojure.java.io :refer [file]]
    )
  (:gen-class))

(def session-config (atom nil))

(defn first-and-last-shapes []
  (let [
      little-r (/ 0.5 (:n @session-config))
    ]
    [
     (map->ShapeModel {:cx little-r, :cy 0.5, :r little-r}),
     (map->ShapeModel {:cx 0.5, :cy 0.5, :r 0.5})
    ]))

(defn get-next-shape [previous-shape]
  (let [
      [first-shape, last-shape] (first-and-last-shapes)
      make-bigger
        (>
          (to-fixed
            (+ 
              (:cx previous-shape)
              (*
                3
                (:r previous-shape)))
            4)
          (to-fixed
            (+ 
              (:cx last-shape)
              (:r last-shape))
            4))
      bigger-r ; val biggerR = (previousShape.r / firstShape.r + 1) * firstShape.r
        (*
          (+
            (/
              (:r previous-shape)
              (:r first-shape))
            1)
          (:r first-shape))
    ]
    (if 
      make-bigger
        (copy previous-shape {:cx bigger-r, :r bigger-r})
      ;else
        (copy 
          previous-shape 
          {
           :cx 
             (+
               (:cx previous-shape)
               (* 2 (:r previous-shape)))
          }))))

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
            (cons previous-shape acc)
            ;else
            (recur (cons previous-shape acc) next-shape)))))))

(defn projected-shape [shape]
  (let [
      config-unit-scale (fn [d] (* d (/ (:r @session-config) 0.5)))
      config-unit-xmove (fn [d] (+ d (- (:cx @session-config) (config-unit-scale 0.5))))
      config-unit-ymove (fn [d] (+ d (- (:cy @session-config) (config-unit-scale 0.5))))
      unit-to-projected-xscale (fn [d] (* d (:x-res @session-config)))
      unit-to-projected-yscale (fn [d] (* d (:y-res @session-config)))
      x-transform (comp unit-to-projected-xscale config-unit-xmove config-unit-scale)
      y-transform (comp unit-to-projected-yscale config-unit-ymove config-unit-scale)
      r-transform (comp unit-to-projected-xscale config-unit-scale)
    ]
    (copy shape 
      {
       :cx (x-transform (:cx shape))
       :cy (y-transform (:cy shape))
       :r (r-transform (:r shape))
      })))

(defn ready-svg-doc [dynamic-content]
  (html
    [:svg
      {
        :xmlns "http://www.w3.org/2000/svg"
        :width (:x-res @session-config)
        :height (:y-res @session-config)
      }
      [:rect
        {
          :x 0
          :y 0
          :width (:x-res @session-config)
          :height (:y-res @session-config)
          :fill "black"
        }]
      dynamic-content
      ]))

(defn ready-shapes []
  (map
    (fn [shape]
      (->
        ; shape
        (projected-shape shape)
        (svg @session-config)
        ; 7
      ))
    (unit-shapes)))

(defn spit-svg []
  (do
    (println "@ spit-svg")
    (spit
      (str "./renders/" (:id @session-config) ".svg")
      ; (file (str "./rendered/" (:id @session-config) ".svg"))
      (ready-svg-doc (ready-shapes)))))


(defn go []
  (spit-svg))

(defn -main
  [& args]
  (do
    ; (println args)
    (reset! 
      session-config 
      (config/from (first args)))
      ; (config/from "deathstartest.edn"))
    (go)))
