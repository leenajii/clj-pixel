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

(defn draw-up [x y]
  (dotimes [y height]
    (let [idx (get-location x y)]
      (if (not (act/stop? (get-rgb pixelvector idx)))
        (white (get-location x y))))))

(defn draw-left [x y]
  (when (> x 0)
    (let [idx (get-location x y)]
    (if (act/stop? (get-rgb idx))
      "I found it! Now I want to stop executing!"
      (do
        (white idx)
        (recur (dec x)))))))

(defn action? [rgb idx x y]
  (cond
    (act/draw-up? rgb) (do (white idx)(draw-up x y))
    (act/draw-left? rgb) (do (white idx)(draw-left x y))
    (act/turn-left? rgb) (white idx)
    (act/turn-right? rgb) (white idx)
    (act/stop? rgb) (color idx (hash-map :r 55 :g 55 :b 55))
    :else (color idx {:r 0 :g 0 :b 0})))

(defn process []
  (dotimes [y height]
    (dotimes [x width]
      (let [idx (get-location x y)]
        (action? (get-rgb pixelvector idx) idx x y))))
  (println @result)
  (println (count @result))
  (spit "resources/result" (s/replace (s/replace @result #" " ",") #"\"" "")))
