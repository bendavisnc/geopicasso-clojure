(ns geopicasso.util
	(:require
    [common.math.helpers :refer [to-fixed]]
    [hiccup.core :refer [html]]
    ))

(defrecord ShapeModel [cx, cy, r])

(defn copy [shapemodel, argm]
  (map->ShapeModel
  	(merge shapemodel argm)))

(defmulti svg
	(fn [shapemodel, config]
		(cond
			(instance? geopicasso.util.ShapeModel shapemodel)
				:shapemodel
			:else
			  (throw (Exception. (str "Unsupported model (" shapemodel ") type for svg conversion."))))))


(defmethod svg :shapemodel [shapemodel, config]
	(let [
      precision-amt 4
    ]
		[:circle
		  {
	     :cx (to-fixed (:cx shapemodel) precision-amt)
	     :cy (to-fixed (:cy shapemodel) precision-amt)
	     :r (to-fixed (:r shapemodel) precision-amt)
	     :fill-opacity 0
	     :stroke "white"
	     :stroke-width 0.2
	    }]))



