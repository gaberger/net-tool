(ns net-tool.core
  (:require [cljs.reader :refer [read-string]]
            [cljs.nodejs :as node]
            [clojure.pprint]
            [clojure.string :as str]))

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


(def running-domains 
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

              result)
          v)))


(defn -main []
  (println "Starting..")
  (let [interfaces (into []  (:interface (:devices (:domain (dump-xml "director")))))]
      (clojure.pprint/print-table (parse-dump-xml interfaces)))






 ; (clojure.walk/postwalk  strip_attributes  {:mac {:_attributes {:address "52:54:00:75:3b:5a"}}}))

 {:interface
   [{:_attributes {:type "bridge"},
     :mac {:_attributes {:address "52:54:00:75:3b:5a"}},
     :source {:_attributes {:bridge "br-pub-api"}},
     :target {:_attributes {:dev "vnet2"}},
     :model {:_attributes {:type "virtio"}},
     :alias {:_attributes {:name "net0"}},
     :address
     {:_attributes
      {:type "pci",
       :domain "0x0000",
       :bus "0x00",
       :slot "0x03",
       :function "0x0"}}}
    {:_attributes {:type "bridge"},
     :mac {:_attributes {:address "52:54:00:ca:bf:f3"}},
     :source {:_attributes {:bridge "br-prov"}},
     :target {:_attributes {:dev "vnet3"}},
     :model {:_attributes {:type "virtio"}},
     :alias {:_attributes {:name "net1"}},
     :address
     {:_attributes
      {:type "pci",
       :domain "0x0000",
       :bus "0x00",
       :slot "0x04",
       :function "0x0"}}}
    {:_attributes {:type "bridge"},
     :mac {:_attributes {:address "52:54:00:6f:ce:43"}},
     :source {:_attributes {:bridge "br-mgmt"}},
     :target {:_attributes {:dev "vnet4"}},
     :model {:_attributes {:type "virtio"}},
     :alias {:_attributes {:name "net2"}},
     :address
     {:_attributes
      {:type "pci",
       :domain "0x0000",
       :bus "0x00",
       :slot "0x05",
       :function "0x0"}}}
    {:_attributes {:type "bridge"},
     :mac {:_attributes {:address "52:54:00:58:98:ce"}},
     :source {:_attributes {:bridge "br-priv-api"}},
     :target {:_attributes {:dev "vnet5"}},
     :model {:_attributes {:type "virtio"}},
     :alias {:_attributes {:name "net3"}},
     :address
     {:_attributes
      {:type "pci",
       :domain "0x0000",
       :bus "0x00",
       :slot "0x06",
       :function "0x0"}}}]})


