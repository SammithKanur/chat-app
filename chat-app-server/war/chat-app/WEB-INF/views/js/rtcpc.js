let peerDetails = {};
let peerConnection;
let dataChannel;
let rtcConfig = {
    iceServers: [
        {
            urls:"turn:sammith-projects.tk:3478",
            username: "sammith",
            credential: "turnserver",
        }
    ]
};
const getDataChannel = () => {
    dataChannel = peerConnection.createDataChannel("dataChannel", {
        reliable : true
    });

    dataChannel.onerror = function(error) {
        console.log("Error occured on datachannel:", error);
    };

    // when we receive a message from the other peer, printing it on the console
    dataChannel.onmessage = function(event) {
        console.log("message:", event.data);
    };

    dataChannel.onclose = function() {
        console.log("data channel is closed");
    };

    peerConnection.ondatachannel = function (event) {
        dataChannel = event.channel;
    };
};
const createPeerConnection = async (ele) => {
    let peer = ele.getAttribute("connection");
    peerConnection = new RTCPeerConnection(rtcConfig);
    peerConnection.onicecandidate = function(event) {
        alert("sending ice candidate");
        if(event.candidate) {
            ws.send(JSON.stringify({type:"rtcpc", subtype:"icecandidate", sender:userName,
                peer:peer, payload:event.candidate}));
        }
    };
    getDataChannel();
    peerConnection.createOffer(function(offer) {
        peerConnection.setLocalDescription(offer);
        ws.send(JSON.stringify({type:"rtcpc", subtype:"offer", sender:userName, peer:peer, payload:offer}));
    }, function(err) {
        alert(err);
    });
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
    console.log("offer recieved");
    let peer = msg.sender;
    let offer = msg.payload;
    peerConnection = new RTCPeerConnection(rtcConfig);
    peerConnection.onicecandidate = function(event) {
        alert("sending ice candidate");
        if(event.candidate) {
            ws.send(JSON.stringify({type:"rtcpc", subtype:"icecandidate", sender:userName,
                peer:peer, payload:event.candidate}));
        }
    };
    getDataChannel();
    peerConnection.setRemoteDescription(new RTCSessionDescription(offer));
    peerConnection.createAnswer(function (answer) {
        console.log("in answer " + answer);
        peerConnection.setLocalDescription(answer);
        ws.send(JSON.stringify({type:"rtcpc", subtype:"answer", sender:userName, peer:peer, payload:answer}));
    }, function(err) {
        alert(err);
    });
    console.log(peerConnection);
};
const recvRtcAnswer = async (msg) => {
    console.log("answer recieved");
    let peer = msg.sender;
    let answer = msg.payload;
    peerConnection.setRemoteDescription(new RTCSessionDescription(answer));
    console.log(peerConnection);
};
const recvRtcIcecandidate = async (msg) => {
    alert("ice candidate recieved");
    let peer = msg.sender;
    let icecandidate = msg.payload;
    peerConnection.addIceCandidate(new RTCIceCandidate(icecandidate));
};