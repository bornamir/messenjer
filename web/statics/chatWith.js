function sendMessasge() {
    var query = window.location.search;
    var urlParams = new URLSearchParams(query);

    var userReceiver = urlParams.get("user")
    var sendMessage = document.getElementById('message_input').value;
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == XMLHttpRequest.DONE && xmlhttp.status == 200) {
            document.getElementById("notif").innerHTML =
                "Message has been sent";
        } else if (this.readyState == XMLHttpRequest.DONE && xmlhttp.status == 200) {
            document.getElementById("notif").innerHTML =
                "User is not online now. S/He will receive your message next time logged in";
        }
    };
    xmlhttp.open("POST", "updateMessage", true);
    xmlhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xmlhttp.send("message=" + sendMessage + "&user=" + userReceiver);

}

var messageWaiting = false;

function getMessages() {
    if (!messagesWaiting) {
        messagesWaiting = true;
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function () {

            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                messagesWaiting = false;
                document.getElementById("notif").innerHTML =
                    "A message is received" + this.responseText;
            }
        };

    } else {
        xmlhttp.open("GET", "updateMessage", true);
        xmlhttp.send();
    }
}


setInterval(getMessages, 1000);