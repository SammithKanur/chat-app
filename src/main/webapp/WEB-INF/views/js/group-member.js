const members = "Members";
const pendingInvitations = "Pending Invitations";
const showDropDown = (e) => {
    $(".user-dropdown").show();
}
const hideDropDown = (e) => {
    $(".user-dropdown").hide();
}
const showList = (e) => {
    $(".account-list").show();
}
const hideList = (e) => {
    $(".account-list").hide();
}
const getMemberListItem = (connection) => {
    return `<div class="item"><div>${connection}</div></div>`;
};
const getPendingRequestItem = (connection) => {
    return `<div class="item"><div>${connection}</div></div>`;
};
const loadList = (ele) => {
    $(".info > .list").empty();
    let ajaxUrl = {
        url:URL + "/user/request",
        type:"POST",
        beforeSend: beforeSend,
        error: function(data) {console.log(data)},
        complete:onComplete,
    };
    switch (ele.innerText) {
        case(members):
            ajaxUrl.data = JSON.stringify({type:"group", subtype:"group-members", validUserName:userName,
                session:session, groupName:groupName});
            ajaxUrl.success = function(data) {
                console.log(data);
                data["group-members"].forEach(item => {
                    $(".info > .list").append(getMemberListItem(item));
                });
            };
            break;
        case(pendingInvitations):
            ajaxUrl.data = JSON.stringify({type:"group", subtype:"group-pending-invitations", validUserName:userName,
                session:session, groupName:groupName});
            ajaxUrl.success = function(data) {
                console.log(data);
                data["group-pending-invitations"].forEach(item => {
                    $(".info > .list").append(getPendingRequestItem(item));
                });
            };
            break;
    }
    $.ajax(ajaxUrl);
};
const deleteMember = (ele) => {
    $.ajax({
        url:URL + "/user/request",
        type:"POST",
        data: JSON.stringify({type:"group", subtype:"member-delete", validUserName:userName,
            session:session, groupName:groupName, userName:userName}),
        beforeSend:beforeSend,
        success:function(data) {
            console.log(data);
            window.location = URL + `/user/home?userName=${userName}&session=${session}`;
        },
        error:function(data) {console.log(data);},
        complete:onComplete,
    });
};
const addMember = (ele) => {
    let member = $(".navbar > input").val();
    $.ajax({
        url: URL + "/user/request",
        type:"POST",
        data:JSON.stringify({type:"group", subtype:"member-add", validUserName:userName, session:session,
            groupName:groupName, userName:member, status:1}),
        beforeSend:beforeSend,
        success:function(data) {
            console.log(data);
        },
        error:function(data) {console.log(data);},
        complete:onComplete,
    });
};
$(document).ready(() => {
    openWsSession();
    loadList({innerText:members});
});

