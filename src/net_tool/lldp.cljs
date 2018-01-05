(ns net-tool.lldp)

(defprotocol LLDP
  (lldp-on [interface])
  (lldp-off [interface]))

(defrecord X710 [interface]
 LLDP
 (lldp-on [interface]
   (let [cmd (str "echo lldp start > " (:interface interface))])

   (str "710 lldp on for port: " (:interface port)))
 (lldp-off [port]
  (str "710 lldp off for port:" port)))

(defrecord ConnectX4LX [port]
 LLDP
 (lldp-on [port]
  (str "510 lldp on for port:" port))
 (lldp-off [port]
  (str "510 lldp off for port:" port)))
