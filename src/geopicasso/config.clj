(ns geopicasso.config
  (:require [clojure.data.json :as json])
  (:gen-class)
  )

(defrecord Config [
  id,
  cx,
  cy,
  r,
  n,
  bg,
  fills,
  strokes,
  shapes,
  x-res,
  y-res])

(def default-config
  (map->Config
    {
      :id "default",
      :cx 0.5,
      :cy 0.5,
      :r 0.4,
      :n 1000,
      :bg "rgb(0, 0, 0)",
      :fills [{:color "black", :opacity 0.0}],
      :strokes [{:color "white", :opacity 1.0, :width 1.0}],
      :shapes [0],
      :x-res 1600,
      :y-res 1200
      }))

(defn with-fallback [fallback]
  (fn [firstchoice]
    (cond
      firstchoice
        firstchoice
      :else
        fallback)))


(defn- file-format-keyfn [path]
  (cond
    (not= -1 (.indexOf path "edn"))
      :edn
    (not= -1 (.indexOf path "json"))
      :json
    :else
      (throw (.Exception "Invalid data format."))))

(defmulti get-dataset file-format-keyfn)

(defmethod get-dataset :edn [path]
  (read-string
    (slurp 
      (clojure.java.io/resource path))))

(defmethod get-dataset :json [path]
  (json/read-str 
    (slurp 
      (clojure.java.io/resource path))
    ))


(defn from [path]
  (let [
      dataset (clojure.walk/keywordize-keys (get-dataset path))
      id (first (clojure.string/split path #"\."))
      [rx, ry] [
          (or
            (get-in dataset [:res :x])
            (:x-res default-config))
          (or
            (get-in dataset [:res :y])
            (:y-res default-config))
        ]
    ]
    (map->Config
      {
        :id id
        :cx ((with-fallback (:cx default-config)) (dataset :cx))
        :cy ((with-fallback (:cy default-config)) (dataset :cy))
        :r ((with-fallback (:r default-config)) (dataset :r))
        :n ((with-fallback (:n default-config)) (dataset :n))
        :bg ((with-fallback (:bg default-config)) (dataset :bg))
        :fills ((with-fallback (:fills default-config)) (dataset :fills))
        :strokes ((with-fallback (:strokes default-config)) (dataset :strokes))
        :shapes ((with-fallback (:shapes default-config)) (dataset :shapes))
        :x-res rx,
        :y-res ry
        })))








