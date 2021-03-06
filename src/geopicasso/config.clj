(ns geopicasso.config
  (:require [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [geopicasso.specs.config :as config-specs]))

;;
;;
;; ns for generating a config that represents all of the input data needed to create a render.

(defrecord Config [id,     ; the name of the file to be saved
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

(defn create [m]
  (map->Config (s/conform config-specs/config-spec m)))

(def default-config
  (create
    {:id "default",
     :cx 0.5,
     :cy 0.5,
     :r 0.4,
     :n 1000,
     :bg "rgb(0, 0, 0)",
     :fills [{:color "black", :opacity 0.0}],
     :strokes [{:color "black", :opacity 1.0, :width 1.0}],
     :shapes [0],
     :x-res 1600,
     :y-res 1200}))

(defn from-resource [path]
  "Given a path to a resources config edn file, return the corresponding config object."
  (let [dataset (-> path io/resource slurp read-string)
        id (first (clojure.string/split path #"\."))
        [rx, ry] [(or
                    (get-in dataset [:res :x])
                    (:x-res default-config))
                  (or
                    (get-in dataset [:res :y])
                    (:y-res default-config))]]
    (create
      {:id id
       :cx (some :cx [dataset default-config])
       :cy (some :cy [dataset default-config])
       :r (some :r [dataset default-config])
       :n (some :n [dataset default-config])
       :bg (some :bg [dataset default-config])
       :fills (some :fills [dataset default-config])
       :strokes (some :strokes [dataset default-config])
       :shapes (some :shapes [dataset default-config])
       :x-res rx,
       :y-res ry})))

(defn from-nothing []
  (create (config-specs/generate-config)))


