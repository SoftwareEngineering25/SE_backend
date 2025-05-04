document.addEventListener('DOMContentLoaded', function () {
  const passwordInput = document.getElementById('login-loginPw');
  const toggleIcon1 = document.querySelector('.login-formTogleOne');
  const toggleIcon2 = document.querySelector('.login-formTogleTwo');

  let isPasswordVisible = false;

  // 초기 상태 설정
  toggleIcon2.style.display = 'none'; // 처음에 비밀번호가 보이지 않도록 설정

  // 토글 기능
  toggleIcon1.addEventListener('click', function () {
      isPasswordVisible = true;
      passwordInput.type = 'text'; // 비밀번호 보이기
      toggleIcon1.style.display = 'none'; // 첫 번째 아이콘 숨기기
      toggleIcon2.style.display = 'block'; // 두 번째 아이콘 보이기
  });

  toggleIcon2.addEventListener('click', function () {
      isPasswordVisible = false;
      passwordInput.type = 'password'; // 비밀번호 숨기기
      toggleIcon1.style.display = 'block'; // 첫 번째 아이콘 보이기
      toggleIcon2.style.display = 'none'; // 두 번째 아이콘 숨기기
  });
});

document.addEventListener('DOMContentLoaded', function () {
    const loginButton = document.getElementById('login-loginButton');

    loginButton.addEventListener('click', function (event) {
        event.preventDefault(); // 기본 폼 제출 방지

        const loginId = document.getElementById('login-loginId').value.trim();
        const loginPw = document.getElementById('login-loginPw').value.trim();

        // 이메일과 비밀번호가 모두 비어있는지 확인
        if (!loginId && !loginPw) {
            alert("이메일과 비밀번호를 모두 입력해주세요.");
            return;
        }

        // 데이터를 JSON 객체로 구성
        const loginData = {
            userEmail: loginId, // 키 이름이 서버 DTO 필드명과 일치해야 함
            userPw: loginPw     // 키 이름이 서버 DTO 필드명과 일치해야 함
        };

        // 서버에 AJAX 요청 보내기
        fetch('/user/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json' // Content-Type은 application/json
            },
            body: JSON.stringify(loginData) // JSON 객체를 문자열로 변환하여 body에 담음 (이 부분이 중요!)
        })
            .then(response => {
                if (!response.ok) {
                    // HTTP 상태 코드가 200번대가 아니면 오류 처리
                    // 서버에서 JSON 오류 응답을 보낼 것으로 기대
                    return response.json().then(errorData => {
                        // 오류 응답 본문에서 메시지 추출
                        throw new Error(errorData.message || '로그인 실패');
                    });
                }
                // 성공 응답은 JSON으로 파싱
                return response.json();
            })
            .then(data => {
                // 성공 응답 처리
                alert(data.message); // "환영합니다."
                window.location.href = data.redirect; // 메인 페이지로 이동
            })
            .catch(error => {
                // 에러 발생 시 (네트워크 오류, HTTP 오류 응답 등)
                console.error("에러 발생:", error);
                // 오류 메시지가 Error 객체에 담겨 있을 수 있습니다.
                alert(error.message || "서버와의 통신 중 오류가 발생했습니다.");
                // 실패 시 로그인 페이지로 리다이렉트 (필요하다면)
                // window.location.href = "/user/login";
            });
    });
});