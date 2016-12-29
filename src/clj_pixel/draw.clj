(ns clj-pixel.draw
  (:require [clj-pixel.actions :as act])
  (:use [clj-pixel.traverse]))

(defn concat-some
  [f vec1 vec2]
  ((fn [x] (filter f x))
    (concat vec1 vec2)))
(concat-some even? [1 2 3] [4 5 6])


(defn draw-y [x param-y f-continue f-recur f-right f-left]
  (println f-recur)
  (loop [y param-y]
    (when (fn [y] (f-continue y))                                 ;;up->(< y height)    down->(> 0 y)
      (println (str "x: " x " y: " y))
      (let [idx (get-location x y)
            rgb (get-rgb pixelvector idx)]
        (if (act/stop? rgb)
          (println "STOP Y!")
          (do
            (white idx)
            (cond
              (act/turn-right? rgb) (fn [x y] (f-right x y))      ;;up->move right x    down->move left x
              (act/turn-left? rgb) (fn [x y] (f-left x y))        ;;up->move lefx x     down->move right x
              :else (recur (fn [y] (f-recur y))))))))))           ;;up->(inc y)         down->(dec y)

(defn draw-x [param-x y f-continue f-recur f-right f-left]
  (loop [x param-x]
    (when (fn [x] (f-continue x))       ;;(>= x 0) LEFT
      (let [idx (get-location x y)
            rgb (get-rgb pixelvector idx)]
        (if (act/stop? rgb)
          (println "STOP X!")
          (do
            (white idx)
            (cond
              (act/turn-right? rgb) (fn [x y] (f-right x y))
              (act/turn-left? rgb) (fn [x y] (f-left x y))
              :else (recur (fn [x] (f-recur x))))))))))     ;;(dec x) LEFT

(defn draw []
  (draw-y 0 1 #(< % height) #(inc %) draw-x draw-y))
