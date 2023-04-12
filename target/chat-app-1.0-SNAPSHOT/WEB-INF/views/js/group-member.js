const members = "Members";
const pendingInvitations = "Pending Invitations";
let searchList = null;
let curInfo = "";
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
const infoSearch = (ele) => {
    let prefix = ele.value;
    if(prefix.length > 0 && event.key === "Enter") {
        $(".info > .list").empty();
        let data = searchList.getList(prefix);
        data.forEach(name => {
            if(curInfo === members)
                $(".info > .list").append(getMemberListItem(name));
            else
                $(".info > .list").append(getPendingRequestItem(name));
        });
    }
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
                searchList = new SearchList(data['group-members'].map(name => name.userName));
                curInfo = members;
                data["group-members"].forEach(item => {
                    $(".info > .list").append(getMemberListItem(item.userName));
                });
            };
            break;
        case(pendingInvitations):
            ajaxUrl.data = JSON.stringify({type:"group", subtype:"group-pending-invitations", validUserName:userName,
                session:session, groupName:groupName});
            ajaxUrl.success = function(data) {
                console.log(data);
                curInfo = pendingInvitations;
                searchList = new SearchList(data["group-pending-invitations"].map(name => name.userName));
                data["group-pending-invitations"].forEach(item => {
                    $(".info > .list").append(getPendingRequestItem(item.userName));
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
            groupName:groupName, userName:member, status:1, inMeeting:0}),
        beforeSend:beforeSend,
        success:function(data) {
            console.log(data);
            showResponseMessage(data, "lawngreen");
        },
        error:function(data) {
            console.log(data);
            showResponseMessage(data.responseText, "red");
        },
        complete:onComplete,
    });
};
$(document).ready(() => {
    loadList({innerText:members});
});

