(ns wireframes.bezier)

(defn- add
  "Add two points"
  [pa pb]
  (mapv + pa pb))

(defn- mult
  "Multiply a point [x,y,z] by a constant c"
  [pt c]
  (mapv (partial * c) pt))

(defn- evaluate-bezier-curve [t control-points]
  (let [omt (- 1 t) ; = one-minus-t
        b   [(* omt omt omt)
             (* 3 t omt omt)
             (* 3 t t omt)
             (* t t t)]]
    (reduce add (map mult control-points b))))

(defn- evaluate-bezier-patch [u v control-points]
  (let [u-curve (mapv
                  (partial evaluate-bezier-curve u)
                  (partition 4 control-points))]
    (evaluate-bezier-curve v u-curve)))

(defn line-points [divisions control-points]
  (let [divisions (double divisions)]
    (vec
      (for [i (range (inc divisions))]
        (evaluate-bezier-curve
          (/ i divisions)
          control-points)))))

(defn surface-points [divisions control-points]
  (let [divisions (double divisions)]
    (vec
      (for [j (range (inc divisions))
            i (range (inc divisions))]
        (evaluate-bezier-patch
          (/ i divisions)
          (/ j divisions)
          control-points)))))