(ns geopicasso.svg-element
  (:require
    [common.math.helpers :as m]
    [geopicasso.util.polygon :as polygon]
    [geopicasso.settings :refer [precision-amount]]))

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
    :fill (:color fill-data)
    :fill-opacity (:opacity fill-data)
    :stroke (:color stroke-data)
    :stroke-opacity (:opacity stroke-data)
    :stroke-width (:width stroke-data)}])

(defmethod svg :polygon [id, shapemodel, fill-data, stroke-data, sides-count]
  [:polygon
   {:id id
    :points (polygon/points shapemodel, sides-count)
    :fill (:color fill-data)
    :fill-opacity (:opacity fill-data)
    :stroke (:color stroke-data)
    :stroke-opacity (:opacity stroke-data)
    :stroke-width (:width stroke-data)}])


