(ns simple-test
  (:require [martian-robots-clojure.core :as core])
  (:use expectations))

(defn n-times [f initial n] (nth (iterate f initial) n))

(def robot {:x 0 :y 0 :orientation :north})

; Command lookup
(expect (core/lookup-command \L) core/turn-left)
(expect (core/lookup-command \R) core/turn-right)
(expect (core/lookup-command \F) core/move-forward)

; Orientation lookup
(expect :north (core/lookup-orientation \N))
(expect :east (core/lookup-orientation \E))
(expect :south (core/lookup-orientation \S))
(expect :west (core/lookup-orientation \W))

; Turning
(expect (core/turn-left robot) {:x 0 :y 0 :orientation :west})
(expect (n-times core/turn-left robot 4) robot)
(expect (core/turn-right robot) {:x 0 :y 0 :orientation :east})
(expect (n-times core/turn-right robot 4) robot)

; Moving
(expect (core/move-forward robot) {:x 0 :y 1 :orientation :north})
(expect (core/move-forward {:x 0 :y 0 :orientation :east}) {:x -1 :y 0 :orientation :east})
(expect (core/move-forward {:x 0 :y 0 :orientation :south}) {:x 0 :y -1 :orientation :south})
(expect (core/move-forward {:x 0 :y 0 :orientation :west}) {:x 1 :y 0 :orientation :west})

; Sets of instructions
(expect (core/execute-instructions robot "RFFFLLFFFR") robot)

; Printing
(expect (interaction (println "Robot: X: 0 Y: 0 Orientation: :north")) (core/print-robot robot))
