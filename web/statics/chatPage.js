var messagesWaiting = false;
var global_users;
var global_onlineUsers;

function getMessages() {
    if (!messagesWaiting) {
        messagesWaiting = true;
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function () {

            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                messagesWaiting = false;
                var jsonObj = JSON.parse(this.responseText);
                var receivedOnlineUsers = jsonObj.onlineUsers;
                var receivedUsers = jsonObj.users;
                global_onlineUsers = receivedOnlineUsers;
                global_users = receivedUsers;
                populate_to_inbox_chat(receivedUsers, receivedOnlineUsers);
            }
        };

        if (document.getElementById("inbox_chat").innerHTML === "") {
            messagesWaiting = false;
            xmlhttp.open("GET", "onlineUsers");
            xmlhttp.send();
        } else {
            xmlhttp.open("GET", "onlineUsers?updated=1", true);
            xmlhttp.send();
        }
    }
}

setInterval(getMessages, 1000);

function populate_to_inbox_chat(users, onlineUsers) {
    var out = "";
    var i;
    for (i = 0; i < users.length; i++) {
        out += "<div class=\"chat_list \"><div class=\"chat_people\"><div class=\"chat_img \">";
        if (onlineUsers.includes(users[i])) {
            out += "<img src=\"statics/user-profile-online.png\" alt=\"online\"></div>";
        } else {
            out += "<img src=\"statics/user-profile-offline.png\" alt=\"offline\"></div>";
        }
        out += "<div class=\"chat_ib\"><h5>  " + users[i] +
            "  <span class='chat_date' onclick=\"location.href='chatWith?user=" +
            users[i] + "';\" style='cursor: pointer;' > Click to chat </span></h5></div></div></div>";
    }
    document.getElementById("inbox_chat").innerHTML = out;

}

function searchUser() {
    var searchUser = document.getElementById('search_input').value;
    if (global_users.find(function (e) {
        return e === searchUser
    }) === undefined) {
        document.getElementById('search_inbox').innerHTML = "<div class=\"chat_ib\"><h5> No user with this username exist </h5></div>";
    } else {
        var out = "";
        out += "<div class=\"chat_list active_chat\"><div class=\"chat_people\"><div class=\"chat_img \">";
        if (global_onlineUsers.includes(searchUser)) {
            out += "<img src=\"statics/user-profile-online.png\" alt=\"online\"></div>";
        } else {
            out += "<img src=\"statics/user-profile-offline.png\" alt=\"offline\"></div>";
        }
        out += "<div class=\"chat_ib\"><h5>  " + searchUser + "  <span class='chat_date' onclick=\"location.href='chatWith?user=" + searchUser + "';\" style='cursor: pointer;' > Click to chat </span></h5></div></div></div>";
        document.getElementById('search_inbox').innerHTML = out;
    }

}