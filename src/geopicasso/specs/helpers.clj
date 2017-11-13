(ns geopicasso.specs.helpers
  (:require [clojure.spec.alpha :as s]
            [miner.strgen :as sg]
            [clojure.spec.gen.alpha :as gen]))

; http://www.mkyong.com/regular-expressions/how-to-validate-hex-color-code-with-regular-expression/
(def color-regex #"^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")

(def color? #(re-matches color-regex %))

(s/def ::color (s/and string? color?))

(def color-spec (s/get-spec ::color))

(defn create-sample []
  (gen/generate (s/gen ::color)))
