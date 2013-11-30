(ns martian-robots-clojure.core
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
(defn process-instruction [robot instruction] ((lookup-command instruction) robot))

(defn execute-instructions
  [robot instructions]
  (reduce process-instruction robot instructions))

(defn print-robot [{:keys [x y orientation]}] (println (str "Robot: X: " x " Y: " y " Orientation: " orientation)))

(defn -main
  "Collects initial robot position and starts executing"
  [& args]
  (println "Martian Robots - Clojure Edition")
  (let [robot {:x 0 :y 0 :orientation :north}]
    (print-robot (execute-instructions robot "FFFLR"))))
