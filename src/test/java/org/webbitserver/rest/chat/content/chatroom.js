// EventSource reference.
var es;
var xhr = new XMLHttpRequest();

// Log text to main window.
function logText(msg) {
    var textArea = document.getElementById('chatlog');
    textArea.value = textArea.value + msg + '\n';
    textArea.scrollTop = textArea.scrollHeight; // scroll into view
}

// Perform login: Ask user for name, and send message to socket.
function login() {
    var defaultUsername = (window.localStorage && window.localStorage.username) || 'yourname';
    var username = prompt('Choose a username', defaultUsername);
    if (username) {
        if (window.localStorage) { // store in browser localStorage, so we remember next next
            window.localStorage.username = username;
        }
        xhr.open("POST", "sessions", true);
        xhr.send(username);
        xhr.onerror = function(e) {
            logText('* Error:'+ e);
        };
        xhr.onreadystatechange = function() {
            if(xhr.readyState == 4 && xhr.status == 200) {
                connect();
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

    // connect to eventsource
    logText('* Connecting...');
    es = new EventSource('message-publisher');
    es.onopen = function(e) {
        logText('* Connected!');
        document.getElementById('entry').focus();
    };
    es.onerror = function(e) {
        logText('* Unexpected error');
    };
    es.onmessage = function(e) {
        onMessage(JSON.parse(e.data));
    };

    // wire up text input event
    var entry = document.getElementById('entry');
    entry.onkeypress = function(e) {
        if (e.keyCode == 13) { // enter key pressed
            var text = entry.value;
            if (text) {
                xhr.open("POST", "messages", true);
                xhr.send(text);
                xhr.onerror = function(e) {
                    logText('* Error:'+ e);
                }

            }
            entry.value = '';
        }
    };
}

// Connect on load.
window.onload = login;
