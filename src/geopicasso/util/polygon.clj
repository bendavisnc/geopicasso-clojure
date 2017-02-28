(ns geopicasso.util.polygon
  (:require 
    [common.math.helpers :refer [diff, to-fixed, perform-rotation, apply-scale, apply-translation]]
    )
  )


;;
;;
;; A ns for generating polygon points (for an svg polygon element)


(defn unit-polygon-points [n]
  "Given a number of sides ind n, return a list of points that represent a polygon."
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
  "Given a list of points, return a string in the format for the svg polygon points attribute value."
  (clojure.string/join
    " "
    (map
      (fn [[x, y]] (str x "," y))
      points)
    ))


(defn points [shapemodel, sides-ind]
  "Return a string that represents the points for a polygon corresponding to the shapemodel and side ind." 
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
      
