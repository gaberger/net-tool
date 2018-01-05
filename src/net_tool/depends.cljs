(ns net-tool.depends
  (:require [cljs.nodejs :as node]
            [clojure.string :as str]))

(defonce spawnSync (.-spawnSync (node/require "child_process")))

(defn spawn
  [cmd-line]
  (let [[cmd & args] (str/split cmd-line #" ")]
    (if (not (nil? args))
      (spawnSync cmd
                 (clj->js args))
      (spawnSync cmd))))
