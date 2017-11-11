(ns geopicasso.util.png
  (:require
    [geopicasso.settings :as settings]
    [clojure.java.shell :as shell])
  (:import (org.apache.batik.transcoder.image PNGTranscoder)
           (org.apache.batik.transcoder TranscoderInput TranscoderOutput)
           (java.io ByteArrayInputStream FileOutputStream)))


(defmulti create-png! (fn [_ _] (if settings/use-rsvg :rsvg :batik)))

(defmethod  create-png! :batik [svg-doc, docname]
  (let [png-converter
        (new PNGTranscoder)
        svg-input
        (new TranscoderInput
          (new ByteArrayInputStream (.getBytes (str svg-doc))))
        svg-out-file
        (new FileOutputStream (str "renders/" docname ".png"))
        svg-out (new TranscoderOutput svg-out-file)]
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

