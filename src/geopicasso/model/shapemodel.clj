(ns geopicasso.model.shapemodel)

(defrecord ShapeModel [cx, cy, r])

(defn create [cx, cy, r]
  (new ShapeModel cx, cy, r))

(defn from-map [m]
  (map->ShapeModel m))

(defn copy [shapemodel, argm]
  (map->ShapeModel
    (merge shapemodel argm)))
