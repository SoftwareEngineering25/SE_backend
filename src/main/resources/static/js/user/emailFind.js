$(document).ready(function () {
  // 휴대폰 번호 유효성 검사 정규표현식 (하이픈 없이 숫자만 10~11자리)
  const phonePattern = /^[0-9]{10,11}$/;

  // 휴대폰 번호 입력 필드에 대한 blur 이벤트
  $('#phone').on('blur', function () {
    const phone = $(this).val().trim();
    if (!phone) {
      $('#phoneError').text("휴대폰 번호를 입력하세요.").css('color', 'red');
      return;
    }
    if (!phonePattern.test(phone)) {
      $('#phoneError').text("형식에 맞게 입력하세요.").css('color', 'red');
    } else {
      $('#phoneError').text("");
    }
  });

  // 인증요청 버튼 클릭 시 이벤트 처리
  $('#sendCode').on('click', function () {
    const phone = $('#phone').val().trim();

    // 휴대폰 번호 형식 검사
    if (!phonePattern.test(phone)) {
      $('#phoneError').text("형식에 맞게 입력하세요.").css('color', 'red');
      return;
    }

    // 유효한 번호일 경우에만 인증 요청
    $.ajax({
      url: '/api/sms/send',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({phoneNumber: phone}),
      success: function (response) {
        alert("인증 코드가 발송되었습니다.");
        // 인증번호 입력 필드 활성화
        $('#authCode').prop('disabled', false);
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