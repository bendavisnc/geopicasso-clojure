(ns geopicasso.core
  (:require 
    [common.math.helpers :refer [to-fixed]]
    [geopicasso.config :as config]
    [geopicasso.util :refer [map->ShapeModel, copy, svg]]
    [geopicasso.helpers :refer [first-and-last-shapes-fn, get-next-shape-fn, projected-shape-fn, create-png!]]
    [hiccup.core :refer [html]]
    [clojure.java.io :refer [file]]
    )
  (:gen-class))

(def session-config (atom nil))

(def first-and-last-shapes (first-and-last-shapes-fn session-config))

(def get-next-shape (get-next-shape-fn first-and-last-shapes))

(def projected-shape (projected-shape-fn session-config))

(defn unit-shapes []
  (do
    ; (println "@ unit-shapes")
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
              (recur (cons previous-shape acc) next-shape))))))))

(defn fills []
  (cycle (:fills @session-config)))

(defn strokes []
  (cycle (:strokes @session-config)))

(defn shapes []
  (cycle (:shapes @session-config)))

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
          :fill (:bg @session-config)
        }]
      dynamic-content
      ]))

(defn ready-shapes []
  (map
    (fn [shape, fill-data, stroke-data]
      (svg 
        (projected-shape shape)
        fill-data
        stroke-data
        @session-config))
    (unit-shapes), (fills), (strokes)))


(defn spit-svg [svg-doc]
  (do
    ; (println "@ spit-svg")
    (spit
      (str "./renders/" (:id @session-config) ".svg")
      ; (file (str "./rendered/" (:id @session-config) ".svg"))
      svg-doc)))
      ; (ready-svg-doc (ready-shapes)))))

(defn spit-png [svg-doc]
  (create-png! svg-doc (:id @session-config)))

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

(defn set-session [path]
  (reset! 
    session-config 
    (config/from path)))


(defn -main
  [& args]
  (do
    ; (println args)
    (set-session (first args))
      ; (config/from "deathstartest.edn"))
    (go)
    ))
