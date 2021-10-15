function startSSH() {
    postData('/api/services/startSSH', {}, function (status, json, ok) {
        let banner = document.getElementById("services-banner")
        banner.innerText = ""

        switch (json.type) {
            case "ERROR":
                banner.innerText = datedMessage("Error " + json.error.message)
                break
            case "SERVICE":
                banner.innerText = datedMessage("Service Responded With Exit Code: " + json.exitCode)
                break
            default:
                console.log(json)
        }
    })
}