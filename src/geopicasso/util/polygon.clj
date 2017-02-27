(ns geopicasso.util.polygon
  (:require 
    [common.math.helpers :refer [diff, to-fixed, perform-rotation, apply-scale, apply-translation]]
    )
  )


(defn unit-polygon-points [n]
  (let [
      first-point [0.5, 0]
      theta (/ 360.0 n)
      next-point
        (fn [previous-point]
          (perform-rotation previous-point, theta, [0.5, 0.5]))
    ]
    (loop [points [first-point]]
      (cond
        (= (count points) n)
          points
        :else
          (recur (cons (next-point (first points)) points))))))

(defn points-serialized [points]
  (clojure.string/join
    " "
    (map
      (fn [[x, y]] (str x "," y))
      points)
    ))


(defn points [shapemodel, sides-ind]
  (let [
      scale-amt (/ (:r shapemodel) 0.5)
      [dx, dy] 
        (diff 
          [(:cx shapemodel), (:cy shapemodel)]
          (apply-scale [0.5, 0.5] scale-amt))
    ]
    (points-serialized
      (map
        (fn [unit-point]
          (->
            unit-point
            (apply-scale scale-amt)
            (apply-translation dx, dy)))
        (unit-polygon-points sides-ind)))))
      
