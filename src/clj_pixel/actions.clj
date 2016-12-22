(ns clj-pixel.actions)

(def left (hash-map :r 123, :g 131, :b 154))
(def right (hash-map :r 182, :g 149, :b 72))

(def draw-up (hash-map :r 7, :g 84, :b 19))
(def draw-left (hash-map :r 139, :g 57, :b 137))

(def stop (hash-map :r 51, :g 69, :b 169))

(defn turn-left? [rgb]
  (if (= rgb left)
    true
    false))

(defn turn-right? [rgb]
  (if (= rgb right)
    true
    false))

(defn draw-up? [rgb]
  (if (= rgb draw-up)
    true
    false))

(defn draw-left? [rgb]
  (if (= rgb draw-left)
    true
    false))

(defn dstop? [rgb]
  (if (= rgb stop)
    true
    false))




