.PHONY: release dev build-exe compile clean


dev:
	shadow-cljs watch app

release:
	 shadow-cljs release app

build-exe:
	nexe -i target/main.js -r resource/* -o target/net-tool

check:
	shadow-cljs check app

debug: 
	shadow-cljs compile app --debug

compile: 
	shadow-cljs compile app 

clean:
	lein clean