(ns wireframes.transform-tests
  (:refer-clojure :exclude [identity concat])
  (:use [clojure.test]
        [wireframes.transform]))

(deftest concat-identity-identity
  (is (= identity
         (concat
           identity
           identity))))

(deftest concat-identity-vector
  (is (= (translate 4 6 9)
         (concat
           identity
           (translate 4 6 9)))))

(deftest concat-two-vectors
  (is (= (translate 8 10 21)
         (concat
           (translate 5 3 2)
           (translate 3 7 19)))))

(deftest concat-three-vectors
  (is (= (translate 11 12 19)
         (concat
           (translate 3 5 6)
           (translate 5 4 3)
           (translate 3 3 10)))))

; TODO: rotate test --> concat is not associative

(deftest transform-single-point
  (is (= [8.0 10.0 13.0]
         (transform-point
           (translate 3 5 6)
           [5 5 7]))))

(deftest perspective-point
  (is (= [22.909090909090907 11.454545454545453]
         ((perspective 20) [12.6 6.3 9.0]))))

(deftest normal-3d-triangle
  (is (= [0.0 -1.0 0.0]
         (normal
           [3.0  5.0   6.0]
           [7.0  5.0  11.0]
           [3.0  5.0  14.0]))))

(deftest triangulation
  (is (= [[1 2 3]] (triangulate [1 2 3])))
  (is (= [[1 2 3] [1 3 4] [1 4 5]] (triangulate [1 2 3 4 5])))
  (is (nil? (triangulate [])))
  (is (nil? (triangulate [1])))
  (is (nil? (triangulate [1 2]))))
