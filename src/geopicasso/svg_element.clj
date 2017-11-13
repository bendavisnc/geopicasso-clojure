(ns geopicasso.svg-element
  (:require
    [common.math.helpers :as m]
    [crumpets.core :as color-helper]
    [geopicasso.util.polygon :as polygon]
    [geopicasso.settings :refer [precision-amount]]))

(defn color [v]
  (cond
    (string? v)
    v
    (= 4 (count v))
    (color-helper/hex (color-helper/->rgba v))
    (= 3 (count v))
    (color-helper/hex (color-helper/->rgb v))))

(defmulti svg
          (fn [_, _, _, _, sides-count]
            (if (zero? sides-count)
              :circle
              ;else
              :polygon)))


(defmethod svg :circle [id, shapemodel, fill-data, stroke-data, _]
  [:circle
   {:id id
    :cx (m/to-fixed (:cx shapemodel) precision-amount)
    :cy (m/to-fixed (:cy shapemodel) precision-amount)
    :r (m/to-fixed (:r shapemodel) precision-amount)
    :fill (color (:color fill-data))
    :fill-opacity (:opacity fill-data)
    :stroke (color (:color stroke-data))
    :stroke-opacity (:opacity stroke-data)
    :stroke-width (:width stroke-data)}])

(defmethod svg :polygon [id, shapemodel, fill-data, stroke-data, sides-count]
  [:polygon
   {:id id
    :points (polygon/points shapemodel, sides-count)
    :fill (color (:color fill-data))
    :fill-opacity (:opacity fill-data)
    :stroke (color (:color stroke-data))
    :stroke-opacity (:opacity stroke-data)
    :stroke-width (:width stroke-data)}])


