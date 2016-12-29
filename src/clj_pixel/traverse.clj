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
    (reset! result (assoc @result (+ idx 1) (get rgb :g)))
    (reset! result (assoc @result (+ idx 2) (get rgb :b)))
    (reset! result (assoc @result (+ idx 3) 255)))

(defn white [idx]
  (color idx (hash-map :r 255 :g 255 :b 255)))

#_(defn draw-up [x y]
  (dotimes [y height]
    (let [idx (get-location x y)]
      (if (not (act/stop? (get-rgb pixelvector idx)))
        (white (get-location x y))))))

(defn draw-left2 [param-x y]
  (loop [x param-x]
    (when (>= x 0)
      (let [idx (get-location x y)
            rgb (get-rgb pixelvector idx)]
        (if (act/stop? rgb)
          (println "STOP LEFT!")
          (do
            (white idx)
            (recur (dec x))))))))

(defn draw-right [param-x y]
  (loop [x param-x]
    (when (< x width)
      (let [idx (get-location x y)
            rgb (get-rgb pixelvector idx)]
        (if (act/stop? rgb)
          (println "STOP LEFT!")
          (do
            (white idx)
            (recur (inc x))))))))

(defn draw-up [x param-y]
  (loop [y param-y]
    (when (< y height)
      (let [idx (get-location x y)
            rgb (get-rgb pixelvector idx)]
        (if (act/stop? rgb)
          (println "STOP UP!")
          (do
            (white idx)
            (cond
              (act/turn-right? rgb) (draw-right x y)
              (act/turn-left? rgb) (draw-left2 x y)
              :else (recur (inc y)))))))))

(defn draw-down [x param-y]
  (loop [y param-y]
    (when (>= y 0)
      (let [idx (get-location x y)
            rgb (get-rgb pixelvector idx)]
        (if (act/stop? rgb)
          (println "STOP DOWN!")
          (do
            (white idx)
            (cond
              (act/turn-right? rgb) (draw-left2 x y)
              (act/turn-left? rgb) (draw-right x y)
              :else (recur (dec y)))))))))

(defn draw-left [param-x y]
  (loop [x param-x]
    (when (>= x 0)
      (let [idx (get-location x y)
            rgb (get-rgb pixelvector idx)]
      (if (act/stop? rgb)
        (println "STOP LEFT!")
        (do
          (white idx)
          (cond
            (act/turn-right? rgb) (draw-up x y)
            (act/turn-left? rgb) (draw-down x y)
            :else (recur (dec x)))))))))

(defn action? [rgb idx x y]
  (cond
    (act/draw-up? rgb) (do (white idx)(draw-up x y))
    (act/draw-left? rgb) (do (white idx)(draw-left x y))
    (act/stop? rgb) (color idx (hash-map :r 55 :g 55 :b 55))
    :else (color idx {:r 0 :g 0 :b 0})))

(defn process []
  (dotimes [x width]
    (dotimes [y height]
      (let [idx (get-location x y)]
        (action? (get-rgb pixelvector idx) idx x y))))
  (println @result)
  (println (count @result))
  (spit "resources/result" (s/replace (s/replace @result #" " ",") #"\"" "")))
