let ws;
class SearchList {
    constructor(list) {
        this.list = list;
    }
    getList(prefix) {
        return this.list.filter(name => name.startsWith(prefix));
    }
};
const getHome = (ele) => {
    window.location = URL + `/user/home?userName=${userName}&session=${session}`;
};
const showBuffer = () => {
    $(".buffer").css("display", "flex");
};
const hideBuffer = () => {
    $(".buffer").css("display", "none");
};
const showResponseMessage = (msg, color) => {
    $(".response-message > div > h2").text(msg);
    $(".response-message > div").css("color", color);
    $(".response-message").css("display", "flex");
};
const hideResponseMessage = (ele) => {
    $(".response-message").css("display", "none");
};
const stopRingPhone = (ele) => {
    ele.style.animation = "none";
    ele.setAttribute("sendRtcOffer", "no")
}
const ringPhone = (ele) => {
    ele.style["animation"] = "shake 0.5s";
    ele.style["animation-iteration-count"] = "infinite";
    ele.setAttribute("sendRtcOffer", "yes")
};
const beforeSend = (xhr) => {
    showBuffer();
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-type", "application/json");
};
const onComplete = (data) => {hideBuffer();}

const openWsSession = () => {
    ws = getWs();
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
    console.log(msg);
    msg = JSON.parse(msg);
    switch(msg.type) {
        case("user"):
            handleUserMessage(msg);
            break;
        case("chat-message"):
            handleChatMessage(msg);
            break;
        case("rtcpc"):
            handleRtcpcMessage(msg);
            break;
    }
};
const handleRtcpcMessage = (msg) => {
    switch(msg.subtype) {
        case("notify-peer"):
            recvPeerNote(msg);
            break;
        case("offer"):
            peerConnection.recvRtcOffer(msg);
            break;
        case("answer"):
            peerConnection.recvRtcAnswer(msg);
            break;
        case("icecandidate"):
            peerConnection.recvRtcIcecandidate(msg);
            break;
    }
};
const recvPeerNote = (msg) => {
    console.log("peerNote");
    console.log(msg);
    let sender = msg.sender;
    let payload = JSON.parse(msg.payload);
    console.log(payload);
    switch(payload.peerType) {
        case("friend"):
            if(payload.state === "ring") {
                ringPhone(document.querySelector(`.friends-list > .list > div[connection=${sender}] > .fa-phone`));
            } else {
                stopRingPhone(document.querySelector(`.friends-list > .list > div[connection=${sender}] > .fa-phone`));
            }
            break;
        case("group"):
            if(payload.state === "ring") {
                ringPhone(document.querySelector(`.groups-list > .list > div[connection=${sender}] > .fa-phone`));
            } else {
                stopRingPhone(document.querySelector(`.groups-list > .list > div[connection=${sender}] > .fa-phone`));
            }
            break;
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