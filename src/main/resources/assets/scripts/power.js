function powerHandle(status, json, ok) {
    let timestamp = document.getElementById("shutdown_timestamp")
    let exitCode = document.getElementById("shutdown_exitcode")
    let commandOut = document.getElementById("shutdown_output")
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

document.getElementById("shutdown_lock").onclick = function () {
    postData("/api/power/lock", {}, (status, json, ok) => powerHandle(status, json, ok))
}

document.getElementById("shutdown_logout").onclick = function () {
    postData("/api/power/logout", {}, (status, json, ok) => powerHandle(status, json, ok))
}

document.getElementById("shutdown_restart").onclick = function () {
    postData("/api/power/restart", {}, (status, json, ok) => powerHandle(status, json, ok))
}

document.getElementById("shutdown_shutdown").onclick = function () {
    postData("/api/power/shutdown", {}, (status, json, ok) => powerHandle(status, json, ok))
}