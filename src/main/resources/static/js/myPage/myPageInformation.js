// 이미지 업로드 버튼 클릭 시 파일 선택 창 열기
document.getElementById('uploadIcon').addEventListener('click', function() {
    document.getElementById('imageUpload').click();
});

// 이미지 선택 후 미리보기 표시
document.getElementById('imageUpload').addEventListener('change', function(event) {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();

        reader.onload = function(e) {
            console.log('이미지 로드 성공:', e.target.result);

            // 기존 프로필 이미지 숨기기
            const profileImages = document.getElementsByClassName('profileImage');
            Array.from(profileImages).forEach(image => {
                image.style.display = 'none';
            });

            // 기존의 미리보기 이미지를 제거하여 중복 방지
            let newProfileImage = document.getElementById('newProfileImage');
            if (newProfileImage) {
                newProfileImage.src = e.target.result; // 기존 미리보기 이미지가 있으면 src만 변경
            } else {
                // 새로 선택한 이미지를 위한 img 태그 생성
                newProfileImage = document.createElement('img');
                newProfileImage.id = 'newProfileImage';  // 새 이미지에 고유한 ID를 설정
                newProfileImage.classList.add('profileImage');
                newProfileImage.width = 150;
                newProfileImage.height = 150;
                newProfileImage.alt = "프로필 이미지";
                newProfileImage.src = e.target.result;

                // 프로필 이미지 표시 영역에 새 이미지 추가
                document.querySelector('.myPage-profile').appendChild(newProfileImage);
            }
        };

        reader.readAsDataURL(file);
    }
});


// ------ 수정 완료 alert 창 ----- //
document.getElementById('completeWriteBtn').addEventListener('click', function() {
    if (!confirm('정말 수정하시겠습니까?')) {
        return;
    }

    const nickname = document.getElementById('nicknameInput').value.trim();
    const profileImage = document.getElementById('imageUpload').files[0];

    const nicknameRegex = /^[a-zA-Z0-9가-힣]{2,5}$/;
    if (!nicknameRegex.test(nickname)) {
        alert('닉네임을 다시 확인해주세요.');
        return;
    }

    const formData = new FormData();
    formData.append('nickname', nickname);

    // 프로필 이미지가 있는 경우에만 FormData에 추가
    if (profileImage) {
        formData.append('profileImage', profileImage);
    }

    // 서버로 비동기 요청 보내기
    fetch('/myPage/updateProfile', {
        method: 'POST',
        body: formData,
    })
        .then(response => response.json())
        .then(data => {
            if (data.photoPath || nickname) {
                // URL 디코딩 후 이미지 경로 사용
                const decodedPath = decodeURIComponent(data.photoPath);
                // 서버에서 반환된 이미지 경로를 사용하여 프로필 이미지 업데이트
                alert('수정이 완료되었습니다.\n상단 프로필은 다음 로그인 시 적용됩니다.');
            }
            location.reload();  // 업데이트 후 마이페이지로 리다이렉트
        })
        .catch(error => {
            console.error('수정 요청 중 오류가 발생했습니다:', error);
            alert('닉네임 또는 이미지를 다시 확인해주세요.');
        });
});

    // 닉네임 수정 부분
    const nicknameDisplay = document.getElementById('nicknameDisplay');
    const nicknameInput = document.getElementById('nicknameInput');
    const nicknameChangeButton = document.querySelector('.myPage-nickNameChange');
    const nicknameError = document.getElementById('nicknameError');

    // 닉네임 텍스트 클릭 시 input 필드 보여주기
    nicknameDisplay.addEventListener('click', function() {
        nicknameDisplay.style.display = 'none';
        nicknameInput.style.display = 'inline';
        nicknameInput.value = nicknameDisplay.textContent;
        nicknameInput.focus();
        nicknameError.style.display = 'none';
        nicknameInput.style.border = '1px solid #317FF0';
    });

    // 닉네임 변경 클릭 시 input 필드 보여주기
    nicknameChangeButton.addEventListener('click', function() {
        nicknameDisplay.style.display = 'none';
        nicknameInput.style.display = 'inline'; // 입력 필드 보이기
        nicknameInput.focus(); // 입력 필드에 포커스 주기
        nicknameInput.style.border = '1px solid #317FF0'; // 입력 필드 테두리 색상 변경
    });

    // 닉네임 유효성 검사 및 중복 확인
    nicknameInput.addEventListener('blur', function() {
        const newNickname = nicknameInput.value.trim(); // 입력 값 가져오기
        const currentNickname = nicknameDisplay.textContent.trim(); // 현재 닉네임 가져오기

        const nicknameRegex = /^[a-zA-Z0-9가-힣]{2,5}$/; // 2자 이상 5자 이내의 한글, 영문, 숫자
        if (!nicknameRegex.test(newNickname)) {
            nicknameError.textContent = "2자 이상 5자 이내의 한글, 영문, 숫자만 허용됩니다.";
            nicknameError.style.display = 'block';
            nicknameError.style.marginLeft = '13px';
            nicknameInput.style.border = '1px solid red';
            nicknameError.style.color = 'red'; // 유효성 검사 실패 시 빨간색 표시
            return;
        }

        if (newNickname === currentNickname) {
            nicknameError.textContent = "현재 닉네임과 동일합니다.";
            nicknameError.style.display = 'block';
            nicknameError.style.marginLeft = '13px';
            nicknameInput.style.border = '1px solid orange'; // 주의 색상 표시
            nicknameError.style.color = 'orange'; // 주의 색상
            return;
        }

        if (newNickname) {
            fetch('/myPage/checkNickname', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ nickname: newNickname })
            })
                .then(response => response.json())
                .then(isDuplicate => {
                    if (isDuplicate) {
                        nicknameError.textContent = "이미 사용 중인 닉네임입니다.";
                        nicknameError.style.marginLeft = '13px';
                        nicknameError.style.display = 'block';
                        nicknameError.style.color = 'red'; // 빨간색으로 변경
                        nicknameInput.style.border = '1px solid red'; // 빨간색 테두리
                    } else {
                        nicknameError.textContent = "닉네임 사용 가능합니다.";
                        nicknameError.style.marginLeft = '13px';
                        nicknameError.style.display = 'block';
                        nicknameError.style.color = 'green'; // 초록색으로 유지
                        nicknameInput.style.border = '1px solid #317FF0'; // 정상 테두리
                    }
                })
                .catch(error => {
                    console.error('다시 입력해주세요:', error);
                });
        }
    });
