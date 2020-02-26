var messagesWaiting = false;

function getMessages() {
    if (!messagesWaiting) {
        messagesWaiting = true;
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                messagesWaiting = false;
                var contentElement = document.getElementById("onlineUsersDiv");
                contentElement.innerHTML = xmlhttp.responseText;
            }
        };
        if (document.getElementById("onlineUsersList")) {
            xmlhttp.open("GET", "onlineUsers?updated=1", true);
            xmlhttp.send();
        } else {
            messagesWaiting = false;
            xmlhttp.open("GET","onlineUsers");
            xmlhttp.send();
        }
    }
}

setInterval(getMessages, 1000);
