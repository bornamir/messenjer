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
    xmlhttp.open("GET", "updateMessage?all=1&receiver=" + userReceiver);
    xmlhttp.send();
}

getAllMessages();

function send() {
    if (fileInput.value === "") { // a text message is being sent
        sendTextMessage();
    } else { // a file is trying to be uploaded
        var file = fileInput.files[0];

        var formData = new FormData();
        formData.append("receiver", userReceiver);
        formData.append("message", file.name);
        formData.append("type", "file");
        formData.append("file", file);
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = getResponseOfPost;
        xmlhttp.open("POST", "upload", true);
        xmlhttp.send(formData);
        unselectFile();
    }
}

function sendTextMessage() {
    var sendMessage = document.getElementById('message_input').value;
    if (sendMessage.trim() === "") {
        document.getElementById('message_input').value = "";
        return;
    }
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = getResponseOfPost;
    xmlhttp.open("POST", "updateMessage", true);
    xmlhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xmlhttp.send("message=" + sendMessage + "&receiver=" + userReceiver + "&type=text");
}

// For geting response of server and adding the message to msg list
function getResponseOfPost() {
    if (this.readyState == XMLHttpRequest.DONE) {
        if (this.status === 200) {
            console.log(this.responseText);
            var jsonObj = JSON.parse(this.responseText);
            addMsg(jsonObj);
            document.getElementById('message_input').value = "";
        } else if (this.status === 202) {
            addMsg(JSON.parse(this.responseText));
            document.getElementById("notif").innerHTML =
                "User cannot receive your message at this moment";
            document.getElementById('message_input').value = "";
        } else {
            console.log(this.status + ' ' + this.responseText);
        }
    }
}

var eventSource = new EventSource("updateMessage?receiver=" + userReceiver);
eventSource.onerror = function (e) {
    console.log("Error");
    console.log(e);
};
eventSource.addEventListener('textMessage', function (e) {
    var jsonMsg = JSON.parse(e.data);
    addMsg(jsonMsg);
});
eventSource.addEventListener("retryTime", function (e) {
    console.log(e.data);
});

function closeSSE() {
    console.log("closing");
    eventSource.close();
}

window.onbeforeunload = closeSSE;

function addMsg(jsonMsg) {
    var msg_history = document.getElementById("msg_history");
    var el = document.createElement("div");
    var inner;
    var fileIcon = '';
    if (jsonMsg.type === "file") {
        fileIcon = "<i class=\"fa fa-download \"  aria-hidden=\"true\" " +
            "onclick='getDownload(this.parentElement.innerText.trim())' style='cursor: pointer'></i>";
    }
    if (jsonMsg.senderUsername === userReceiver) {
        el.classList.add("incoming_msg");
        inner = '    <div class="incoming_msg_img">' +
            '<img src="https://ptetutorials.com/images/user-profile.png" alt="' +
            jsonMsg.senderUsername + '"></div>' +
            '<div class="received_msg">' +
            '<div class="received_withd_msg"><p>' +
            jsonMsg.message + " " + fileIcon + '</p>' +
            '<span class="time_date"> ' +
            jsonMsg.time +
            '    |    ' +
            jsonMsg.date + '</span></div></div></div>';

    } else {
        inner = '    <div class="sent_msg">' +
            '<p>' + fileIcon + " " +
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


// handling downloading process -------------

function getDownload(fileName) {
    console.log(fileName);
    // var fileName = this.parentElement.innerText.trim();
    // var postData = new FormData();
    // postData.append('receiver', userReceiver);
    // postData.append("file", this.parentElement.innerText.trim())

    var url = new URL("http://localhost:8080/messenjer/upload");
    url.searchParams.set("receiver", userReceiver);
    url.searchParams.set("file", fileName);
    var xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);
// xhr.responseType = 'blob';
    xhr.onload = function (event) {
        var blob = new Blob([this.response], {type: xhr.getResponseHeader('content-type')});
        var contentDispo = this.getResponseHeader('Content-Disposition');
        // var fileName = contentDispo.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)[1];
        console.log(contentDispo);
        console.log(fileName);
        console.log(blob);
        var a = document.createElement('a');
        a.href = window.URL.createObjectURL(blob);
        a.download = fileName;
        a.dispatchEvent(new MouseEvent('click'));
    }
    xhr.send();
}


// handling sending file process -------------------------

var fileInput = document.createElement("input");
fileInput.setAttribute("type", "file");
fileInput.multiple = false;
fileInput.oninput = selectedFile;

function selectFile() {
    fileInput.click();
    selectedFile();
}

function selectedFile() {
    if (fileInput.files.length > 0) { // if any file is selected
        if (fileInput.files[0].size > 1024 * 1024 * 10) { // if the file size is more than 10 MB, dont let it
            document.getElementById("notif").innerHTML = "File size is more than 10 MB";
            fileInput.value = "";
        } else {
            document.getElementById("notif").innerHTML =
                "This file is selected for uploading: " +
                fileInput.files[0].name +
                " <i onclick='unselectFile()' style='cursor: pointer; color: blue'> Cancel </i> ";
            document.getElementById("message_input").disabled = true;
        }
    } else {
        document.getElementById("message_input").disabled = false;
        document.getElementById("notif").innerHTML = "";
    }
}

function unselectFile() {
    fileInput.value = "";
    selectedFile();
}


// pressing enter key in the input box will send the message
document.getElementById("message_input")
    .addEventListener("keyup", function (event) {
        if (event.keyCode === 13) {
            event.preventDefault();
            send();
        }
    });
;