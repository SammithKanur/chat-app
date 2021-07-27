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
    // let width = vid.offsetWidth;
    // let height = vid.offsetHeight;
    const constraints = {
        'audio': {'echoCancellation': true},
        'video': {
            'width': {'exact' : 100},
            'height': {'exact' : 100},
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
    console.log("ice candidate recieved");
    let peer = msg.sender;
    let icecandidate = msg.payload;
    peerConnection.addIceCandidate(new RTCIceCandidate(icecandidate));
};
const listMediaDevices = () => {
    navigator.mediaDevices.enumerateDevices().then(devices => {
        console.log(devices);
    });
};
class PeerConnection {
    constructor() {
        this.peers = {};
        this.createPeer();
    }
    createPeer = async () => {
        if(peerType === "friend") {
            this.peers[connection] = {mediaStream: new MediaStream(), rtcPeerConnection: new RTCPeerConnection(rtcConfig)};
            this.addEvents(connection);
        } else {
            let members = await this.getGroupMembers();
            console.log(members);
            console.log("in group members");
            members.forEach(member => {
                if(member !== userName) {
                    this.peers[member] = {mediaStream: new MediaStream(), rtcPeerConnection: new RTCPeerConnection(rtcConfig)};
                    this.addEvents(member);
                }
            });
        }
        this.addMediaStream();
    };
    getGroupMembers = async() => {
        let members = [];
        await $.ajax({
            url:URL + "/user/request",
            type:"POST",
            data: JSON.stringify({type:"group", subtype:"group-members", validUserName:userName,
                session:session, groupName:connection}),
            beforeSend: beforeSend,
            success: function(data) {
                console.log(data);
                data["group-members"].forEach(member => {
                    members.push(member.userName);
                });
            },
            error: function(data) {
                console.log(data);
            },
            complete: onComplete,
        });
        return members;
    };
    addEvents = (peer) => {
        this.peers[peer].rtcPeerConnection.onicecandidate = (event) => {
            console.log("sending ice candidate");
            if(event.candidate) {
                ws.send(JSON.stringify({type:"rtcpc", subtype:"icecandidate", sender:userName,
                    peer:peer, payload:event.candidate}));
            }
        };
        this.peers[peer].rtcPeerConnection.ontrack = (event) => {
            this.peers[peer].mediaStream.addTrack(event.track, this.peers[peer].mediaStream);
        };
        this.peers[peer].rtcPeerConnection.onconnectionstatechange = (event) => {
            console.log(event);
        };
    };
    getMemberHtmlElement = (peer) => {
        return `<div class="member"><div>${peer}</div><video id="${peer}-media" autoplay controls></video></div>`;
    };
    getMembers = () => {
        let members = [];
        for(const member in this.peers) {
            members.push(member);
        }
        members = [userName, ...members];
        return members;
    };
    addVideoElements = (members) => {
        if(peerType === "friend") {
            members.forEach(member => {
                $(".video-streams").append(this.getMemberHtmlElement(member));
            });
        } else {
            console.log("adding video elements");
            for(let i = 0; i < members.length; i += 2) {
                $(".video-streams").append('<div class="item"></div>');
                $(".video-streams > .item:last-child").append(this.getMemberHtmlElement(members[i]));
                if(i + 1 < members.length) {
                    $(".video-streams > .item:last-child").append(this.getMemberHtmlElement(members[i + 1]));
                }
            }
        }
    }
    getUserMedia = async () => {
        let width = parseInt($(`#${userName}-media`).outerWidth());
        let height = parseInt($(`#${userName}-media`).outerHeight());
        const constraints = {
            'audio': {'echoCancellation': true},
            'video': {
                'width': {'exact' : width},
                'height': {'exact' : height},
            }
        }
        return await navigator.mediaDevices.getUserMedia(constraints);
    };
    addMembersMedia = async (members) => {
        let membersMedia = await this.getMembersMedia(members);
        for(let i = 0; i < members.length; i++) {
            let mediaElm = `#${members[i]}-media`;
            if(members[i] === userName) {
                document.querySelector(mediaElm).muted = "true";
            } else {
                membersMedia[0].getTracks().forEach(track => {
                    this.peers[members[i]].rtcPeerConnection.addTrack(track, membersMedia[0]);
                });
            }
            document.querySelector(mediaElm).srcObject = membersMedia[i];
        }
    };
    getMembersMedia = async (members) => {
        let membersMedia = [];
        for(let i = 0; i < members.length; i++) {
            if(members[i] === userName) {
                membersMedia.push(await this.getUserMedia());
            } else {
                membersMedia.push(this.peers[members[i]].mediaStream);
            }
        }
        return membersMedia;
    };
    addMembers = async() => {
        let members = this.getMembers();
        this.addVideoElements(members);
        this.addMembersMedia(members);
    };
    addMediaStream = () => {
        if(peerType === "friend") {
            $(".video-streams").attr("peer-type", "friend");
        } else {
            $(".video-streams").attr("peer-type", "group");
        }
        this.addMembers();
    };
    sendRtcOffer = async () => {
        for(const member in this.peers) {
            try {
                console.log(`sending offer to ${member}`);
                const offer = await this.peers[member].rtcPeerConnection.createOffer();
                this.peers[member].rtcPeerConnection.setLocalDescription(offer);
                ws.send(JSON.stringify({
                    type: "rtcpc", subtype: "offer", sender: userName,
                    peer: member, payload: offer
                }));
            } catch(err) {
                console.log(err);
            }
        }
    };
    recvRtcOffer = async (msg) => {
        try{
            console.log("offer recieved");
            let peer = msg.sender;
            let offer = msg.payload;
            this.peers[peer].rtcPeerConnection.setRemoteDescription(new RTCSessionDescription(offer));
            const answer = await this.peers[peer].rtcPeerConnection.createAnswer();
            this.peers[peer].rtcPeerConnection.setLocalDescription(answer);
            ws.send(JSON.stringify({type:"rtcpc", subtype:"answer", sender:userName, peer:peer, payload:answer}));
        } catch(err) {
            console.log(err);
        }
    };
    recvRtcAnswer = (msg) => {
        console.log("answer recieved");
        let peer = msg.sender;
        let answer = msg.payload;
        this.peers[peer].rtcPeerConnection.setRemoteDescription(new RTCSessionDescription(answer));
        console.log(peerConnection);
    };
    recvRtcIcecandidate = (msg) => {
        console.log("ice candidate recieved");
        let peer = msg.sender;
        let icecandidate = msg.payload;
        this.peers[peer].rtcPeerConnection.addIceCandidate(new RTCIceCandidate(icecandidate));
    };
}
$(document).ready(() => {
    openWsSession();
    peerConnection = new PeerConnection();
    if(sendRtcOffer === "yes") {
        // alert(sendRtcOffer);
        setTimeout(() => {
            console.log(ws.readyState);
            peerConnection.sendRtcOffer();
        }, 5000);
    }
});