function turnOff() {
    let banner = document.getElementById("monitor-banner")
    banner.innerText = ""

    postData('/api/monitor/turnoff', {}, function (status, json, ok) {
        if (json.type !== "ERROR") {
            banner.innerText = datedMessage("monitor manually turned off")
        } else {
            banner.innerText = datedMessage(json.error.message)
        }
    })
}

/**
 * @param state {Element}
 */
function keepOff(state) {
    let banner = document.getElementById("monitor-banner")
    banner.innerText = ""
    postData('/api/monitor/keepoff', {"state": state.checked}, function (status, json, ok) {
        switch (json.type) {
            case "ERROR":
                banner.innerText = datedMessage("Monitor manually turned off")
                break
            case "MONITOR_STATE":
                banner.innerText = datedMessage("Monitor state changed to: " + json.state.state)
                break
            default:
                console.log(json)
        }
    })
}

let monitor_checkbox = document.getElementById("monitor-keepOff")
monitor_checkbox.checked = (monitor_checkbox.getAttribute("checkedstate") === "true")