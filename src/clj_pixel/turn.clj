(ns clj-pixel.turn)

(def left (hash-map :r 123, :g 131, :b 154))
(def right (hash-map :r 182, :g 149, :b 72))

(defn turn-left? [rgb]
  (if (= rgb left)
    true
    false))

(defn turn-right? [rgb]
  (if (= rgb right)
    true
    false))


