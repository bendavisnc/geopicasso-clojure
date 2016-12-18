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
      (mapv
        #(to-fixed % 4)
        [
          (+
            (* x (Math/cos theta))
            (* y (Math/sin theta))),
          (+
            (* x (Math/sin theta) -1)
            (* y (Math/cos theta)))]))))

(def apply-translation
  (fn [coord, dx, dy]
    (let [
        [x, y] coord
      ]
      (mapv
        #(to-fixed % 4)
        [
          (+ x dx)
          (+ y dy)]))))


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

