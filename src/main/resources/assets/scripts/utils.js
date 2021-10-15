async function postData(url = '', data = {}, handle = function (status, json, ok) {}) {
    // Default options are marked with *
    const response = await fetch(url, {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        mode: 'cors', // no-cors, *cors, same-origin
        cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
        credentials: 'same-origin', // include, *same-origin, omit
        headers: {
            'Content-Type': 'application/json'
            // 'Content-Type': 'application/x-www-form-urlencoded',
        },
        redirect: 'follow', // manual, *follow, error
        referrerPolicy: 'no-referrer', // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
        body: JSON.stringify(data) // body data type must match "Content-Type" header
    }).then((response) => {
        return new Promise((resolve) => response.json()
            .then((json) => resolve({
                status: response.status,
                ok: response.ok,
                json,
            })));
    }).then(({status, json, ok}) => handle(status, json, ok));
    return response
}

/**
 *
 * @param base {String}
 * @param parameters {Map}
 * @returns {string}
 */
function formURL(base = "", parameters = {}) {
    return base + "?" + new URLSearchParams(parameters).toString()
}

function datedMessage(message) {
    return new Date().toDateString() + ": " + message
}