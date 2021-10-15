/**
 *
 */
function login(submit) {
    let user = document.getElementById("login-user")
    let pass = document.getElementById("login-pass")

    postData("/login", {"username": user.value, "password": pass.value}, function (status, json, ok) {
        if (json.type !== "ERROR") {
            window.location = '/control'
        } else {
            let banner = document.getElementById("login-banner")
            console.log(json.error.message)
            banner.innerText = json.error.message
        }
    })
}

/**
 *
 */
function createAccount() {
    let user = document.getElementById("new-user")
    let pass = document.getElementById("new-pass")
    let repass = document.getElementById("new-pass")
    if (pass.value !== repass.value) {
        let banner = document.getElementById("create-banner")
        banner.innerText = "Passwords do not match"
        return
    }
    
    postData("/createaccount", {"username": user.value, "password": pass.value}, function (status, json, ok) {
        if (json.type !== "ERROR") {
            window.location = '/control'
        } else {
            let banner = document.getElementById("create-banner")
            console.log(json.error.message)
            banner.innerText = json.error.message
        }
    })
}
