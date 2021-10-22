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

function powerHandle(status, json, ok) {
    
}