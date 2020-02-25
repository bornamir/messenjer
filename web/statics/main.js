var messagesWaiting = false;

function getMessages() {
    if (!messagesWaiting) {
        messagesWaiting = true;
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                messagesWaiting = false;
                var contentElement = document.getElementById("content");
                contentElement.innerHTML = xmlhttp.responseText;
            }
        };
        xmlhttp.open("GET", "onlineUsers?t=" + new Date(), true);
        xmlhttp.send();
    }
}

setInterval(getMessages, 1000);
