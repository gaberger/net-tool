(ns net-tool.core
  (:require [cljs.reader :refer [read-string]]
            [clojure.pprint :as pprint]
            [clojure.string :as str]
            [clojure.tools.cli :refer [parse-opts]]
            [cljs-node-io.core :as io :refer [slurp spit]]
            [xml-js]
            [net-tool.depends :as d]))

(defn parse-xml [xml]
  (.xml2js xml-js xml #js{:compact true}))

(defn isvirsh? []
  (boolean
    (re-find #"virsh"
             (->
               (d/spawn "which virsh")
               (js->clj :keywordize-keys true)
               :stdout
               .toString))))

(defn running-domains []
  (->
    (d/spawn "virsh list --name --state-running")
    (js->clj :keywordize-keys true)
    :stdout
    .toString
    (str/split #"\n")))


(defn dump-xml [domain]
  (let [xml (->
              (d/spawn (str "virsh dumpxml " domain))
              (js->clj :keywordize-keys true)
              :stdout
              .toString)]
    (js->clj (parse-xml xml) :keywordize-keys true)))


(defn dump-xml-mock []
  (js->clj (parse-xml (slurp "resources/dumpxml.txt")) :keywordize-keys true))


(defn parse-dump-xml [v]
  (mapv (fn [m]
          (let [result {:type        (get-in m [:_attributes :type])
                        :mac         (get-in m [:mac :_attributes :address])
                        :bridge      (get-in m [:source :_attributes :bridge])
                        :hostdev     (get-in m [:target :_attributes :dev])
                        :guestdriver (get-in m [:model :_attributes :type])
                        :alias       (get-in m [:alias :_attributes :name])
                        :pciaddress  {:domain   (get-in m [:address :_attributes :domain])
                                      :bus      (get-in m [:address :_attributes :bus])
                                      :slot     (get-in m [:address :_attributes :slot])
                                      :function (get-in m [:address :_attributes :function])}}]

            result))
        v))

(defn interfaces []
  (->> (dump-xml "director") :domain :devices :interface))

(def mock-interfaces (->> (dump-xml-mock) :domain :devices :interface))

(defn process-vms [options]
  (let [interfaces (if (:mock options) (parse-dump-xml mock-interfaces) (parse-dump-xml (running-domains)))]
    (pprint/print-table interfaces)))

(def cli-options
  ; An option with a required argument
  [["-p" "--port PORT" "Port number"
    :default 80
    :parse-fn #(js/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
  ;; A non-idempotent option
   ["-v" nil "Verbosity level"
    :id :verbosity
    :default 0
    :assoc-fn (fn [m k _] (update-in m [k] inc))]
   ["-m" "--mock" "Mock Output"
    :id :mock
    :default false]
   ["-h" "--help" "Help"]])


(defn usage [options-summary]
  (->> ["Usage: net-tool [options] action"
        ""
        "Options:"
        options-summary
        ""
        "Actions:"
        "  vm       List VM Network Configuration"
        "  bond     Print bond interfaces"
        "  status   Print a server's status"
        ""
        "Please refer to the manual page for more information."]
       (str/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (str/join \newline errors)))

(defn validate-args
  "Validate command line arguments. Either return a map indicating the program
  should exit (with a error message, and optional ok status), or a map
  indicating the action the program should take and the options provided."
  [args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) {:exit-message (usage summary) :ok? true}
      errors {:exit-message (error-msg errors)}
      (and (= 1 (count arguments))
           (#{"vm" "bond" "status"} (first arguments)))
      {:action (first arguments) :options options}
      :else {:exit-message (usage summary)})))

(defn exit [status msg]
  (println msg))

(defn main [& args]
  (let [{:keys [action options exit-message ok?]} (validate-args args)]
    (if exit-message
      (exit (if ok? 0 1) exit-message)
      (case action
        "vm" (process-vms options)
        "bond" (pprint/pprint (isvirsh?))
        "status" (println "Status")))))


;(set! *main-cli-fn* -main)
