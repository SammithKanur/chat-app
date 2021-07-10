let friendConnectionType = "friend";
let groupConnectionType = "group";
let state = {connectionType:"", connection:""};
let ws;
const navigateToProfile = (friend) => {
    window.location = URL + `/user/profile?userName=${userName}&session=${session}&friend=${friend}`;
};
const getAccount = (ele) => {
    window.location = URL + `/user/account?userName=${userName}&session=${session}`;
};
const closeGroupForm = (ele) => {
    $(".popup").hide();
};
const openCreateGroup = (ele) => {
    $(".popup").css("display", "flex");
};
const createGroup = (ele) => {
    let groupName = $(".popup > .create-group > input").val();
    $.ajax({
        url: URL + "/user/request",
        type:"POST",
        data:JSON.stringify({type:"group", subtype:"create", validUserName:userName, session:session,
            groupName:groupName, userName:userName, status:2}),
        beforeSend: beforeSend,
        success: function(data){
            console.log(data);
            $(".popup > .create-group > p").text(data);
        },
        error: function(data) {
            console.log(data);
            $(".popup > .create-group > p").text(data.responseText);
        },
        complete:onComplete,
    });
};
const getProfile = (ele) => {
    $.ajax({
        url: URL + "/user/request",
        type: "POST",
        data: JSON.stringify({type:"friend", subtype:"view", validUserName:userName, session:session, user:userName,
            connection:ele.value}),
        beforeSend: beforeSend,
        success: function(data) {
            if(data.account.userName !== null)
                navigateToProfile(ele.value);
            console.log(data);
        },
        error: function(data) {
            console.log(data);
        },
        complete:onComplete,
    });
};
const getPredictions = (ele) => {
    if(event.key === "Enter") {
        getProfile(ele);
    }
    else if(event.which >= 48 && event.which <= 90) {
        //get predictions
    }
};
const mouseMoveHandler = (e) => {
    if(dir === 'X') {
        document.documentElement.style.setProperty(cssVar, event.clientX);
    } else {
        document.documentElement.style.setProperty(cssVar, event.clientY);
    }
};
const mouseUpHandler = () => {
    document.removeEventListener("mousemove", mouseMoveHandler);
    document.removeEventListener("mouseup", mouseUpHandler);
};
const sidebarMouseDown = (e) => {
    cssVar = "--sidebar-width";
    dir = "X";
    document.addEventListener("mousemove", mouseMoveHandler);
    document.addEventListener('mouseup', mouseUpHandler);
}
const chatareaMouseDown = (e) => {
    cssVar = "--chat-height";
    dir = "Y";
    document.addEventListener("mousemove", mouseMoveHandler);
    document.addEventListener('mouseup', mouseUpHandler);
}
const showDropDown = (e) => {
    $(".user-dropdown").show();
}
const hideDropDown = (e) => {
    $(".user-dropdown").hide();
}
const resizeTextArea = (e) => {
    console.log(e.scrollHeight, e.style.flexBasis);
    if(e.scrollHeight < 400)
        e.style.flexBasis = 200 + "px";
    else
        e.style.flexBasis = e.scrollHeight + "px";
};
const getGroupListItem = (connectionName) => {
    return `<div connectionType="${groupConnectionType}" connection="${connectionName}" onclick="loadMesages(this)">${connectionName}` +
        `<div class="fa fa-cog" onclick="getGroupSettings(this)" ` +
        `connectionName="${connectionName}"></div></div>`;
};
const getFriendListItem = (connectionName) => {
    return `<div connectionType="${friendConnectionType}" connection="${connectionName}" onclick="loadMesages(this)"> ` +
        `${connectionName}</div>`;
};
const getGroupSettings = (ele) => {
    let connection = ele.getAttribute("connectionName");
    window.location = URL + `/user/group-member?userName=${userName}&session=${session}&groupName=${connection}`;
};
const loadList = (data, listType) => {
    $.ajax({
        url:URL + "/user/request",
        type:"POST",
        data:data,
        beforeSend:beforeSend,
        success: function(data) {
            console.log(data);
            if(listType === "friends") {
                data["friends"].forEach(item => {
                    $(".friends-list > .list").append(getFriendListItem(item));
                });
            } else {
                data["user-groups"].forEach(item => {
                    $(".groups-list > .list").append(getGroupListItem(item));
                });
            }
        },
        error:function(data){concole.log(data)},
        complete:onComplete,
    });
};
const getMessage = (message) => {
    if(message[1] === userName) {
        return `<div class="message" style="margin-left: calc(60% - 10px)"><div>${message[1]}</div><textarea readonly>` +
            `${message[0]}</textarea></div>`
    }
    return `<div class="message"><div>${message[1]}</div><textarea readonly>` +
        `${message[0]}</textarea></div>`
};
const loadMesages = (ele) => {
    $("#send-message").show();
    let connection = ele.getAttribute("connection");
    let connectionType = ele.getAttribute("connectionType");
    state.connectionType = connectionType;
    state.connection = connection;
    let list = $(".message-area");
    list.empty();
    let ajaxUrl = {
        url:URL + "/user/request",
        type:"POST",
        beforeSend:beforeSend,
        success:function(data) {
            console.log(data);
            data["chat"].forEach(message => {
                list.append(getMessage(message));
                resizeTextArea(document.getElementsByClassName("message-area")[0].lastChild.lastChild)
            });
        },
        error:function(data) {console.log(data);},
        complete:onComplete,
    };
    if(connectionType === friendConnectionType) {
        ajaxUrl.data = JSON.stringify({type:"friend", subtype:"chat-read", validUserName:userName,
            session:session, user:userName, connection:connection});
    } else {
        ajaxUrl.data = JSON.stringify({type:"group", subtype:"chat-read", validUserName:userName,
            session:session, groupName:connection});
    }
    $.ajax(ajaxUrl);
};
const writeMessage = (ele) => {
    let message = $(".chat-area").val();
    let ajaxUrl = {
        url:URL + "/user/request",
        type:"POST",
        beforeSend:beforeSend,
        success:function(data) {
            console.log(data);
            $(".message-area").append(getMessage([message, userName]));
            resizeTextArea(document.getElementsByClassName("message-area")[0].lastChild.lastChild);
        },
        error:function(data){console.log(data);},
        complete:onComplete,
    };
    if(state.connectionType === friendConnectionType) {
        ajaxUrl.data = JSON.stringify({type:"friend", subtype:"chat-write", validUserName:userName,
            session:session, user:userName, connection:state.connection, message:message});
    } else {
        ajaxUrl.data = JSON.stringify({type:"group", subtype:"chat-write", validUserName:userName,
            session:session, groupName:state.connection, userName:userName, message:message});
    }
    $.ajax(ajaxUrl);
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
    if(state.connectionType === msg.connectionType && state.connection === msg.connection) {
        $(".message-area").append(getMessage([msg.message, msg.sender]));
        resizeTextArea(document.getElementsByClassName("message-area")[0].lastChild.lastChild);
    }
};
$(document).ready(() => {
    //load friendList
    openWsSession();
    loadList(JSON.stringify({type:"friend", subtype:"list-friends", validUserName:userName, session:session,
        user:userName}), "friends");
    //loadGroupsList
    loadList(JSON.stringify({type:"group", subtype:"user-group-list", validUserName:userName, session:session,
        userName:userName}), "groups");
});