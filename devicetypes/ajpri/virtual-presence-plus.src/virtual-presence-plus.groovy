//Release History
//		1.0 May 20, 2016
//			Initial Release


metadata {
        definition (name: "Virtual Presence Plus", namespace: "ajpri", author: "Austin Pritchett") {
        capability "Switch"
        capability "Refresh"
        capability "Presence Sensor"
		capability "Sensor"
        
		command "arrived"
		command "departed"
    }

	// simulator metadata
	simulator {
	}

	// UI tile definitions
	tiles {
		standardTile("button", "device.switch", width: 1, height: 1, canChangeIcon: false,  canChangeBackground: true) {
			state "off", label: 'Away', action: "switch.on", icon: "st.Kids.kid10", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'Present', action: "switch.off", icon: "st.Kids.kid10", backgroundColor: "#53a7c0", nextState: "off"
		}
		standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
		}
        standardTile("presence", "device.presence", width: 2, height: 2, canChangeBackground: true) {
			state("present", labelIcon:"st.presence.tile.mobile-present", action:"switch.off", backgroundColor:"#53a7c0")
			state("not present", labelIcon:"st.presence.tile.mobile-not-present", action:"switch.on", backgroundColor:"#ffffff")
		}
		main (["presence"])
		details(["presence", "button", "refresh"])
	}
//    tiles {
//		standardTile("presence", "device.presence", width: 2, height: 2, canChangeBackground: true) {
//			state("not present", label:'not present', icon:"st.presence.tile.not-present", backgroundColor:"#ffffff", action:"arrived")
//			state("present", label:'present', icon:"st.presence.tile.present", backgroundColor:"#53a7c0", action:"departed")
//		}
//		main "presence"
//		details "presence"
//	}
}

def parse(String description) {
	def pair = description.split(":")
	createEvent(name: pair[0].trim(), value: pair[1].trim())
}

// handle commands
def arrived() {
	on()
}


def departed() {
    off()
}

def on() {
	sendEvent(name: "switch", value: "on")
    sendEvent(name: "presence", value: "present")

}

def off() {
	sendEvent(name: "switch", value: "off")
    sendEvent(name: "presence", value: "not present")

}