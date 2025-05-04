$(document).ready(function () {
    const userIdRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const userPhonePattern = /^[0-9]{10,11}$/;

    $('#pwFindForm').on('submit', function (event) {
        event.preventDefault();

        const userEmailValue = $('#userEmail').val().trim();
        const userPhoneValue = $('#userPhone').val().trim();

        if (!userIdRegex.test(userEmailValue)) {
            alert("올바른 이메일 형식을 입력하세요.");
            return;
        }

        if (!userPhonePattern.test(userPhoneValue)) {
            alert("휴대폰 번호를 정확히 입력하세요.");
            return;
        }

        // AJAX 요청을 통해 전화번호와 이메일 확인
        $.ajax({
            url: '/user/PwFind',
            type: 'POST',
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data: $.param({
                userEmail: userEmailValue,
                userPhone: userPhoneValue
            }),
            success: function (response) {
                alert("인증이 완료되었습니다."); // 성공 메시지 표시
                window.location.href = "/user/PwReset"; // PwReset 페이지로 이동
            },
            error: function () {
                alert("이메일을 다시 확인해주세요.");
            }
        });
    });

    // 인증요청 버튼 클릭 시 이벤트 처리
    $('#sendCode').on('click', function () {
        const userPhone = $('#userPhone').val().trim();

        // 휴대폰 번호 형식 검사
        if (!userPhonePattern.test(userPhone)) {
            $('#phoneError').text("형식에 맞게 입력하세요.").css('color', 'red');
            return;
        }

        // 유효한 번호일 경우에만 인증 요청
        $.ajax({
            url: '/api/sms/send',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({phoneNumber: userPhone}),
            success: function (response) {
                alert("인증 코드가 발송되었습니다.");
                $('#authCode').prop('disabled', false); // 인증번호 입력 필드 활성화
            },
            error: function (xhr, status, error) {
                console.error("에러 발생: " + error);
                alert("서버와의 통신 중 오류가 발생했습니다.");
            }
        });
    });

    // 인증번호 확인
    $('#verifyCode').on('click', function () {
        const authCode = $('#authCode').val().trim();
        if (!authCode) {
            $('#authCodeError').text("인증번호를 입력하세요.").css('color', 'red');
            return;
        }

        $.ajax({
            url: '/api/sms/verify',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({authCode: authCode}),
            success: function (response) {
                if (response === "인증이 완료되었습니다.") {
                    $('#authCodeError').text(response).css('color', 'green');
                } else if (response === "인증번호가 만료되었습니다. 다시 시도해 주세요.") {
                    $('#authCodeError').text(response).css('color', 'orange');
                    alert("인증번호가 만료되었습니다. 다시 요청해주세요.");
                } else {
                    $('#authCodeError').text(response).css('color', 'red');
                }
            },
            error: function (xhr, status, error) {
                console.log("에러 발생: " + error);
                alert("서버와의 통신 중 오류가 발생했습니다.");
            }
        });
    });
});
