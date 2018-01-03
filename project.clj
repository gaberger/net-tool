(defproject lumo-test-1 "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [instaparse-lumo "1.4.7"]
                 [org.apache.ant/ant-launcher "1.9.4"]
                 [com.fasterxml.jackson.core/jackson-core "2.8.7"]
                 [cljs-node-io "0.5.0"]
                 [cljsjs/nodejs-externs "1.0.4-1"]]
  :jvm-opts ^:replace ["-Xmx1g" "-server"]
  :npm {:dependencies [[source-map-support "0.4.0"]]}
  :source-paths ["src" "target/classes"]
  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-npm "0.6.2"]]
  :clean-targets ["out" "release"]
  :target-path "target"
  :cljsbuild {:builds
              [{:id "dev" 
               :source-paths ["src"]
               :compiler {:output-to "package/index.js"
                          :main net-tool.core
                          :target :nodejs
                          :install-deps true 
                          :optimizations :none
                          :npm-deps {:shelljs "0.7.8"
                                     :fast-xml-parser "2.8.3"
                                     :xml-js "1.6.1"}}}
              {:id "prod"
                  :source-paths ["src"]
                  :compiler {:main net-tool.core
                             :output-to "package/index.js"
                             :target :nodejs
                             :output-dir "target"
                             :install-deps true 
                             ;; :externs ["externs.js"]
                             :optimizations :simple
                             :pretty-print true
                             :parallel-build true
                             :npm-deps {:shelljs "0.7.8"
                                        :fast-xml-parser "2.8.3"
                                        :xml-js "1.6.1"}}}]
                                        })
