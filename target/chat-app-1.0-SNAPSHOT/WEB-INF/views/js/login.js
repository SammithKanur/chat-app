let userName;
let password;
const getUserName = () => {
    return $("input[name='username']").val();
};
const getPassword = () => {
    return $("input[name='password']").val();
};
const showError = (msg) => {
    $(".message").show();
    $(".message > h2").text(msg)
};
const handleLogin = (e) => {
    event.preventDefault();
    userName = getUserName();
    password = getPassword();
    $.ajax({
        url: URL + "/user/login",
        type: "POST",
        data: JSON.stringify({userName:userName, password:password}),
        beforeSend: beforeSend,
        success: function(data) {
            console.log(JSON.stringify(data));
            window.location = URL + `/user/home?userName=${userName}&session=${data}`;
        },
        error: function(data) {
            showError(data.responseText);
        },
        complete:onComplete,
    });
};
$(document).ready(() => {
});