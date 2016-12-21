(ns clj-pixel.core
  (:require [clojure.string :as s]))

(def height 60)
(def width 180)

(def file (slurp "resources/pixelarray"))
(def arr (s/split file  #","))

(defn getLocation [x y]
  (bit-shift-left (+ x (* width y)) 2))

(defn process [data]
  (dotimes [y height]
    (dotimes [x width]
      (println (str "" (getLocation x  y))))))



