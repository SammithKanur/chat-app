const showBuffer = () => {
    let loader = '<div class="buffer"><div class="loader"></div></div>';
    $("body").append(loader);
};
const hideBuffer = () => {
    $(".buffer").remove();
};
const beforeSend = (xhr) => {
    showBuffer();
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-type", "application/json");
};
const onComplete = (data) => {hideBuffer();}
const logout = (e) => {
    $.ajax({
        url: URL + "/user/request",
        type: "POST",
        data: JSON.stringify({validUserName:userName, session:session, type:"user", subtype:"logout", userName:userName}),
        beforeSend: beforeSend,
        success: function(data) {
            console.log(data);
        },
        error: function(data) {
            console.log(data);
        },
        complete:onComplete,
    })
    window.location = URL + "/";
};