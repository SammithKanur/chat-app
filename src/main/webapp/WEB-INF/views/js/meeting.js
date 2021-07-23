let peerDetails = {};
let peerConnection;
let dataChannel;
let peerStream;
let rtcConfig = {
    iceServers: [
        {
            urls:"turn:sammith-projects.tk:3478",
            username: "sammith",
            credential: "turnserver",
        }
    ]
};
const showDropDown = (e) => {
    $(".user-dropdown").show();
};
const hideDropDown = (e) => {
    $(".user-dropdown").hide();
};
const getWs = () => {
    return new WebSocket(WSURL + `/${userName}/${session}/${connection}/${peerType}`);
};
const getStream = async () => {
    const vid = document.querySelector(".video-streams > .user-stream");
    let width = vid.offsetWidth;
    let height = vid.offsetHeight;
    const constraints = {
        'audio': {'echoCancellation': true},
        'video': {
            'width': {'exact' : width},
            'height': {'exact' : height},
        }
    }
    return await navigator.mediaDevices.getUserMedia(constraints);
};
const createPeer = () => {
    peerConnection = new RTCPeerConnection(rtcConfig);
    peerConnection.onicecandidate = function(event) {
        console.log("sending ice candidate");
        if(event.candidate) {
            ws.send(JSON.stringify({type:"rtcpc", subtype:"icecandidate", sender:userName,
                peer:connection, payload:event.candidate}));
        }
    };
    peerConnection.onconnectionstatechange = function(event) {
        console.log("in connection state change " + event.connectionState);
    };
    peerConnection.ontrack = function(event) {
        peerStream.addTrack(event.track, peerStream);
    };
};
const createRtcOffer = () => {
    peerConnection.createOffer(function(offer) {
        console.log("offer created");
        peerConnection.setLocalDescription(offer);
        ws.send(JSON.stringify({type:"rtcpc", subtype:"offer", sender:userName,
            peer:connection, payload:offer}));
    }, function(err) {
        alert(err);
    });
};
const addStream = async () => {
    const userStream = await getStream();
    userStream.getTracks().forEach(track => {
        peerConnection.addTrack(track, userStream);
    });
    const userVideo = document.querySelector(".video-streams > .user-stream");
    const peerVideo = document.querySelector(".video-streams > .peer-stream");
    peerStream = new MediaStream();
    userVideo.srcObject = userStream;
    peerVideo.srcObject = peerStream;
};
const recvRtcOffer = (msg) => {
    console.log("offer recieved");
    let peer = msg.sender;
    let offer = msg.payload;
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
const recvRtcAnswer = (msg) => {
    console.log("answer recieved");
    let peer = msg.sender;
    let answer = msg.payload;
    peerConnection.setRemoteDescription(new RTCSessionDescription(answer));
    console.log(peerConnection);
};
const recvRtcIcecandidate = (msg) => {
    alert("ice candidate recieved");
    let peer = msg.sender;
    let icecandidate = msg.payload;
    peerConnection.addIceCandidate(new RTCIceCandidate(icecandidate));
};
$(document).ready(async () => {
    openWsSession();
    createPeer();
    await addStream();
    if(sendRtcOffer === "yes") {
        // alert(sendRtcOffer);
        createRtcOffer();
    }
});