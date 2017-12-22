(require '[cljs.build.api :as b])

(b/watch "src"
  {:main 'lumo-test-1.core
   :output-to "out/lumo_test_1.js"
   :output-dir "out"})
