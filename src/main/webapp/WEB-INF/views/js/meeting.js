const showDropDown = (e) => {
    $(".user-dropdown").show();
};
const hideDropDown = (e) => {
    $(".user-dropdown").hide();
};
const getWs = () => {
    return new WebSocket(WSURL + `/${userName}/${session}/${connection}/${peerType}`);
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
$(document).ready(() => {
    openWsSession();
});