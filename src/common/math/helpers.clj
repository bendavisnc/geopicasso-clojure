(ns common.math.helpers)

(defn to-fixed [n, p]
  (let [k
        (Math/pow 10 p),
        mult
        (* n k),
        rounded
        (Math/round mult),
        divided
        (/ rounded k)]
    divided))

(defn to-radians [degrees]
  (* degrees (/ Math/PI 180)))

(defn apply-rotation [[x, y], degrees]
  (let [theta (to-radians degrees)]
    [(+ (* x (Math/cos theta)) (* y (Math/sin theta))),
     (+ (* x (Math/sin theta) -1) (* y (Math/cos theta)))]))

(defn apply-translation [[x, y], dx, dy]
  [(+ x dx),
   (+ y dy)])

(defn apply-scale [[x, y], s]
  [(* x s), (* y s)])

(defn diff [[x1, y1], [x2, y2]]
  [(- x1 x2), (- y1 y2)])

(defn perform-rotation [coord, degrees, [dx, dy]]
  (-> coord
    (apply-translation (- dx) (- dy))
    (apply-rotation  degrees)
    (apply-translation dx dy)))

