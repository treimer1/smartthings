/*
 *  DSC Zone Device
 *
 *  Author: Matt Martz <matt.martz@gmail.com>
 *  Date: 2014-04-28
 *  Modified to be a motion device: Kent Holloway <drizit@gmail.com>
 *  Date: 2014-05-24
 */

// for the UI
metadata {
  definition (name: "DSC Zone Motion", namespace: "treimer1", author: "matt.martz@gmail.com") {
    // Change or define capabilities here as needed
    capability "Refresh"
    capability "Motion Sensor"
    capability "Sensor"
    capability "Polling"

    // Add commands as needed
    command "zone"
  }

  simulator {
    // Nothing here, you could put some testing stuff here if you like
  }

  tiles {
    // Main Row
    standardTile("zone", "device.motion", width: 2, height: 2, canChangeBackground: true, canChangeIcon: true) {
      state("active",   label:'motion',    icon:"st.motion.motion.active",   backgroundColor:"#53a7c0")
      state("inactive", label:'no motion', icon:"st.motion.motion.inactive", backgroundColor:"#ffffff")
      state("alarm",    label:'ALARM',     icon:"st.motion.motion.active",   backgroundColor:"#ff0000")
    }

    // This tile will be the tile that is displayed on the Hub page.
    main "zone"

    // These tiles will be displayed when clicked on the device, in the order listed here.
    details(["zone"])
  }
}

// handle commands
def zone(String state) {
  // state will be a valid state for a zone (open, closed)
  // zone will be a number for the zone
  log.debug "Zone: ${state}"

  // Since this is a motion sensor device we need to convert open to active and closed to inactive
  // before sending the event
  def eventMap = [
   'open':"active",
   'closed':"inactive",
   'alarm':"alarm"
  ]
  def newState = eventMap."${state}"
  // Send final event
  sendEvent (name: "motion", value: "${newState}")
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

