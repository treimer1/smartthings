/*
 *  My Alarm Panel
 *
 *  Author: Ted Reimer <ted.reimer@gmail.com>
 *  Date: 2015-09-30
 */

preferences {
    input("ip", "text", title: "IP", description: "The IP of your alarmserver")
    input("port", "text", title: "Port", description: "The port")
} 

// for the UI
metadata {
  // Automatically generated. Make future change here.
  definition (name: "My Alarm Panel", namespace: "treimer1", author: "Ted Reimer <ted.reimer@gmail.com>") {
    // Change or define capabilities here as needed
    capability "Refresh"
    capability "Polling"
    capability "Switch"
    capability "Alarm"

    attribute "alarmStatus", "string"
    attribute "switchAway", "string"
    attribute "switchStay", "string"
    attribute "panic", "string"
    attribute "systemStatus", "string"
    attribute "response", "string"
        
    // Add commands as needed
    command "partition"
    command "armAway"
    command "armStay"
    command "disarm"
    command "clear"
    command "chimeToggle"
    command "panic"
    command "away"
    command "stay"
  }

  simulator {
    // Nothing here, you could put some testing stuff here if you like
  }

  tiles {
    standardTile("dscpartition", "device.dscpartition", width: 2, height: 2, canChangeBackground: true, canChangeIcon: true) {
      state "armed",     label: 'Armed', action: "disarm", backgroundColor: "#79b821", icon:"st.Home.home3"
      state "armed_away",label: 'Away', action: "disarm", icon: "st.Home.home3", backgroundColor: "#add8e6"
      state "armed_stay",label: 'Stay', action: "disarm", icon: "st.Home.home4", backgroundColor: "#f1d801"
      state "exitdelay", label: 'Exit Delay', action: "disarm", icon: "st.Home.home2", backgroundColor: "#B8B8B8"
      state "entrydelay",label: 'EntryDelay', backgroundColor: "#ff9900", icon:"st.Home.home3"
      state "notready",  label: 'Not Ready',  backgroundColor: "#ffa81e", icon: "st.Home.home2"
      state "ready",     label: 'Ready', action: "armStay", icon: "st.Home.home2", backgroundColor: "#ffffff"
      state "alarm",     label: 'Alarm',      backgroundColor: "#ff0000", icon:"st.Home.home3"
    }
    standardTile("away", "device.awaySwitch", width: 1, height: 1, canChangeIcon: false, canChangeBackground: false) {
      state "on", label: "Away", action: "disarm", icon: "st.Home.home3", backgroundColor: "#add8e6"
      state "off", label: "Away", action: "armAway",icon: "st.Home.home3", backgroundColor: "#ffffff"
                }
    standardTile("stay", "device.staySwitch", width: 1, height: 1, canChangeIcon: false, canChangeBackground: false) {
      state "on", label: "Stay", action: "disarm", icon: "st.Home.home4", backgroundColor: "#f1d801"
      state "off", label: "Stay", action: "armStay",icon: "st.Home.home4", backgroundColor: "#ffffff"
    }
    standardTile("refresh", "device.refresh", inactiveLabel: false, width: 1, height: 1, canChangeIcon: false, canChangeBackground: false) {
      state "default", action:"refresh", icon:"st.secondary.refresh"
    }
    standardTile("clear", "device.disarm", inactiveLabel: false, width: 1, height: 1, canChangeIcon: false, canChangeBackground: false) {
      state "default", label:"Clear", action:"disarm", icon:"st.secondary.refresh"
    }
    standardTile("chime", "device.chime", width:1, height: 1, canChangeIcon: false, canChangeBackground: false) {
      state "chimeOff", label:"Chime", action:"chimeToggle", icon:"st.secondary.off", backgroundColor: "#ffffff"
      state "chimeOn", label:"", action:"chimeToggle", icon:"st.secondary.beep", backgroundColor: "#ffffff"
    }
    standardTile("panic", "device.panic", width: 1, height: 1, canChangeIcon: false, canChangeBackground: true) {
      state "default", label:"Panic", action:"panic", icon:"st.alarm.alarm.alarm", backgroundColor:"#ffffff"
      state "panic", label:"Cancel Panic", action:"clear", icon:"st.alarm.alarm.alarm", backgroundColor:"#ff0000"
    }
    valueTile("systemStatus", "device.systemStatus", inactiveLabel: false,
      decoration: "flat", width: 3, height: 1) {
      state "default", label: "${currentValue}"
    }
    
    main "dscpartition"

    // These tiles will be displayed when clicked on the device, in the order listed here.
    details(["dscpartition","away","stay","panic","clear"])
  }
}

