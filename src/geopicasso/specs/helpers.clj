(ns geopicasso.specs.helpers
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]))

(s/def ::color-component (s/and nat-int? #(<= % 255)))


(s/def ::opacity (s/and double? pos? #(<= % 1)))

(defn random-color [num_of_comps]
  (mapv
    #(%)
    ;(repeat (rand-nth [3 4]) #(rand-int 256))
    (repeat num_of_comps #(rand-int 256))))

(s/def ::color
  (s/with-gen
    (s/and vector? #(#{3, 4} (count %)) (s/every ::color-component))
    #(gen/fmap random-color (gen/elements [3 4]))))

(def color-spec
  (s/get-spec ::color))

(s/def ::fill-item (s/keys :req-un [::color ::opacity]))

(def fill-item-spec
  (s/get-spec ::fill-item))

;(defn create-sample []
;  (gen/generate (s/gen color-spec)))
