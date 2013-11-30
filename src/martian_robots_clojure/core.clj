(ns martian-robots-clojure.core
  (:require [clojure.string :as string])
  (:gen-class))

(defmacro dbg [x] `(let  [x# ~x]  (println "dbg:" '~x "=" x#) x#))

(defn turn-left [robot]
  (let [orientation ({:north :west
                      :west :south
                      :south :east
                      :east :north
                      } (robot :orientation))]
    (assoc robot :orientation orientation)))

(defn turn-right [robot]
  (let [orientation ({:north :east
                      :east :south
                      :south :west
                      :west :north
                      } (robot :orientation))]
    (assoc robot :orientation orientation)))

(defn move-forward [robot] ({:north (assoc robot :y (inc (robot :y)))
                             :east (assoc robot :x (dec (robot :x)))
                             :south (assoc robot :y (dec (robot :y)))
                             :west (assoc robot :x (inc (robot :x)))}
                            (robot :orientation))) 

(defn lookup-command [cmd] ({\L turn-left \R turn-right \F move-forward} cmd))
(defn lookup-orientation [orientation] ({ \N :north \E :east \S :south \W :west } orientation))
(defn process-instruction [robot instruction] (instruction robot))

(defn execute-instructions
  [robot instructions]
  (println instructions)
  (reduce process-instruction robot instructions))

(defn print-robot [{:keys [x y orientation]}] (println (str "Robot: X: " x " Y: " y " Orientation: " orientation)))

(defn parse-position
  [pos]
  (if (not (re-matches #"\d+ \d+ [N E S W]" pos)) nil
    (let [[x y orientation] (string/split pos #"\W")]
      {:x (Integer/parseInt x) 
       :y (Integer/parseInt y)
       :orientation (lookup-orientation (nth orientation 0))})))

(defn accept-position []
  (println "Please enter position: ")
  (let [pos (parse-position (read-line))] (if (nil? pos) (do (println "Invalid") (recur)) pos)))

(defn parse-instructions [ins]
  (let [cmds (map lookup-command ins)] (if (some nil? cmds) nil cmds)))

(defn accept-instructions []
  (println "Please enter instructions: ")
  (let [instructions (parse-instructions (read-line))] (if (nil? instructions) (do (println "Invalid") (recur)) instructions)))

(defn -main
  "Collects initial robot position and starts executing"
  [& args]
  (println "Martian Robots - Clojure Edition")
  (let [robot (accept-position)
        instructions (accept-instructions)]
    (print-robot (execute-instructions robot instructions))))
