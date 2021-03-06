// EventSource reference.
var inbound;
var outbound = new XMLHttpRequest();

// Log text to main window.
function logText(msg) {
    var textArea = document.getElementById('chatlog');
    textArea.value = textArea.value + msg + '\n';
    textArea.scrollTop = textArea.scrollHeight; // scroll into view
}

// Perform login: Ask user for name, and post request over xhr.
function login() {
    var defaultUsername = (window.localStorage && window.localStorage.username) || 'yourname';
    var username = prompt('Choose a username', defaultUsername);
    if (username) {
        if (window.localStorage) { // store in browser localStorage, so we remember next next
            window.localStorage.username = username;
        }
        outbound.open("POST", "sessions", true);
        outbound.send(username);
        outbound.onerror = function(e) {
            logText('* Error:' + e);
        };
        outbound.onreadystatechange = function() {
            if (outbound.readyState == 4 && outbound.status == 200) {
                connect();
                document.getElementById('entry').focus();
            }
        };
    }
}

function onMessage(incoming) {
    switch (incoming.action) {
        case 'JOIN':
            logText("* User '" + incoming.username + "' joined.");
            break;
        case 'LEAVE':
            logText("* User '" + incoming.username + "' left.");
            break;
        case 'SAY':
            logText("[" + incoming.username + "] " + incoming.message);
            break;
    }
}

// Connect to eventsource and setup events.
function connect() {
    // clear out any cached content
    document.getElementById('chatlog').value = '';

    if (window.EventSource) {
        setTimeout(function() {
            logText('* Connecting with EventSource...');
            inbound = new EventSource('message-publisher');
            inbound.onopen = function(e) {
                logText('* Connected!');
            };
            inbound.onerror = function(e) {
                console.log(e);
                logText('* Unexpected error');
            };
            inbound.onmessage = function(e) {
                onMessage(JSON.parse(e.data));
            };

            // wire up text input event
            var entry = document.getElementById('entry');
            entry.onkeypress = function(e) {
                if (e.keyCode == 13) { // enter key pressed
                    var text = entry.value;
                    if (text) {
                        outbound.open("POST", "messages", true);
                        outbound.send(text);
                        outbound.onerror = function(e) {
                            logText('* Error:' + e);
                        }

                    }
                    entry.value = '';
                }
            };
        }, 0);
    } else {
        logText('* No EventSource support in your browser :-(');
    }
}

// Connect on load.
window.onload = login;
