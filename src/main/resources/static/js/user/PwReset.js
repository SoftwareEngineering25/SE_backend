$(document).ready(function () {
  // 비밀번호 정규표현식 검사 함수: 최소 8자, 문자, 숫자, 특수문자 포함
  function validatePassword(password) {
    const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    return regex.test(password);
  }

  const newPassword = document.getElementById("newPassword");
  const confirmNewPassword = document.getElementById("confirmNewPassword");
  const passwordError = document.getElementById("passwordError");
  const confirmPasswordError = document.getElementById("confirmPasswordError");

  // 첫 번째 비밀번호 필드 input 이벤트: 비밀번호 유효성 검사
  newPassword.addEventListener("input", function () {
    const password = newPassword.value;
    if (!validatePassword(password)) {
      passwordError.innerHTML = "비밀번호는 최소 8자 이상이어야 하며, 문자, 숫자, 특수문자를 포함해야 합니다.";
      passwordError.style.color = "red";
      confirmPasswordError.innerHTML = ""; // 비밀번호 오류 시 확인 오류 초기화
    } else {
      passwordError.innerHTML = ""; // 오류 메시지 초기화
    }
  });

  // 두 번째 비밀번호 필드 input 이벤트: 비밀번호 일치 확인 및 유효성 검사
  confirmNewPassword.addEventListener("input", function () {
    const password = newPassword.value;
    const confirmPassword = confirmNewPassword.value;

    // 비밀번호 유효성 검사 추가
    if (!validatePassword(password)) {
      confirmPasswordError.innerHTML = ""; // 비밀번호가 유효하지 않으면 확인 오류 메시지 초기화
    } else if (password === confirmPassword) {
      confirmPasswordError.innerHTML = "비밀번호가 일치합니다.";
      confirmPasswordError.style.color = "green";
    } else {
      confirmPasswordError.innerHTML = "비밀번호가 일치하지 않습니다.";
      confirmPasswordError.style.color = "red";
    }
  });

  // 비밀번호 토글 기능: 비밀번호 보기/숨기기
  $('#passwordToggle').on('click', function () {
    const passwordInput = $('#newPassword');
    const passwordToggleIcon = $('#passwordToggle');

    if (passwordInput.attr('type') === 'password') {
      passwordInput.attr('type', 'text'); // 비밀번호 보이기
      passwordToggleIcon.attr('src', './../../image/view.png'); // 아이콘 변경
    } else {
      passwordInput.attr('type', 'password'); // 비밀번호 숨기기
      passwordToggleIcon.attr('src', './../../image/noView.png'); // 아이콘 변경
    }
  });

  // 비밀번호 확인 토글 기능: 비밀번호 확인 필드 보기/숨기기
  $('#confirmPasswordToggle').on('click', function () {
    const confirmPasswordInput = $('#confirmNewPassword');
    const confirmPasswordToggleIcon = $('#confirmPasswordToggle');

    if (confirmPasswordInput.attr('type') === 'password') {
      confirmPasswordInput.attr('type', 'text'); // 비밀번호 보이기
      confirmPasswordToggleIcon.attr('src', './../../image/view.png'); // 아이콘 변경
    } else {
      confirmPasswordInput.attr('type', 'password'); // 비밀번호 숨기기
      confirmPasswordToggleIcon.attr('src', './../../image/noView.png'); // 아이콘 변경
    }
  });

  // 폼 제출 시 유효성 검사
  document.getElementById("pwResetForm").onsubmit = function() {
    const password = newPassword.value;
    const confirmPassword = confirmNewPassword.value;

    if (!validatePassword(password)) {
      alert("비밀번호는 최소 8자 이상이어야 하며, 문자, 숫자, 특수문자를 포함해야 합니다.");
      return false; // 폼 제출 방지
    }

    if (password !== confirmPassword) {
      alert("비밀번호가 일치하지 않습니다.");
      return false; // 폼 제출 방지
    }

    alert("비밀번호 변경이 완료 되었습니다.\n로그인 페이지로 이동합니다.")
    return true; // 모든 검사를 통과하면 폼 제출 허용
  };
});
