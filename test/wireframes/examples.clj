(ns wireframes.examples
  (:use [clojure.test])
  (:require [taoensso.timbre.profiling :as profiling :refer (p profile)]
            [wireframes.transform :as t]
            [wireframes.shapes.primitives :as p]
            [wireframes.shapes.curved-solids :as cs]
            [wireframes.shapes.platonic-solids :as ps]
            [wireframes.shapes.patch-loader :as pl]
            [wireframes.shapes.wavefront-loader :as wl]
            [wireframes.shapes.stl-loader :as sl]
            [wireframes.renderer.bitmap :as b])
  (:import [java.awt Color]))

;(set! *unchecked-math* true)

(defn harness [{:keys [shape focal-length lighting-position transform
                       draw-fn filename size style fill-color] :as opts}]
  (let [dir (str "doc/gallery/" (name style) "/")]
    (.mkdir (clojure.java.io/file dir))
    (printf "%-14s %-20s" style filename)
    (flush)
    (is
      (b/write-png
        (let [start-time (System/nanoTime)
              img (b/->img
                    (partial b/draw-solid opts)
                    (or size [1000 900]))]
          (printf "--> %10.4f msecs\n" (/ (- (System/nanoTime) start-time) 1000000.0))
          (flush)
          img)
        (str dir filename)))))

(deftest ^:examples complex-solids
  (let [teapot (pl/load-shape "data-files/newell-teapot/teapot" 16)
        teacup (pl/load-shape "data-files/newell-teapot/teacup" 16)
        plane (wl/load-shape "data-files/obj_IconA5.obj")
        sonic (wl/load-shape "data-files/Sonic.obj")
        avent (wl/load-shape "data-files/Avent.obj")
        rpi-case (sl/load-shape  "data-files/RichRap_Raspbery_Pi_Case_Bottom.stl")]
    (doseq [style [:transparent :translucent :opaque :shaded]]

      (harness {
        :filename "torus.png"
        :style style
        :shape (cs/make-torus 1 3 60 60)
        :focal-length 3
        :size [400 400]
        :transform (t/combine
                     (t/rotate :z (t/degrees->radians 65))
                     (t/rotate :y (t/degrees->radians -30))
                     (t/translate 0 0 16))})

      (harness {
        :filename "sphere.png"
        :style style
        :shape (cs/make-sphere 3 30)
        :focal-length 3
        :size [400 400]
        :transform (t/combine
                     (t/rotate :x (t/degrees->radians 60))
                     (t/rotate :y (t/degrees->radians -15))
                     (t/translate 0 0 16))})

      (harness {
        :filename "tetrahedron.png"
        :style style
        :shape ps/tetrahedron
        :focal-length 5
        :size [300 300]
        :transform (t/combine
                     (t/rotate :z (t/degrees->radians 55))
                     (t/rotate :y (t/degrees->radians -20))
                     (t/scale 2)
                     (t/translate 0 0 16))})

      (harness {
        :filename "cube.png"
        :style style
        :shape ps/cube
        :focal-length 5
        :size [300 300]
        :transform (t/combine
                     (t/rotate :z (t/degrees->radians 55))
                     (t/rotate :y (t/degrees->radians -20))
                     (t/scale 2)
                     (t/translate 0.5 -1.5 16))})

      (harness {
        :filename "icosahedron.png"
        :style style
        :shape ps/icosahedron
        :focal-length 5
        :size [300 300]
        :transform (t/combine
                     (t/rotate :z (t/degrees->radians 55))
                     (t/rotate :y (t/degrees->radians -30))
                     (t/translate 0 0 16))})

      (harness {
        :filename "wineglass.png"
        :fill-color (Color. 0xEAF5FC)
        :style style
        :shape (cs/make-wineglass 48)
        :focal-length 20
        :size [400 400]
        :transform (t/combine
                     (t/rotate :z (t/degrees->radians 15))
                     (t/rotate :x (t/degrees->radians 20))
                     (t/scale 1.75)
                     (t/translate 0.1 -0.4 10))})

      (harness {
        :filename "teapot.png"
        :style style
        :shape teapot
        :focal-length 10
        :transform (t/combine
                     (t/rotate :z (t/degrees->radians 35))
                     (t/rotate :x (t/degrees->radians -120))
                     (t/translate 0 -1 40))})

      (harness {
        :filename "teacup.png"
        :style style
        :shape teacup
        :focal-length 10
        :transform (t/combine
                     (t/rotate :x (t/degrees->radians -30))
                     (t/translate 0 0 25))})


      (harness {
        :filename "icon-a5.png"
        :style style
        :shape plane
        :focal-length 4
        :transform (t/combine
                     (t/rotate :z (t/degrees->radians 25))
                     (t/rotate :y (t/degrees->radians 130))
                     (t/rotate :x (t/degrees->radians -50))
                     (t/translate 3 -5 60))})

      (harness {
        :filename "sonic.png"
        :style style
        :shape sonic
        :focal-length 3.5
        :transform (t/combine
                     (t/rotate :x (t/degrees->radians 0))
                     (t/rotate :y (t/degrees->radians -150))
                     (t/translate 3 -15 60))})

      (harness {
        :filename "aventador.png"
        :style style
        :size [1024 550]
        :shape avent
        :focal-length 4
        :transform (t/combine
                     (t/rotate :x (t/degrees->radians 20))
                     (t/rotate :y (t/degrees->radians -120))
                     (t/rotate :z (t/degrees->radians -20))
                     (t/translate -0.6 -0.28 8.85))})


    (harness {
        :filename "rpi-case.png"
        :style style
        :shape (sl/load-shape  "data-files/RichRap_Raspbery_Pi_Case_Bottom.stl")
        :focal-length 24
        :transform (t/combine
                     (t/rotate :z (t/degrees->radians 25))
                     (t/rotate :y (t/degrees->radians 160))
                     (t/rotate :x (t/degrees->radians 20))
                     (t/scale 0.1)
                     (t/translate 3 -5 210))}))))

