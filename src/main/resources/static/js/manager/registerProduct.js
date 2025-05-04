// "상품 코드 추가" 버튼에 대한 이벤트 리스너
document.getElementById('numberUp').addEventListener('click', function () {
    const targetDiv = document.getElementById('targetDiv'); // 새로운 입력 필드가 추가될 타겟 div

    // 현재 입력 필드 수를 계산
    const inputCount = targetDiv.querySelectorAll('input').length;

    // 최대 10개의 입력 필드만 허용
    if (inputCount >= 10) {
        alert('최대 10개의 입력 필드만 추가할 수 있습니다.');
        return;
    }

    // 새로운 상품 코드 입력 필드 생성
    const newInput = document.createElement('input');
    newInput.setAttribute('type', 'text');
    newInput.setAttribute('name', 'productCode'); // 그룹화를 위해 같은 이름 사용

    targetDiv.appendChild(newInput); // 새로운 입력 필드를 추가
});

// "상품 등록" 버튼 클릭 이벤트 리스너
document.querySelector("#productButton").addEventListener('click', function () {
    const productName = document.querySelector('input[name="productName"]').value;
    const productCodes = Array.from(document.querySelectorAll('input[name="productCode"]'))
        .map(input => input.value)
        .filter(code => code.trim() !== ""); // 빈 값을 제거

    const productPrice = document.querySelector('input[name="productPoint"]').value;
    const fileInput = document.querySelector('input[name="file"]'); // 파일 입력 선택

    // FormData 객체 생성
    const formData = new FormData();

    // 상품 이름, 가격, 코드 추가
    productCodes.forEach((code) => {
        formData.append('productName', productName); // 상품 이름 추가
        formData.append('productPrice', productPrice); // 상품 가격 추가
        formData.append('productCode', code); // 상품 코드 추가
    });

    // 파일은 한 번만 추가
    if (fileInput.files.length > 0) {
        formData.append('file', fileInput.files[0]); // 파일 추가
    }

    // 서버에 상품 등록 요청 전송
    fetch('/manager/registerProduct', {
        method: 'POST',
        body: formData // FormData 객체 전송
    })
        .then(response => {
            // 응답의 Content-Type을 확인하여 JSON인지 확인
            const contentType = response.headers.get("Content-Type");
            if (!response.ok) {
                throw new Error("등록에 실패했습니다."); // 오류 응답 처리
            }
            if (contentType && contentType.includes("application/json")) {
                return response.json(); // JSON 응답 파싱
            } else {
                throw new Error("응답이 JSON 형식이 아닙니다.");
            }
        })
        .then(data => {
            alert(data.message); // 성공 메시지
            window.location.href = "/manager/manageProduct"; // 상품 관리 페이지로 리디렉션
        })
        .catch(error => {
            alert('상품 등록 중 오류가 발생했습니다: ' + error.message); // 오류 처리
        });
});


