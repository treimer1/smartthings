/**
 *  My X10 Module
 *
 *  Copyright 2015 Ted Reimer
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
 
preferences {
    input("ip", "text", title: "IP", defaultValue: "192.168.0.31", description: "The IP of your mochad web portal")
    input("port", "text", title: "Port", defaultValue: "80", description: "The port")
} 
 
metadata {
	definition (name: "My X10 Module", author: "Ted Reimer") {
		capability "Switch"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
		standardTile("button", "device.switch", width: 1, height: 1, canChangeIcon: true) {
           state "on", label: '${name}', action: "switch.off", icon: "st.Home.home4", backgroundColor: "#79b821"
           state "off", label: '${name}', action: "switch.on", icon: "st.Home.home4", backgroundColor: "#ffffff"
    }
		main "button"
		details(["button"])
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

// handle commands
def on() {
    def deviceId = device.deviceNetworkId
    log.debug "deviceId" : deviceId
    def x10Code = deviceId.stripIndent(4)
    log.debug "x10Code" : x10Code
    
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/x10.php?command=on&target=" + x10Code + "&xdim=100",
        headers: [
            HOST: "$ip:$port"
            //HOST: getHostAddress()
        ]
    )
    log.debug "response" : "Request to turn on received"
    //log.debug "on"
    sendEvent (name: "switch", value: "on")
    return result
}

def off() {
    def deviceId = device.deviceNetworkId
    log.debug "deviceId" : deviceId
    def x10Code = deviceId.stripIndent(4)
    log.debug "x10Code" : x10Code
    
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/x10.php?command=off&target=" + x10Code + "&xdim=0",
        headers: [
            HOST: "$ip:$port"
            //HOST: getHostAddress()
        ]
    )
    log.debug "response" : "Request to turn off received"
    //log.debug "off"
    sendEvent (name: "switch", value: "off")
    return result
}

