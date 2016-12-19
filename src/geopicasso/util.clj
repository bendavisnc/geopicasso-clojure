(ns geopicasso.util
	(:require
    [common.math.helpers :refer [to-fixed]]
    [hiccup.core :refer [html]]
    )
 )

(defrecord ShapeModel [cx, cy, r])

(defn copy [shapemodel, argm]
  (map->ShapeModel
  	(merge shapemodel argm)))

(defmulti svg
	(fn [& args]
		(cond
			(and
				(instance? geopicasso.util.ShapeModel (first args))
				(= (count args) 4))
				:standard
			; (instance? geopicasso.util.ShapeModel (first args))
				; :shapemodel
			:else
			  (throw (Exception. (str "Unsupported svg conversion operation."))))))


; (defmethod svg :shapemodel [shapemodel, config]
	; (let [
      ; precision-amt 4
    ; ]
		; [:circle
		  ; {
	     ; :cx (to-fixed (:cx shapemodel) precision-amt)
	     ; :cy (to-fixed (:cy shapemodel) precision-amt)
	     ; :r (to-fixed (:r shapemodel) precision-amt)
	     ; :fill-opacity 0
	     ; :stroke "white"
	     ; :stroke-width 0.2
	    ; }]))

(defmethod svg :standard [shapemodel, fill-data, stroke-data, config]
	(let [
      precision-amt 4
    ]
		[:circle
		  {
	     :cx (to-fixed (:cx shapemodel) precision-amt)
	     :cy (to-fixed (:cy shapemodel) precision-amt)
	     :r (to-fixed (:r shapemodel) precision-amt)
	     :fill (:color fill-data)
	     :fill-opacity (:opacity fill-data)
	     :stroke (:color stroke-data)
	     :stroke-opacity (:opacity stroke-data)
	     :stroke-width (:width stroke-data)
	    }]))

