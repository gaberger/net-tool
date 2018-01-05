(ns net-tool.parser
   (:require [instaparse.core :as insta :refer-macros [defparser]]
             [net-tool.depends :refer [spawn]]))

(def bond-path "/proc/net/bonding/")

; (defparser w
;                "BOND = DRIVER | MODE | Epsilon
;                 DRIVER = sentence ' Bonding Driver: ' DRIVER_L
;                 DRIVER_L = sentence <newline>
;                 MODE = 'ABC '
;                 <sentence> = token (<whitespace> token)*
;                 whitespace = #'\\s+'
;                 symbol = #'[.]'
;                 <numchar> = number word | word number
;                 <token> = word | numchar
;                 <word> = #'[a-zA-Z()\\.,]+'
;                 <newline> = #'\\n' | #'\\r\\n'
;                 <number> = #'[0-9]+'"
;            :string-ci true)



(defparser x
           "BOND = header w lacp w aggregator w slave w details
            <w> = <#'\\s+'>
            header = L1 w L2 w L3 w L4 w L5 w L6 w L7
            L1 = 'Ethernet Channel Bonding Driver: v3.7.1 (April 27, 2011)'
            L2 = 'Bonding Mode: IEEE 802.3ad Dynamic link aggregation'
            L3 = 'Transmit Hash Policy: layer3+4 (1)'
            L4 = 'MII Status: up'
            L5 = 'MII Polling Interval (ms): 100'
            L6 = 'Up Delay (ms): 0'
            L7 = 'Down Delay (ms): 0'
            lacp = M1 w M2 w M3 w M4 w M5 w M6
            M1  = '802.3ad info'
            M2  = 'LACP rate: fast'
            M3  = 'Min links: 0'
            M4  = 'Aggregator selection policy (ad_select): stable'
            M5  = 'System priority: 65535'
            M6  = 'System MAC address: 24:6e:96:8c:a6:06'
            aggregator = N1 w N2 w N3 w N4 w N5 w N6
            N1  = 'Active Aggregator Info:'
            N2  = 'Aggregator ID: 1'
            N3  = 'Number of ports: 1'
            N4 = 'Actor Key: 13'
            N5 = 'Partner Key: 19'
            N6 = 'Partner Mac Address: 4c:76:25:e8:67:40'
            slave = S1 w S2 w S3 w S4 w S5 w S6 w S7 w S8 w S9 w S10 w S11 w S12
            S1 = 'Slave Interface: em2'
            S2 = 'MII Status: up'
            S3 = 'Speed: 10000 Mbps'
            S4 = 'Duplex: full'
            S5 = 'Link Failure Count: 0'
            S6 = 'Permanent HW addr: 24:6e:96:8c:a6:06'
            S7 = 'Slave queue ID: 0'
            S8 = 'Aggregator ID: 1'
            S9 = 'Actor Churn State: none'
            S10 = 'Partner Churn State: none'
            S11 = 'Actor Churned Count: 1'
            S12 = 'Partner Churned Count: 1'
            details = D1 w D2 w D3 w D4 w D5 w D6 w D7 w D8 w D9 w D10 w  D11 w D12 w D13 w D14
            D1 = 'details actor lacp pdu:'
            D2 = 'system priority: 65535'
            D3 = 'system mac address: 24:6e:96:8c:a6:06'
            D4 = 'port key: 13'
            D5 = 'port priority: 255'
            D6 = 'port number: 1'
            D7 = 'port state: 63'
            D8 = 'details partner lacp pdu:'
            D9  = 'system priority: 32768'
            D10 = 'system mac address: 4c:76:25:e8:67:40'
            D11 = 'oper key: 19'
            D12 = 'port priority: 32768'
            D13 = 'port number: 4271'
            D14 = 'port state: 63'
            "
           :string-ci true)

; (def transform-options {:lvalue keyword
;                         :section-terms (partial apply str)})


(def template "Ethernet Channel Bonding Driver: v3.7.1 (April 27, 2011)
 Bonding Mode: IEEE 802.3ad Dynamic link aggregation
 Transmit Hash Policy: layer3+4 (1)
 MII Status: up
 MII Polling Interval (ms): 100
 Up Delay (ms): 0
 Down Delay (ms): 0
 802.3ad info
 LACP rate: fast
 Min links: 0
 Aggregator selection policy (ad_select): stable
 System priority: 65535
 System MAC address: 24:6e:96:8c:a6:06
 Active Aggregator Info:
   Aggregator ID: 1
   Number of ports: 1
   Actor Key: 13
   Partner Key: 19
   Partner Mac Address: 4c:76:25:e8:67:40
 Slave Interface: em2
 MII Status: up
 Speed: 10000 Mbps
 Duplex: full
 Link Failure Count: 0
 Permanent HW addr: 24:6e:96:8c:a6:06
 Slave queue ID: 0
 Aggregator ID: 1
 Actor Churn State: none
 Partner Churn State: none
 Actor Churned Count: 1
 Partner Churned Count: 1
 details actor lacp pdu:
     system priority: 65535
     system mac address: 24:6e:96:8c:a6:06
     port key: 13
     port priority: 255
     port number: 1
     port state: 63
 details partner lacp pdu:
     system priority: 32768
     system mac address: 4c:76:25:e8:67:40
     oper key: 19
     port priority: 32768
     port number: 4271
     port state: 63")
