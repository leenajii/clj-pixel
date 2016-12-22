(ns clj-pixel.traverse
  (:require [clojure.string :as s])
  (:require [clj-pixel.actions :as act]))

(def height 60)
(def width 180)

(def file (slurp "resources/pixelarray"))
(def pixelvector (s/split file  #","))

(defn get-location [x y]
  (bit-shift-left (+ x (* width y)) 2))

(defn get-rgb [data idx]
  (hash-map :r (nth data idx) :g (nth data (+ idx 1)) :b (nth data (+ idx 2))))

(defn action? [rgb]
  (cond
    (act/draw-up? rgb) (str "Draw up: " rgb)
    (act/draw-left? rgb) (str "Draw left: " rgb)
    (act/turn-left? rgb) (str "Turn left: " rgb)
    (act/turn-right? rgb) (str "Turn right: " rgb)
    (act/stop? rgb) (str "STOP: " rgb)
    :else (str "No action: " rgb)))

(defn process []
  (dotimes [y height]
    (dotimes [x width]
      (println (action? (get-rgb pixelvector (get-location x y)))))))
