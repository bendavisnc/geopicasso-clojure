(ns geopicasso.config
  (:require [clojure.data.json :as json])
  )
;;
;;
;; ns for generating a config that represents all of the input data needed to create a render.

(defrecord Config [
  id,     ; the name of the file to be saved
  cx,     ; the center x coordinate of the largest circle. 
  cy,     ; the center y coordinate of the largest circle. 
  r,      ; the radius of the largest circle.
  n,      ; the number of circles drawn along the largest circle's diameter.
  bg,     ; the background color of the render.
  fills,  ; the fills to alternate with each circle drawn.
  strokes,; the strokes to alternate with each circle drawn.
  shapes, ; the shapes to alternate with each circle drawn.
  x-res,  ; the width in pixels of the render.
  y-res]) ; the height in pixels of the render.

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
      :strokes [{:color "black", :opacity 1.0, :width 1.0}],
      :shapes [0],
      :x-res 1600,
      :y-res 1200
      }))


(defn- *-file? [format, path]
  (not= -1 (.indexOf path format)))
  
(def edn-file? (partial *-file? "edn"))

(def json-file? (partial *-file? "json"))

(defn- file-format-keyfn [path]
  (cond
    (edn-file? path)
      :edn
    (json-file? path)
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

(defn with-fallback [fallback]
  (fn [firstchoice]
    (cond
      firstchoice
        firstchoice
      :else
        fallback)))

(defn from [path]
  "Given a path to a resources config file (edn or json), return the corresponding config object."
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
