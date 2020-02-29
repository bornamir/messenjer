var query = window.location.search;
var urlParams = new URLSearchParams(query);
var userReceiver = urlParams.get("user");

function getAllMessages() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == XMLHttpRequest.DONE && xmlhttp.status == 200) {
            var jsonMsgs = JSON.parse(this.responseText);
            jsonMsgs.forEach(addMsg);
        }
    };
    xmlhttp.open("GET", "updateMessage?all=1&user=" + userReceiver);
    xmlhttp.send();
}

getAllMessages();


function sendMessasge() {

    var sendMessage = document.getElementById('message_input').value;
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == XMLHttpRequest.DONE) {
            if (this.status === 200) {
                console.log(this.responseText);
                var jsonObj = JSON.parse(this.responseText);
                addMsg(jsonObj);
                document.getElementById('message_input').value = "";
            } else if (this.status === 202) {
                addMsg(JSON.parse(this.responseText))
                document.getElementById("notif").innerHTML =
                    "User cannot receive your message at this moment";
                document.getElementById('message_input').value = "";
            } else {
                console.log(xmlhttp.status + ' ' + xmlhttp.responseText);
            }
        }
    };
    xmlhttp.open("POST", "updateMessage", true);
    xmlhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xmlhttp.send("message=" + sendMessage + "&user=" + userReceiver + "&type=text");

}


var eventSource = new EventSource("updateMessage?user=" + userReceiver);
eventSource.onopen = function (e) {
    console.log("Connection Opened");
};
// eventSource.onmessage = function (e) {
//     var jsonObj = JSON.parse(e.data);
//
//     console.log("Message: " + jsonObj.message + " " + jsonObj.created_date);
// };
eventSource.onerror = function (e) {
    console.log("Error" + e.data);
};

eventSource.addEventListener('textMessage', function (e) {
    var jsonMsg = JSON.parse(e.data);
    console.log(jsonMsg);
    addMsg(jsonMsg);
});

function addMsg(jsonMsg) {
    var msg_history = document.getElementById("msg_history");
    var el = document.createElement("div");
    var inner = '';
    if (jsonMsg.senderUsername === userReceiver) {
        el.classList.add("incoming_msg");
        inner = '    <div class="incoming_msg_img">' +
            '<img src="https://ptetutorials.com/images/user-profile.png" alt="' +
            jsonMsg.senderUsername + '"></div>' +
            '<div class="received_msg">' +
            '<div class="received_withd_msg"><p>' +
            jsonMsg.message + '</p>' +
            '<span class="time_date"> ' +
            jsonMsg.time +
            '    |    ' +
            jsonMsg.date + '</span></div></div></div>';

    } else {
        inner = '    <div class="sent_msg">' +
            '    <p>' +
            jsonMsg.message +
            '</p>' +
            '<span class="time_date"> ' +
            jsonMsg.time +
            '    |    ' +
            jsonMsg.date + '</span></div>';
        el.classList.add("outgoing_msg");
    }
    el.innerHTML = inner;
    msg_history.appendChild(el);
    msg_history.scrollTop = msg_history.scrollHeight;
}

// function addOutgoingMsg(jsonMsg) {
//     var msg_history = document.getElementById("msg_history");
//     var el = document.createElement("div");
//     var inner = '    <div class="sent_msg">\n' +
//         '    <p>Test which is a new approach to have all\n' +
//         'solutions</p>\n' +
//         '<span class="time_date"> 11:01 AM    |    June 9</span></div>'
//
//
//     el.innerHTML = inner;
//     msg_history.appendChild(el);
// }

// var messagesWaiting = false;
//
// function getMessages() {
//     if (!messagesWaiting) {
//         messagesWaiting = true;
//         var xmlhttp = new XMLHttpRequest();
//         xmlhttp.onreadystatechange = function () {
//
//             if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
//                 messagesWaiting = false;
//                 document.getElementById("notif").innerHTML =
//                     "A message is received" + this.responseText;
//             }
//         };
//         xmlhttp.open("GET", "updateMessage?user="+ userReceiver, true);
//         xmlhttp.send();
//     }
// }
// setInterval(getMessages, 1000);