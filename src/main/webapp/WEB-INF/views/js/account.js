const connectionRequest = "Connection Requests";
const pendingInvitations = "Pending Invitations";
const groupRequests = "Group Requests";
const friends = "Friends";
const sendRequest = "Send Request";
const withdrawRequest = "WithDraw Request";
const unfollow = "Unfollow";
const acceptRequest = "Accept";
const declineRequest = "Decline";
let groups = 0;
let followers = 0;
const showDropDown = (e) => {
    $(".user-dropdown").show();
};
const hideDropDown = (e) => {
    $(".user-dropdown").hide();
};
const showList = (e) => {
    $(".account-list").show();
};
const hideList = (e) => {
    $(".account-list").hide();
};
const updatePassword = (ele) => {
    let newPassword = $(".account > .password > input").val();
    $.ajax({
        url:URL + "/user/request",
        type:"POST",
        data:JSON.stringify({type:"user", subtype:"account-update", validUserName:userName,
            session:session, userName:userName, password:newPassword, groups:groups, followers:followers}),
        beforeSend:beforeSend,
        success:function(data) {console.log(data);},
        error: function(data) {console.log(data)},
        complete:onComplete,
    });
};
const removeAccount = (ele) => {
    $.ajax({
        url: URL + "/user/request",
        type: "POST",
        data: JSON.stringify({type:"user", subtype:"account-remove", validUserName:userName, session:session,
            userName:userName}),
        beforeSend: beforeSend,
        success: function(data) {
            console.log(data);
            logout(ele);
        },
        error: function(data) {
            console.log(data);
            logout(ele);
        },
        complete:onComplete,
    });
};
const getListItem = (listType, connectionName) => {
    let item = "";
    switch(listType) {
        case(friends):
            item = `<div class='item'><div>${connectionName}</div><button onclick="handleClick(this)" ` +
                `connectionName=${connectionName}>${unfollow}</button></div>`;
            break;
        case(connectionRequest):
            item = `<div class='item'><div>${connectionName}</div><button onclick="handleClick(this)" ` +
                `connectionName=${connectionName} group="0">${acceptRequest}</button><button onclick="handleClick(this)" ` +
                `connectionName=${connectionName} group="0">${declineRequest}</button></div>`;
            break;
        case(pendingInvitations):
            item = `<div class='item'><div>${connectionName}</div><button onclick="handleClick(this)" ` +
                `connectionName=${connectionName}>${withdrawRequest}</button></div>`;
            break;
        case(groupRequests):
            item = `<div class='item'><div>${connectionName}</div><button onclick="handleClick(this)" ` +
                `connectionName=${connectionName} group="1">${acceptRequest}</button><button onclick="handleClick(this)" ` +
                `connectionName=${connectionName} group="1">${declineRequest}</button></div>`;
            break;
    }
    return item;
};
const handleClick = (ele) => {
    const connection = ele.getAttribute("connectionName");
    let isGroup = "";
    let ajaxUrl = {
        url: URL + "/user/request",
        type:"POST",
        beforeSend:beforeSend,
        success:function(data){
            ele.parentElement.parentElement.removeChild(ele.parentElement);
            console.log(data);
        },
        error:function(data){console.log(data)},
        complete:onComplete,
    };
    switch(ele.innerText) {
        case(acceptRequest):
            isGroup = ele.getAttribute("group");
            data = {type:"friend", subtype:"request-update", validUserName:userName,
                session:session, user:userName, connection:connection, status:3};
            if(isGroup === "1") {
                data.type = "group";
                data.subtype = "member-update-status";
                data.status = 2;
                data.groupName = connection;
                data.userName = userName;
            }
            ajaxUrl.data = JSON.stringify(data);
            break;
        case(declineRequest):
            isGroup = ele.getAttribute("group");
            data = {type:"friend", subtype:"request-delete", validUserName:userName,
                session:session, user:userName, connection:connection};
            if(isGroup === "1") {
                data.type = "group";
                data.subtype = "member-delete";
                data.groupName = connection;
                data.userName = userName;
            }
            ajaxUrl.data = JSON.stringify(data);
            break;
        case(unfollow):
            ajaxUrl.data = JSON.stringify({type:"friend", subtype:"request-delete", validUserName:userName,
                session:session, user:userName, connection:connection});
            break;
        case(withdrawRequest):
            ajaxUrl.data = JSON.stringify({type:"friend", subtype:"request-delete", validUserName:userName,
                session:session, user:userName, connection:connection});
            break;
    }
    $.ajax(ajaxUrl);
};
const loadList = (ele) => {
    $(".account").hide();
    $(".info").hide();
    $(".heading > h2").text(ele.innerText);
    $(".info > .list").empty();
    let ajaxUrl = {
        url: URL + "/user/request",
        type:"POST",
        beforeSend: beforeSend,
        error:function(data){console.log(data)},
        complete:onComplete,
    };
    switch(ele.innerText) {
        case(friends):
            ajaxUrl.data = JSON.stringify({type:"friend", subtype:"list-friends", validUserName:userName,
                session:session, user:userName});
            ajaxUrl.success = function(data) {
                console.log(data);
                let list = $(".info > .list");
                data["friends"].forEach(item => {list.append(getListItem(friends, item.connection))});
            };
            break;
        case(connectionRequest):
            ajaxUrl.data = JSON.stringify({type:"friend", subtype:"list-connection-request", validUserName:userName,
                session:session, user:userName});
            ajaxUrl.success = function(data) {
                console.log(data);
                let list = $(".info > .list");
                data["connection-requests"].forEach(item => {list.append(getListItem(connectionRequest, item.connection))});
            };
            break;
        case(pendingInvitations):
            ajaxUrl.data = JSON.stringify({type:"friend", subtype:"list-pending-invitation", validUserName:userName,
                session:session, user:userName});
            ajaxUrl.success = function(data) {
                console.log(data);
                let list = $(".info > .list");
                data["pending-invitations"].forEach(item => {list.append(getListItem(pendingInvitations, item.connection))});
            };
            break;
        case(groupRequests):
            ajaxUrl.data = JSON.stringify({type:"group", subtype:"user-group-requests", validUserName:userName,
                session:session, userName:userName});
            ajaxUrl.success = function(data) {
                console.log(data);
                let list = $(".info > .list");
                data["user-group-requests"].forEach(item => {list.append(getListItem(groupRequests, item.groupName))});
            };
            break;
    }
    $.ajax(ajaxUrl);
    $(".info").show();
};
const loadAccount = (ele) => {
    $(".info").hide();
    $(".account").hide();
    $(".heading > h2").text("Account");
    $.ajax({
        url: URL + "/user/request",
        type: "POST",
        data: JSON.stringify({type:"user", subtype:"account-view", validUserName:userName,
            session:session, userName:userName}),
        beforeSend:beforeSend,
        success:function(data) {
            console.log(data);
            $(".account > .name").text(`Display Name: ${data.userName}`);
            $(".account > .groups").text(`Groups: ${data.groups}`);
            $(".account > .followers").text(`Followers: ${data.followers}`);
            $(".account > .password > input").val(data.password);
            console.log($(".account > .password > input").val());
            groups = data.groups;
            followers = data.followers;
        },
        error: function(data) {
            console.log(data);
        },
        complete:onComplete,
    });
    $(".account").show();
};

$(document).ready(() => {
    loadAccount({});
});