// parse events into attributes
def parse(String description) {
  log.debug "Parsing '${description}'"
  def myValues = description.tokenize()

  log.debug "Event Parse function: ${description}"
  sendEvent (name: "${myValues[0]}", value: "${myValues[1]}")
}

def partition(String state, String partition) {
    // state will be a valid state for the panel (ready, notready, armed, etc)
    // partition will be a partition number, for most users this will always be 1

    log.debug "Partition: ${state} for partition: ${partition}"
    sendEvent (name: "dscpartition", value: "${state}")
}

def poll() {
  log.debug "Executing 'poll'"
  // TODO: handle 'poll' command
  // On poll what should we do? nothing for now..
}

def refresh() {
  log.debug "Executing 'refresh' which is actually poll()"
  poll()
  // TODO: handle 'refresh' command
}

// Implement "switch" (turn alarm on/off)
def on() {
    armStay()
}

def off() {
    disarm()
}

def away() {
    armAway()
}

def stay() {
    armStay()
}

// Commands sent to the device
def armAway() {
    log.debug "Sending arm command"
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/api/alarm/arm",
        headers: [
            HOST: "$ip:$port"
            //HOST: getHostAddress()
        ]
    )
    log.debug "response" : "Request to arm received"
    //log.debug "arm"
//  sendEvent (name: "switch", value: "on")
    sendEvent(name: "awaySwitch", value: "on")
    sendEvent(name: "staySwitch", value: "off")
    sendEvent(name: "switch", value: "on")
    return result
}

def armStay() {
    log.debug "Sending arm command"
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/api/alarm/stayarm",
        headers: [
            HOST: "$ip:$port"
            //HOST: getHostAddress()
        ]
    )
    log.debug "response" : "Request to stayarm received"
    //log.debug "arm"
    sendEvent(name: "awaySwitch", value: "off")
    sendEvent(name: "staySwitch", value: "on")
    sendEvent(name: "switch", value: "on")
    return result
}

def disarm() {
    log.debug "Sending disarm command"
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/api/alarm/disarm",
        headers: [
            HOST: "$ip:$port"
            //HOST: getHostAddress()
        ]
    )
    log.debug "response" : "Request to disarm received"
    //log.debug "disarm"
    sendEvent(name: "awaySwitch", value: "off")
    sendEvent(name: "staySwitch", value: "off")
    sendEvent(name: "switch", value: "off")
    return result
}

def siren() {
    panic()
} 

def both() {
    panic()
}

def strobe() {
}

def chimeToggle() {
    log.debug "Toggling chime"
//    zigbee.smartShield(text: "chimeOn").format()
}

def panic() {
    log.debug "Sending panic command"
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/api/alarm/panic",
        headers: [
            HOST: "$ip:$port"
            //HOST: getHostAddress()
        ]
    )
    log.debug "response" : "Panic request received"
    //log.debug "panic"
    //sendEvent(name: "switch", value: "on")
    return result
}

// TODO: Need to send off, on, off with a few secs in between to stop and clear the alarm
def clear() {
    delayBetween([disarm(), disarm()],500)
}

//def refresh() {
//    update()
//}

def update() {
    refresh()
}

def configure() {
    update()
}
