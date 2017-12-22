(ns lumo-test-1.core
  (:require [cljs.reader :refer [read-string]]
            [cljs.nodejs :as node]
            [clojure.string :as str]
            [clojure.data.xml :refer [parse]]))



(def spawnSync (.-spawnSync (node/require "child_process")))
(def xml-parser (node/require "fast-xml-parser"))

(defn spawn
  [cmd-line]
  (let [[cmd & args] (str/split cmd-line #" ")]
   (if (not (nil? args)) 
      (spawnSync cmd 
            (clj->js args)) 
      (spawnSync cmd)))) 


(def running-domains 
    (-> 
      (spawn "virsh list --name --state-running")
      (js->clj :keywordize-keys true) 
      :stdout
      .toString
      (str/split #"\n")))  



;INSTALL
;npm install -g lumo-cljs --unsafe-perm
;curl --silent --location https://rpm.nodesource.com/setup_9.x | sudo bash -
;sudo yum -y install nodejs