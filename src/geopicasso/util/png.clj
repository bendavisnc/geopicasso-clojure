(ns geopicasso.util.png
  (:require
    [common.math.helpers :refer [to-fixed]]
    [hiccup.core :refer [html]]
    [geopicasso.util.polygon :refer [points]]
    [geopicasso.settings :as settings]
    [clojure.java.shell :as shell]))


(defmulti create-png! (fn [_ _] (if settings/use-rsvg :rsvg :batik)))

(defmethod  create-png! :batik [svg-doc, docname]
  (let [png-converter
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
  (let [temp-svg-file (java.io.File/createTempFile docname ".svg")]
    (do
      (spit temp-svg-file svg-doc)
      (shell/sh "rsvg" (.getAbsolutePath temp-svg-file) (str "renders/" docname ".png"))
      (shutdown-agents))))

