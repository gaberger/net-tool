(defproject lumo-test-1 "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.946"]]
  :jvm-opts ^:replace ["-Xmx1g" "-server"]
  :npm {:dependencies [[source-map-support "0.4.0"]]}
  :source-paths ["src" "target/classes"]
  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-npm "0.6.2"]]
  :clean-targets ["out" "release"]
  :target-path "target"
  :cljsbuild {:builds
            {:dev {:source-paths ["src"]
                   :compiler {:output-to "out/main.js"
                              :main lumo-test-1.main
                              :target :nodejs
                              ;; :npm-deps causes to treat node
                              ;; modules as proper name spaces
                              :npm-deps {:shelljs "0.7.8"}
                                         :fast-xml-parser "2.8.3"}}}})
