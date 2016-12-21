(ns clj-pixel.core
  (:require [clojure.string :as s]))

(def height 60)
(def width 180)

(def file (slurp "resources/pixelarray"))
(def pixelvector (s/split file  #","))

(defn getLocation [x y]
  (bit-shift-left (+ x (* width y)) 2))

(defn check-color [data result x y]
  (let [idx (getLocation x y)
        rgb (nth data idx)
        updated (assoc result idx rgb)]
    (println rgb)
    (println (filter some? updated))
    updated))

(defn check-color-conj [data result x y]
  (let [concatenated (conj result (check-color data result x y))]
    concatenated))

(defn process [data]
  (dotimes [y height]
    (dotimes [x width]
      (let [initial (vec (make-array Object (count data)))
             result (check-color-conj data initial x y)]))))



