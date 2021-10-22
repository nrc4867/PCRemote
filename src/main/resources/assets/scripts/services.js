function serviceHandle(status, json, ok) {
    let timestamp = document.getElementById("services_timestamp")
    let exitCode = document.getElementById("services_exitcode")
    let commandOut = document.getElementById("services_output")
    clearText(timestamp)
    clearText(exitCode)
    clearText(commandOut)

    switch (json.type) {
        case "ERROR":
            timestamp.innerText = datedMessage("Error " + json.error.message)
            break
        case "SERVICE":
            timestamp.innerText = "Completed At: " + datestamp()
            exitCode.innerText = "Exit code: " + json["exitCode"]
            commandOut.innerText = json["consoleOutput"]
            unhide(timestamp)
            unhide(exitCode)
            unhide(commandOut)
            break
        default:
            console.log(json)
    }
}

document.getElementById("service_startSSH").onclick = () => postData('/api/services/startSSH', {}, serviceHandle)