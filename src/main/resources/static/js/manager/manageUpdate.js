// DOMContentLoaded 이벤트: DOM이 완전히 로드된 후 이벤트 리스너 추가
document.addEventListener("DOMContentLoaded", function() {
    // + 버튼에 대한 클릭 이벤트 설정
    const numberUpButton = document.getElementById('numberUp');
    if (numberUpButton) {
        numberUpButton.addEventListener('click', addInputField);
    }

    // 상품 추가 버튼 클릭 이벤트 설정
    const updateButton = document.getElementById("updateButton");
    if (updateButton) {
        updateButton.addEventListener('click', handleUpdate);
    }
});

// + 버튼 클릭 시 새로운 input 필드를 추가하는 함수
function addInputField() {
    const targetDiv = document.querySelectorAll('.register-right-con')[2]; // input 필드를 추가할 div 선택

    const inputCount = targetDiv.querySelectorAll('input').length; // 현재 div 내 input 필드 개수 확인

    if (inputCount >= 10) {
        alert('최대 10개의 입력 필드만 추가할 수 있습니다.');
        return;
    }

    const newInput = document.createElement('input');
    newInput.setAttribute('type', 'text');
    newInput.setAttribute('name', 'productCode1[]');  // name 속성 수정

    targetDiv.appendChild(newInput);
}

// 상품 추가 버튼 클릭 시 실행할 함수
function handleUpdate() {
    console.log("상품 추가 버튼이 클릭되었습니다."); // 로그 추가
    const productName1 = document.querySelector('input[name="productName1"]').value;

    const productCodes1 = Array.from(document.querySelectorAll('input[name="productCode1[]"]'))  // 수정된 부분
        .map(input => input.value)
        .filter(code => code.trim() !== "");  // 빈 값 제거

    const productPrice1 = document.querySelector('input[name="productPoint1"]').value;

    console.log(`상품명: ${productName1}, 상품코드: ${JSON.stringify(productCodes1)}, 소모 포인트: ${productPrice1}`);

    const formData = productCodes1.map(code1 => ({
        productName: productName1,
        productCode: code1,
        productPrice: productPrice1
    }));

    fetch('/manager/updateProduct', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("추가에 실패했습니다.");
            }
            return response.json();
        })
        .then(data => {
            alert('상품이 성공적으로 추가되었습니다.');
            window.location.href = "/manager/manageProduct";
        })
        .catch(error => {
            console.error('Error:', error); // 에러를 콘솔에 로그
            alert('상품 등록 중 오류가 발생했습니다.');
        });
}
