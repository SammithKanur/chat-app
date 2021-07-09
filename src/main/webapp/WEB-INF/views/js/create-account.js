const getUserName = () => {
    return $("input[name='username']").val();
};
const getPassword = () => {
    return $("input[name='password']").val();
};
const showMessage = (msg, color) => {
    $(".message").show();
    $(".message").css("color", color);
    $(".message > h2").text(msg);
};
const handleCreate = (e) => {
    event.preventDefault();
    userName = getUserName();
    password = getPassword();

    $.ajax({
        url: URL + "/user/create-account",
        type: "POST",
        data: JSON.stringify({userName:userName, password:password, groups:0, followers:0}),
        beforeSend:beforeSend,
        success: function(data) {
            showMessage(data, "black");
        },
        error: function(data) {
            showMessage(data.responseText, "red");
        },
        complete:onComplete,
    });
}