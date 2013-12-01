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
  (reduce process-instruction robot instructions))

(defn print-robot [{:keys [x y orientation]}] (println (str "Robot: X: " x " Y: " y " Orientation: " orientation)))

(defn getInput [prompt, parse-fn]
  (println prompt)
  (let [parsed (parse-fn (read-line))] (if (nil? parsed) (do (println "Invalid") (recur prompt parse-fn)) parsed)))

(defn parse-bounds [bounds]
  (if (re-matches #"\d+ \d+" bounds)
    (let [[strX strY] (string/split bounds #"\W") x (Integer/parseInt strX) y (Integer/parseInt strY) min-bound 1 max-bound 50]
      (if (and (<= min-bound x max-bound) (<= min-bound y max-bound))
        { :x x :y y } nil)) nil))

(defn accept-bounds [] (getInput "Please enter bounds: " parse-bounds))

(defn parse-position
  [pos]
  (if (not (re-matches #"\d+ \d+ [N E S W]" pos)) nil
    (let [[x y orientation] (string/split pos #"\W")]
      {:x (Integer/parseInt x) 
       :y (Integer/parseInt y)
       :orientation (lookup-orientation (nth orientation 0))})))
(defn accept-position [] (getInput "Please enter position: " parse-position))

(defn parse-instructions [ins]
  (let [cmds (map lookup-command ins)] (if (some nil? cmds) nil cmds)))
(defn accept-instructions [] (getInput "Please enter robot instructions: " parse-instructions))

(defn -main
  "Collects initial robot position and starts executing"
  [& args]
  (println "Martian Robots - Clojure Edition")
  (let [world-bounds (accept-bounds)
        robot (accept-position)
        instructions (accept-instructions)]
    (print-robot (execute-instructions robot instructions))))
