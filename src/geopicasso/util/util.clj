(ns geopicasso.util.util
  (:require
    [common.math.helpers :refer [to-fixed]]
    [hiccup.core :refer [html]]
    [geopicasso.util.polygon :refer [points]]
    [geopicasso.settings :as settings]
    [clojure.java.shell :as shell]))



(defrecord ShapeModel [cx, cy, r])

(defn create-shapemodel [cx, cy, r]
  (ShapeModel. cx, cy, r))

(defn copy [shapemodel, argm]
  (map->ShapeModel
    (merge shapemodel argm)))

(defmulti svg
  (fn [id, shapemodel, fill-data, stroke-data, sides-count]
    (cond
      ; true
        ; :debug
      (and
        (instance? geopicasso.util.util.ShapeModel shapemodel)
        (= sides-count 0))
      :circle
      (and
        (instance? geopicasso.util.util.ShapeModel shapemodel)
        (> sides-count 0))
      :polygon
      :else
        (throw (Exception. (str "Unsupported svg conversion operation."))))))


(defmethod svg :circle [id, shapemodel, fill-data, stroke-data, _]
  (let [
        precision-amt 4]

    [:circle
      {
       :id id      
       :cx (to-fixed (:cx shapemodel) precision-amt)
       :cy (to-fixed (:cy shapemodel) precision-amt)
       :r (to-fixed (:r shapemodel) precision-amt)
       :fill (:color fill-data)
       :fill-opacity (:opacity fill-data)
       :stroke (:color stroke-data)
       :stroke-opacity (:opacity stroke-data)
       :stroke-width (:width stroke-data)}]))



(defmethod svg :polygon [id, shapemodel, fill-data, stroke-data, sides-count]
  (let [
        precision-amt 4]

    [:polygon
      {
       :id id      
       :points (points shapemodel, sides-count)
       :fill (:color fill-data)
       :fill-opacity (:opacity fill-data)
       :stroke (:color stroke-data)
       :stroke-opacity (:opacity stroke-data)
       :stroke-width (:width stroke-data)}]))


(defmulti create-png! 
  (fn [svg-doc, docname]
    (if settings/use-rsvg
      :rsvg
      ;else
      :batik)))

(defmethod  create-png! :batik [svg-doc, docname]
  (let [
        png-converter
        (org.apache.batik.transcoder.image.PNGTranscoder.)
        svg-input
        (org.apache.batik.transcoder.TranscoderInput. 
          (java.io.ByteArrayInputStream. (.getBytes (str svg-doc))))
        svg-out-file
        (java.io.FileOutputStream. (str "renders/" docname ".png"))
        svg-out (org.apache.batik.transcoder.TranscoderOutput. svg-out-file)]

    (do
      (.transcode png-converter svg-input svg-out)
      (.flush svg-out-file)
      (.close svg-out-file))))

(defmethod  create-png! :rsvg [svg-doc, docname]
  ; (with-open [temp-svg-file (java.io.File/createTempFile docname ".svg")]
    ; (do
      ; (spit temp-svg-file svg-doc)
      ; (shell/sh "rsvg" (.getAbsolutePath temp-svg-file) (str "renders/" docname ".png")))))
  (let [
        temp-svg-file (java.io.File/createTempFile docname ".svg")]

    (do
      (spit temp-svg-file svg-doc)
      (shell/sh "rsvg" (.getAbsolutePath temp-svg-file) (str "renders/" docname ".png"))
      (shutdown-agents))))



