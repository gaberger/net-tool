;  (ns net-tool.parser
;   (require [instaparse-cljs.core :as insta]))


; (def bonding-driver
;   (insta/parser
;      "ROOT = MODE
;       MODE = 'Bonding Mode: ' #'.*'
;       " :string-ci true
;         :input-format :ebnf))

; (def words-and-numbers
;           (insta/parser
;             "BOND = sentence*
;              DRIVER = 'Ethernet Channel Bonding Driver: ' DRIVER_L
;              DRIVER_L = sentence
;              sentence = token (<whitespace> token)*
;              <token> = word | number
;              whitespace = #'\\s+'
;              word = #'[a-zA-Z]+'
;              number = #'[0-9]+'"))

; (def s 
;   "
; Ethernet Channel Bonding Driver: v3.7.1 (April 27, 2011)

; Bonding Mode: IEEE 802.3ad Dynamic link aggregation
; ")


; Transmit Hash Policy: layer3+4 (1)
; MII Status: up
; MII Polling Interval (ms): 100
; Up Delay (ms): 0
; Down Delay (ms): 0

; 802.3ad info
; LACP rate: fast
; Min links: 0
; Aggregator selection policy (ad_select): stable
; System priority: 65535
; System MAC address: 24:6e:96:8c:a6:06
; Active Aggregator Info:
;   Aggregator ID: 1
;   Number of ports: 1
;   Actor Key: 13
;   Partner Key: 19
;   Partner Mac Address: 4c:76:25:e8:67:40

; Slave Interface: em2
; MII Status: up
; Speed: 10000 Mbps
; Duplex: full
; Link Failure Count: 0
; Permanent HW addr: 24:6e:96:8c:a6:06
; Slave queue ID: 0
; Aggregator ID: 1
; Actor Churn State: none
; Partner Churn State: none
; Actor Churned Count: 1
; Partner Churned Count: 1
; details actor lacp pdu:
;     system priority: 65535
;     system mac address: 24:6e:96:8c:a6:06
;     port key: 13
;     port priority: 255
;     port number: 1
;     port state: 63
; details partner lacp pdu:
;     system priority: 32768
;     system mac address: 4c:76:25:e8:67:40
;     oper key: 19
;     port priority: 32768
;     port number: 4271
;     port state: 63