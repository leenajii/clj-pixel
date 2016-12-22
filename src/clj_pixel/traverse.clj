(ns clj-pixel.traverse
  (:require [clojure.string :as s])
  (:require [clj-pixel.actions :as act]))

(def file (slurp "resources/pixelarray"))
(def pixelvector (s/split file  #","))

(def result (atom pixelvector))

(def height 60)
(def width 180)

(defn get-location [x y]
  (bit-shift-left (+ x (* width y)) 2))

(defn get-rgb [data idx]
  (hash-map :r (nth data idx) :g (nth data (+ idx 1)) :b (nth data (+ idx 2))))

(defn color [idx rgb]
    (reset! result (assoc @result idx (get rgb :r)))
    (reset! result (assoc @result (+ idx 1) (:g rgb)))
    (reset! result (assoc @result (+ idx 2) (:b rgb)))
    (reset! result (assoc @result (+ idx 3) 255)))

(defn white [idx]
  (color idx (hash-map :r 255 :g 255 :b 255)))

(defn action? [rgb idx]
  (cond
    (act/draw-up? rgb) (white idx)
    (act/draw-left? rgb) (white idx)
    (act/turn-left? rgb) (white idx)
    (act/turn-right? rgb) (white idx)
    (act/stop? rgb) (color idx (hash-map :r 55 :g 55 :b 55))
    :else (color idx rgb)))

(defn process []
  (dotimes [y height]
    (dotimes [x width]
      (let [idx (get-location x y)]
        (action? (get-rgb pixelvector idx) idx))))
  (println @result)
  (println (count @result))
  (spit "resources/result" (s/replace (s/replace @result #" " ",") #"\"" "")))
