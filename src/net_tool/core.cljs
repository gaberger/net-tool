(ns net-tool.core
  (:require [cljs.reader :refer [read-string]]
            [cljs.nodejs :as node]
            [clojure.pprint]
            [clojure.string :as str]
            [cljs-node-io.core :as io :refer [slurp spit]]))

(node/enable-util-print!)

(def mock true)


(def spawnSync (.-spawnSync (node/require "child_process")))
(def xml-parser (node/require "xml-js"))

(defn parse-xml [xml]
  (.xml2js xml-parser xml #js{:compact true}))

(defn spawn
  [cmd-line]
  (let [[cmd & args] (str/split cmd-line #" ")]
   (if (not (nil? args)) 
      (spawnSync cmd 
            (clj->js args)) 
      (spawnSync cmd)))) 


(defn running-domains []
    (-> 
      (spawn "virsh list --name --state-running")
      (js->clj :keywordize-keys true) 
      :stdout
      .toString
      (str/split #"\n")))


(defn dump-xml [domain]
  (let [xml (-> 
              (spawn (str "virsh dumpxml " domain))  
              (js->clj :keywordize-keys true)
              :stdout
              .toString)]
      (js->clj (parse-xml xml) :keywordize-keys true)))

(def dump-xml-mock
  (js->clj  (parse-xml (slurp "src/dumpxml.txt")) :keywordize-keys true))

(defn parse-dump-xml [v]
  (mapv (fn [m]
            (let [result {:type (get-in m [:_attributes :type])
                          :mac (get-in m [:mac :_attributes :address])
                          :bridge (get-in m [:source :_attributes :bridge])
                          :hostdev (get-in m [:target :_attributes :dev])
                          :guestdriver (get-in m [:model :_attributes :type])
                          :alias (get-in m [:alias :_attributes :name])
                          :pciaddress {:domain (get-in m [:address :_attributes :domain])
                                          :bus (get-in m [:address :_attributes :bus])
                                          :slot (get-in m [:address :_attributes :slot])
                                          :function (get-in m [:address :_attributes :function])}}]

              result))
        v))

(defn interfaces []
 (->> (dump-xml "director") :domain :devices :interface))

(def mock-interfaces (->> dump-xml-mock :domain :devices :interface))

(defn -main []
  (println "Starting..")
  (let [interfaces (if mock mock-interfaces (interfaces))]
      (clojure.pprint/print-table (parse-dump-xml interfaces))))


(set! *main-cli-fn* -main)


