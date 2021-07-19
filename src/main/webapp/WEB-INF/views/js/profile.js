const sendRequest = "Send Request";
const withdrawRequest = "WithDraw Request";
const unfollow = "Unfollow";
const acceptRequest = "Accept";
const declineRequest = "Decline";
const showDropDown = (e) => {
    $(".user-dropdown").show();
};
const hideDropDown = (e) => {
    $(".user-dropdown").hide();
};
const handleConnection = (ele) => {
    let ajaxUrl = {
        url:URL + "/user/request",
        type:"POST",
        beforeSend: beforeSend,
        error: function(data) {
            logout(ele);
            console.log(data)
        },
        complete:onComplete,
    }
    switch(ele.innerText) {
        case(sendRequest):
            //create friend
            ajaxUrl.data = JSON.stringify({type:"friend", subtype:"request-create",validUserName:userName,
                session:session, user:userName, connection:friend});
            ajaxUrl.success = function(data) {setButtonStatus("Pending")};
            break;
        case(withdrawRequest):
            //remove friend
            ajaxUrl.data = JSON.stringify({type:"friend", subtype:"request-delete",validUserName:userName,
                session:session, user:userName, connection:friend});
            ajaxUrl.success = function(data) {setButtonStatus("not connected")};
            break;
        case(unfollow):
            //remove friend
            ajaxUrl.data = JSON.stringify({type:"friend", subtype:"request-delete",validUserName:userName,
                session:session, user:userName, connection:friend});
            ajaxUrl.success = function(data) {setButtonStatus("not connected")};
            break;
        case(acceptRequest):
            //change status to 3
            ajaxUrl.data = JSON.stringify({type:"friend", subtype:"request-update",validUserName:userName,
                session:session, user:userName, connection:friend});
            ajaxUrl.success = function(data) {setButtonStatus("Friends")};
            break;
        case(declineRequest):
            //remove friend
            ajaxUrl.data = JSON.stringify({type:"friend", subtype:"request-delete",validUserName:userName,
                session:session, user:userName, connection:friend});
            ajaxUrl.success = function(data) {setButtonStatus("not connected")};
            break;
    }
    $.ajax(ajaxUrl);
};
const setButtonStatus = (status) => {
    $(".profile > .button-area > .btn2").hide();
    switch (status) {
        case("not connected"):
            $(".profile > .button-area > .btn1").text(sendRequest);
            break;
        case("Friends"):
            $(".profile > .button-area > .btn1").text(unfollow);
            break;
        case("Pending"):
            $(".profile > .button-area > .btn1").text(withdrawRequest);
            break;
        case("Request"):
            $(".profile > .button-area > .btn1").text(acceptRequest);
            $(".profile > .button-area > .btn2").show();
            $(".profile > .button-area > .btn2").text(declineRequest);
            break;
    }
};
const loadProfile = () => {
    $.ajax({
        url:URL + "/user/request",
        type:"POST",
        data: JSON.stringify({type:"friend", subtype:"view", validUserName:userName, session:session, user:userName,
            connection:friend}),
        beforeSend: beforeSend,
        success: function(data) {
            $(".profile > .name").text(`Display Name: ${friend}`);
            $(".profile > .groups").text(`Followers: ${data.account.followers}`);
            $(".profile > .followers").text(`Groups: ${data.account.groups}`);
            setButtonStatus(data.status);
            $(".profile").show();
            console.log(data);
        },
        error: function(data) {
            console.log(data);
            logout(this);
        },
        complete:onComplete,
    })
}
$(document).ready(() => {
    openWsSession();
    loadProfile();
});