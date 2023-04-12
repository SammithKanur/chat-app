let userName;
let password;
const getUserName = () => {
    return $("input[name='username']").val();
};
const getPassword = () => {
    return $("input[name='password']").val();
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
            console.log(data);
            showResponseMessage(data.responseText, "red");
        },
        complete:onComplete,
    });
};
$(document).ready(() => {
});