(ns clj-pixel.image
  (:require [mikera.image.core :as img]
            [mikera.image.colours :as color]
             [clojure.string :as s]))

  (def image (img/new-image 180 60))
  (def pixel-array (slurp "resources/result"))

  (def arr2 (s/split pixel-array #","))

  (def pixels (doall (map #(Integer/parseInt %) (drop-last (rest arr2)))))

  (defn save []
    (img/set-pixels image (int-array pixels))
    (img/save image "/home/leena/Pictures/newnut.png" :quality 0.9 :progressive true))
