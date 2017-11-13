(ns geopicasso.specs.config
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest]
            [geopicasso.specs.helpers :refer [color-spec, fill-item-spec, stroke-item-spec, shape-item-spec]]))

;;
;;
;; Defines a clojure spec for a geopicasso config map and provides a generate function to create a new test sample.

; The name of the file to be saved
(s/def ::id
  (s/with-gen
    string?
    #(gen/return (str "spec_generated_" (rand-int 1000)))))

; The center x coordinate of the largest circle.
(s/def ::cx (s/and double? pos? #(< % 1000)))

; The center y coordinate of the largest circle.
(s/def ::cy (s/and double? pos? #(< % 1000)))

; The  radius of the largest circle.
(s/def ::r (s/and double? pos? #(< % 1000)))

; The number of circles drawn along the largest circle's diameter.
(s/def ::n (s/and int? pos? #(< % 10000)))

; The background color of the render.
(s/def ::bg color-spec)

; The fills to alternate with each circle drawn.
(s/def ::fills (s/coll-of fill-item-spec :kind vector? :min-count 0 :max-count 100))

; The strokes to alternate with each circle drawn.
(s/def ::strokes (s/coll-of stroke-item-spec :kind vector? :min-count 0 :max-count 100))

; The shapes to alternate with each circle drawn.
(s/def ::shapes (s/coll-of shape-item-spec :kind vector? :min-count 0 :max-count 100))

; The width in pixels of the render.
(s/def ::x-res
  (s/with-gen
    int?
    #(gen/return 1600)))

; The height in pixels of the render.
(s/def ::y-res
  (s/with-gen
    int?
    #(gen/return 1200)))

(s/def ::config (s/keys :req-un [::id ::cx ::cy ::r ::n ::bg ::fills ::strokes ::shapes ::x-res ::y-res]))

(def config-spec
  (s/get-spec ::config))


(defn generate-config []
  (gen/generate (s/gen ::config)))