(comment
(harness {
  :filename "wineglass.png"
  :style :shaded
  :fill-color (Color. 0xeaf5fc)
  :shape (cs/make-wineglass 60)
  :focal-length 20
  :size [900 900]
  :transform (t/combine
               (t/rotate :z (t/degrees->radians 15))
               (t/rotate :x (t/degrees->radians 20))
               (t/scale 1.75)
               (t/translate 0.1 -0.4 10))})

(harness {
  :filename "grid.png"
  :style :translucent
  :shape (p/make-grid -2 -2 10 10)
  :focal-length 60
  :transform (t/combine
               (t/rotate :z (t/degrees->radians 15))
               (t/rotate :x (t/degrees->radians 80))
               (t/scale 0.1)
               (t/translate 0 -0.2 5))})

(harness {
  :filename "rpi-case.png"
  :style :translucent
  :shape (sl/load-shape  "data-files/RichRap_Raspbery_Pi_Case_Bottom.stl")
  :focal-length 24
  :transform (t/combine
               (t/rotate :z (t/degrees->radians 25))
               (t/rotate :y (t/degrees->radians 160))
               (t/rotate :x (t/degrees->radians 20))
               (t/scale 0.1)
               (t/translate 3 -5 210))})

(harness {
  :filename "cube.png"
  :style :translucent
  :shape ps/cube
  :focal-length 5
  :size [300 300]
  :transform (t/combine
               (t/rotate :z (t/degrees->radians 55))
               (t/rotate :y (t/degrees->radians -20))
               (t/scale 2)
               (t/translate 0.5 -1.5 16))})

(harness {
  :filename "knurled-cylinder.png"
  :style :translucent
  :shape (p/extrude
           (cs/make-star 1 1.2 50)
           (t/combine
             (t/rotate :z (t/degrees->radians 3))
             (t/scale 0.95)
             (t/rotate :y (t/degrees->radians 2))
             (t/translate 0 0 0.2))
           50)
  :focal-length 5
  :size [900 900]
  :transform (t/combine
               (t/rotate :x (t/degrees->radians 15))
               (t/rotate :y (t/degrees->radians -20))
               (t/translate 0.5 -0.0 16))})


(sl/save-shape
  (cs/make-torus 1 3 60 60)
  "Torus, created with https://github/rm-hull/wireframes [October 16 2013]"
  "doc/gallery/torus.stl")

(sl/save-shape
  (cs/make-wineglass 60)
  "Wineglass, created with https://github/rm-hull/wireframes [October 16 2013]"
  "doc/gallery/wineglass.stl")

(sl/save-shape
  (pl/load-shape "data-files/newell-teapot/teapot" 16)
  "Utah Teapot, created with https://github/rm-hull/wireframes [October 16 2013]"
  "doc/gallery/teapot.stl")

 (sl/save-shape
(p/extrude
           (cs/make-star 1 1.1 36)
           (t/combine
             (t/rotate :z (t/degrees->radians 3))
             (t/scale 0.95)
             (t/rotate :y (t/degrees->radians 2))
             (t/translate 0 0 0.2))
           50)
  "Weird swirly thing, created with https://github/rm-hull/wireframes [October 16 2013]"
  "doc/gallery/weird-swirly-thing.stl")



)
