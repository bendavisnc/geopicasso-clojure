(ns geopicasso.specs.config
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest]
            [geopicasso.specs.helpers :refer [color-spec, fill-item-spec]]))

;(defrecord Config [id,     ; the name of the file to be saved
;                   cx,     ; the center x coordinate of the largest circle.
;                   cy,     ; the center y coordinate of the largest circle.
;                   r,      ; the radius of the largest circle.
;                   n,      ; the number of circles drawn along the largest circle's diameter.
;                   bg,     ; the background color of the render.
;                   fills,  ; the fills to alternate with each circle drawn.
;                   strokes,; the strokes to alternate with each circle drawn.
;                   shapes, ; the shapes to alternate with each circle drawn.
;                   x-res,  ; the width in pixels of the render.
;                   y-res])) ; the height in pixels of the render.

;
; The name of the file to be saved
(s/def ::id string?)

; The center x coordinate of the largest circle.
(s/def ::cx double?)

; The center y coordinate of the largest circle.
(s/def ::cy double?)

; The  radius of the largest circle.
(s/def ::r double?)

; The number of circles drawn along the largest circle's diameter.
(s/def ::n int?)

; The background color of the render.
(s/def ::bg color-spec)

; The fills to alternate with each circle drawn.
(s/def ::fills (s/coll-of fill-item-spec :kind vector? :min-count 0 :max-count 100))

(s/def ::config (s/keys :req-un [::id ::cx ::cy ::r ::n ::bg ::fills]))
;(s/def ::config (s/keys :req-un [::id ::cx ::cy ::r ::n]))

(defn create-sample []
  (gen/generate (s/gen ::config)))
