var messagesWaiting = false;

function getMessages() {
    if (!messagesWaiting) {
        messagesWaiting = true;
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                messagesWaiting = false;
                // var contentElement = document.getElementById("onlineUsersDiv");
                // contentElement.innerHTML = xmlhttp.responseText;
                var jsonObj = JSON.parse(this.responseText);
                var out = "";
                var i;
                var onlineUsers = jsonObj.onlineUsers;
                var users = jsonObj.users;
                console.log(jsonObj);
                out += "<div id=\"onlineUsersList\"><p><b> Online users are :</b></p>"
                for (i = 0; i < onlineUsers.length; i++) {
                    out += " <p> " + onlineUsers[i] + "</p>";
                }
                out += "</div> <div id=\"allUsers\"><p><b> All users:</b></p>"
                for (i = 0; i < users.length; i++) {
                    out += " <p> " + users[i] + "</p>";
                }
                out += "</div> "

                document.getElementById("onlineUsersDiv").innerHTML = out;

            }
        };
        if (document.getElementById("onlineUsersList")) {
            xmlhttp.open("GET", "onlineUsers?updated=1", true);
            xmlhttp.send();
        } else {
            messagesWaiting = false;
            xmlhttp.open("GET", "onlineUsers");
            xmlhttp.send();
        }
    }
}

// function changeColor() {


setInterval(getMessages, 1000);
// setInterval(changeColor, 1000);
