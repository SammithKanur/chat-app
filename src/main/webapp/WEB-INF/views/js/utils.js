let ws;
const showBuffer = () => {
    let loader = '<div class="buffer"><div class="loader"></div></div>';
    $("body").append(loader);
};
const hideBuffer = () => {
    $(".buffer").remove();
};
const beforeSend = (xhr) => {
    showBuffer();
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-type", "application/json");
};
const onComplete = (data) => {hideBuffer();}

const openWsSession = () => {
    ws = new WebSocket(WSURL + `/${userName}/${session}`);
    ws.onopen = (e) => {
        console.log("websocket connection is open");
    };
    ws.onmessage = (e) => {
        handleWsMessage(e.data);
    };
    ws.onclose = (e) => {
        console.log("client closing");
    };
    ws.onerror = (e) => {
        console.log("client ws error");
    };
};
const handleWsMessage = (msg) => {
    msg = JSON.parse(msg);
    console.log(msg);
    switch(msg.type) {
        case("chat-message"):
            handleChatMessage(msg);
            break;
        case("rtcpc"):
            handleRtcpcMessage(msg);
            break;
    }
};
const handleChatMessage = (msg) => {
    if(state.connectionType === msg.connectionType && state.connection === msg.connection) {
        $(".message-area").append(getMessage([msg.message, msg.sender]));
        resizeTextArea(document.getElementsByClassName("message-area")[0].lastChild.lastChild);
    }
};

const logout = (e) => {
    $.ajax({
        url: URL + "/user/request",
        type: "POST",
        data: JSON.stringify({validUserName:userName, session:session, type:"user", subtype:"logout", userName:userName}),
        beforeSend: beforeSend,
        success: function(data) {
            console.log(data);
        },
        error: function(data) {
            console.log(data);
        },
        complete:onComplete,
    })
    window.location = URL + "/user/login-page";
};