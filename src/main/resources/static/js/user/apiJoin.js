$(document).ready(function () {

    let isPhoneVerified = false; // 휴대폰 인증 여부

    // 전체 동의 체크박스 클릭 시 나머지 체크박스 모두 체크 또는 해제
    $('#checkAll').on('change', function () {
        var isChecked = $(this).is(':checked');
        $('.apijoin-checkboxInput').not('#checkAll').prop('checked', isChecked);
    });

    // 개별 체크박스 상태 변경 시 전체 동의 체크박스 상태도 변경
    $('.apijoin-checkboxInput').not('#checkAll').on('change', function () {
        var allChecked = $('.apijoin-checkboxInput').not('#checkAll').length === $('.apijoin-checkboxInput:checked').not('#checkAll').length;
        $('#checkAll').prop('checked', allChecked);
    });


    $('#accountEmail').on('blur', function () {
        const accountEmailValue = $('#accountEmail').val().trim();

        // 이메일 중복 확인 요청
        $.ajax({
            url: '/api/user/checkEmail', // 이메일 중복 확인 API 경로
            type: 'GET',
            data: {userEmail: accountEmailValue},
            success: function (response) {
                if (response) {
                    // 중복된 이메일일 경우
                    $("#accountEmailError").text("이미 존재하는 이메일입니다.").css({"color": "red", "display": "block"});
                } else {
                    // 사용 가능한 이메일일 경우
                    $("#accountEmailError").text("사용 가능한 이메일입니다.").css({"color": "green", "display": "block"});
                }
            },
            error: function (xhr, status, error) {
                console.error("에러 발생: " + error);
                alert("서버와의 통신 중 오류가 발생했습니다.");
            }
        });
    });

    // 닉네임 확인
    // 닉네임 정규표현식: 이메일 형식 검사
    const nicknameRegex = /^[a-zA-Z0-9가-힣]{2,5}$/; // 2자 이상 5자 이내의 한글, 영문, 숫자

    $('#name').on('blur', function () {
        const userNickName = $('#name').val().trim();

        // 이메일 중복 확인 요청
        $.ajax({
            url: '/checkNickName', // 이메일 중복 확인 API 경로
            type: 'POST',
            data: ({userNickName: userNickName}),
            success: function (response) {

                if (userNickName.length > 0 && !nicknameRegex.test(userNickName)) {
                    $("#nameError").text("2글자 이상, 5글자 이하만 가능합니다").css({"color": "red", "display": "block"});
                } else if (response === 1) {
                    // 중복된 이메일일 경우
                    $("#nameError").text("이미 존재하는 닉네임 입니다.").css({"color": "red", "display": "block"});
                } else {
                    // 사용 가능한 이메일일 경우
                    $("#nameError").text("사용 가능한 닉네임 입니다.").css({"color": "green", "display": "block"});
                }
            },
            error: function (xhr, status, error) {
                console.error("에러 발생: " + error);
                alert("서버와의 통신 중 오류가 발생했습니다.");
            }
        });
    });

    let phoneAlertShown = false; // 중복 전화번호에 대한 alert 표시 여부
    // 핸드폰 중복확인
    $('#phone').on('blur', function () {
        const phone = $('#phone').val().trim();

        $.ajax({
            url: '/api/user/checkPhone',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({userPhone: phone}),
            success: function (response) {
                if (response.exists) {
                    if (!phoneAlertShown) {
                        phoneAlertShown = true;
                    }
                    $('#phoneError').text("전화번호가 이미 존재합니다.").css('color', 'red');
                    $('#sendCode').prop('disabled', true);
                } else {
                    $('#phoneError').text("");
                    phoneAlertShown = false;
                    $('#sendCode').prop('disabled', false);
                }
            },
            error: function (xhr, status, error) {
                console.error("에러 발생: " + error);
                alert("서버와의 통신 중 오류가 발생했습니다.");
            }
        });
    });


// 휴대폰 번호 유효성 검사 정규표현식 (하이픈 없이 숫자만 10~11자리)
    const phonePattern = /^[0-9]{10,11}$/;

    $('#sendCode').on('click', function () {
        const phone = $('#phone').val().trim();

        // 휴대폰 번호 형식 검사
        if (!phonePattern.test(phone)) {
            $('#phoneError').text("올바른 휴대폰 번호를 입력하세요. (하이픈 없이 10~11자리 숫자)").css("color", "red");
            return; // 유효하지 않은 번호면 요청을 보내지 않음
        } else {
            $('#phoneError').text(""); // 오류 메시지 초기화
        }

        // 유효한 번호일 경우에만 인증 요청
        $.ajax({
            url: '/api/sms/send',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({phoneNumber: phone}),
            success: function (response) {
                alert(response);
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
                    isPhoneVerified = true; // 인증 완료
                } else if (response === "인증번호가 만료되었습니다. 다시 시도해 주세요.") {
                    $('#authCodeError').text(response).css('color', 'orange');
                    alert("인증번호가 만료되었습니다. 다시 요청해주세요.");
                    isPhoneVerified = false; // 인증 실패
                } else {
                    $('#authCodeError').text(response).css('color', 'red');
                    isPhoneVerified = false;
                }
            },
            error: function (xhr, status, error) {
                console.log("에러 발생: " + error);
                alert("서버와의 통신 중 오류가 발생했습니다.");
            }
        });
    });

    // 회원가입 버튼 클릭 시 유효성 검사 및 필수 체크박스 확인
    $('#submitForm').on('click', function (event) {
        event.preventDefault(); // 기본 폼 제출 동작 방지

        // 필수 체크박스 검사
        const isAllRequiredChecked = $('#checkAge').is(':checked') &&
            $('#checkTerms').is(':checked') &&
            $('#checkPrivacy').is(':checked');

        if (!isAllRequiredChecked) {
            alert("필수 항목에 모두 동의해야 회원가입이 가능합니다.");
            return;
        }

        // 닉네임과 휴대폰 번호 유효성 검사
        const nameValue = $('#name').val().trim();
        if (nameValue === "") {
            alert("닉네임을 입력하세요.");
            return;
        }

        // 휴대폰 인증 여부 검사
        if (!isPhoneVerified) {
            alert("휴대폰 인증을 완료해야 회원가입이 가능합니다.");
            return;
        }

        // 핸드폰 번호 검사
        const phonePattern = /^[0-9]{10,11}$/;
        const phoneValue = $('#phone').val().trim();
        if (!phonePattern.test(phoneValue)) {
            alert("올바른 휴대폰 번호를 입력하세요.");
            return;
        }

        showAlertAndRedirect();
    });
});

// 회원가입 완료 시 알림 및 리다이렉트
function showAlertAndRedirect() {
    alert("회원가입이 완료되었습니다.");
    // 실제 회원가입 폼 제출 또는 리다이렉트 처리
    $('#apijoinForm').submit(); // 폼을 서버로 제출
}