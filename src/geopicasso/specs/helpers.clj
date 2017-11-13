(ns geopicasso.specs.helpers
  (:require [clojure.spec.alpha :as s]
            [miner.strgen :as sg]
            [clojure.spec.gen.alpha :as gen]))

(s/def ::opacity (s/and double? #(and (>= % 0) (<= % 1))))

; http://www.mkyong.com/regular-expressions/how-to-validate-hex-color-code-with-regular-expression/
(def color-regex #"^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")

(s/def ::color
  (s/with-gen
    string?
    #(sg/string-generator color-regex)))

(def color-spec
  (s/get-spec ::color))

(s/def ::fill-item (s/keys :req-un [::color ::opacity]))

(s/def ::width (s/and double? pos? #(<= % 4)))
(s/def ::stroke-item (s/keys :req-un [::color ::opacity] :opt-un [::width]))

(s/def ::shape-item (s/and int? #(not (= 1 %)) #(and (>= % 0) (<= % 100))))

(def fill-item-spec
  (s/get-spec ::fill-item))

(def stroke-item-spec
  (s/get-spec ::stroke-item))

(def shape-item-spec
  (s/get-spec ::shape-item))


