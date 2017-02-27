(ns common.math.helpers)

(def to-fixed
  (fn [n, p]
  	(let [
  		  k 
	      	(Math/pow 10 p)
        mult 
          (* n k)
        rounded
          (Math/round mult)
        divided
          (/ rounded k)
      ]
      divided)))

  	; (Math/round
	  	; (* n (Math/pow 10 p))

(def to-radians
  (fn [degrees]
    (*
      degrees
      (/ Math/PI 180))))

(def apply-rotation
  (fn [coord, degrees]
    (let [
        theta (to-radians degrees)
        [x, y] coord
      ]
      [
        (+
          (* x (Math/cos theta))
          (* y (Math/sin theta))),
        (+
          (* x (Math/sin theta) -1)
          (* y (Math/cos theta)))])))

(def apply-translation
  (fn [coord, dx, dy]
    (let [
        [x, y] coord
      ]
      [
        (+ x dx)
        (+ y dy)])))

(def apply-scale
  (fn [coord, s]
    (mapv
      #(* % s)
      coord
      )))

(def diff
  (fn [[x1, y1], [x2, y2]]
    [(- x1 x2), (- y1 y2)]))



(def perform-rotation
  (fn [coord, degrees, pivot-coord]
    (let [
        [dx, dy] pivot-coord
      ]
      ( 
        (comp
          #(apply-translation % dx dy)
          #(apply-rotation % degrees)
          #(apply-translation % (- dx) (- dy)))
        coord))))

