$(function () {
    $('#verify-img').click(function () {
        $(this).attr('src', $(this).attr('src') + '?' + Math.random());
    });

    $('#register-btn').click(function () {
        var param = {
            username: $('#username').val(),
            telephone: $('#telephone').val(),
            email: $('#email').val(),
            password: $('#password').val(),
            checkPassword: $('#check-password').val(),
            vcode: $('#vcode').val()
        };
        registerAjax(param);
    });
});

function registerAjax(param) {
    $.ajax({
        url: "/register",
        type: "post",
        data: JSON.stringify(param),
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function (response) {
            if (response.status === true) {

            } else {
                if (response.data !== null) {
                    for (var attr in response.data) {
                        break;
                    }
                } else {

                }
            }
            $('#verify-img').attr('src', '/vcode' + '?' + Math.random());
        },
        error: function (response) {
            $('#verify-img').attr('src', '/vcode' + '?' + Math.random());
        }
    });
}