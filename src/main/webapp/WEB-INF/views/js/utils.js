let ws;
let peerDetails = {};
let rtcConfig = {};
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
const createPeerConnection = async (peer) => {
    peerDetails[peer] = new RTCPeerConnection(rtcConfig);
    let offer = await peerDetails[peer].createOffer();
    await peerDetails[peer].setLocalDescription(offer);
    ws.send(JSON.stringify({type:"rtcpc", subtype:"offer", peer:peer,
        sender:userName, payload:offer}));
    addRtcEvents(peer);
};
const addRtcEvents = (peer) => {
    let peerConnection = peerDetails[peer];
    peerConnection.addEventListener("connectionstatechange", event => {
        switch(peerConnection.connectionState) {
            case("connected"):
                alert(`${userName} and ${peer} conncted through p2p`);
                break;
            case("disconnected"):
                alert(`${peer} disconnected`);
                break;
        }
    });
    peerConnection.addEventListener("icecandidate", event => {
        console.log("sending ice candidate");
        if(event.candidate) {
            ws.send(JSON.stringify({type:"rtcpc", subtype:"icecandidate", sender:userName,
                peer:peer, payload:event.candidate}));
        }
    });
    peerDetails[peer] = peerConnection;
};
const openWsSession = () => {
    ws = new WebSocket(WSURL + `/${userName}/${session}`);
    ws.onopen = (e) => {
        ws.send("client says hello");
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
    console.log(state);
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
const handleRtcpcMessage = (msg) => {
    switch(msg.subtype) {
        case("offer"):
            recvRtcOffer(msg);
            break;
        case("answer"):
            recvRtcAnswer(msg);
            break;
        case("icecandidate"):
            recvRtcIcecandidate(msg);
            break;
    }
};
const recvRtcOffer = async (msg) => {
    console.log("offer");
    console.log(msg);
    peerConnection = new RTCPeerConnection(rtcConfig);
    peerConnection.setRemoteDescription(new RTCSessionDescription(msg.payload));
    const answer = await peerConnection.createAnswer();
    await peerConnection.setLocalDescription(answer);
    peerDetails[msg.sender] = peerConnection;
    ws.send(JSON.stringify({type:"rtcpc", subtype:"answer",
        peer:msg.sender, sender:userName, payload:answer}));
};
const recvRtcAnswer = async (msg) => {
    console.log("answer");
    console.log(msg);
    await peerDetails[msg.sender].setRemoteDescription(new RTCSessionDescription(msg.payload));
};
const recvRtcIcecandidate = async (msg) => {
    console.log("icecandidate");
    console.log(msg);
    peerDetails[msg.sender].addIceCandidate(msg.payload);
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