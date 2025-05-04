$(document).ready(function () {
    let currentPosition = 10; // 슬라이드 시작 위치를 10px로 설정
    const $productFrame = $('.pointShop-productFrame'); // 보이는 영역
    const $productList = $('.pointShop-productList'); // 전체 상품 리스트
    const $products = $('.pointShop-product'); // 각각의 상품들
    const productWidth = $products.outerWidth(true); // 상품 하나의 너비 (마진 포함)
    const visibleCount = Math.floor($productFrame.width() / productWidth); // 한 번에 보이는 상품 개수
    const totalProducts = $products.length; // 총 상품 개수

    // 최대 이동할 수 있는 위치: 보이지 않는 상품들만큼 이동 가능
    const maxPosition = 10 - productWidth * (totalProducts - visibleCount);

    // 왼쪽 버튼 클릭 시
    $('.pointShop-leftBtn').click(function () {
        if (currentPosition < 10) { // 10px 기준으로 왼쪽 끝을 넘어가지 않도록 제한
            currentPosition += productWidth; // 한 상품 너비만큼 오른쪽으로 이동
            $productList.animate({left: currentPosition + 'px'}, 300); // 애니메이션으로 이동
        }
    });

    // 오른쪽 버튼 클릭 시
    $('.pointShop-rightBtn').click(function () {
        if (currentPosition > maxPosition) { // maxPosition을 넘어가지 않도록 제한
            currentPosition -= productWidth; // 한 상품 너비만큼 왼쪽으로 이동
            $productList.animate({left: currentPosition + 'px'}, 300); // 애니메이션으로 이동
        }
    });

    // 상품구매 클릭 시 갯수 검사 후 구매 가능 여부 확인
    $('.pointShop-buyBtn').click(function (e) {
        e.preventDefault(); // 기본 폼 제출 방지
        const quantityInput = $(this).siblings('div').find('.pointShop-buyEa'); // 버튼의 형제 요소에서 input 찾기
        const quantity = parseInt(quantityInput.val(), 10); // input 값 가져오기
        const productPrice = parseInt($(this).closest('.pointShop-productInfo').find('.pointShop-productPrice').text()) || 0; // 상품 가격 가져오기
        const totalCost = productPrice * quantity; // 총 필요 포인트 계산
        const productName = $(this).closest('.pointShop-productInfo').find('.pointShop-productName').text(); // 상품명 가져오기

        // 재고 확인
        const productStock = parseInt($(this).closest('.pointShop-productInfo').find('.pointShop-productStock').text().replace(/[^\d]/g, ''), 10); // 재고 수량 가져오기

        // 보유 포인트 확인
        const myPointText = $('.pointShop-myPoint').text(); // 서버에서 보유받은 텍스트 값을 가져옴
        const myPoint = parseInt(myPointText.replace(/[^\d]/g, ''), 10); // 숫자만 추출하고 정수로 변환

        // 유효성 검사
        if (quantity <= 0) {
            alert('0 이하는 구매할 수 없습니다');
        } else if (isNaN(quantity)) {
            alert('구매 갯수를 입력해 주세요');
        } else if (totalCost > myPoint) {
            alert('보유 포인트가 부족합니다. 구매를 취소합니다.');
        } else if (quantity > productStock) {
            alert('재고가 부족합니다. 구매 가능한 수량 이하로 입력해 주세요.');
        } else {
            const confirmPurchase = confirm(`정말로 구매 하시겠습니까?\n상품명: ${productName}\n갯수: ${quantity}개\n총 필요 포인트: ${totalCost} 포인트\n해당 포인트가 차감됩니다`);
            if (confirmPurchase) {
                // 실제 구매 로직
                $.ajax({
                    url: '/shop/buy', // 서버의 구매 처리 URL
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        productName: productName,
                        quantity: quantity,
                        totalCost: totalCost
                    }),
                    success: function (response) {
                        alert(`${productName} ${quantity}개 구매 완료되었습니다.\n상품코드는 문자로 전송됩니다.`);
                        location.reload();
                    },
                    error: function (xhr, status, error) {
                        alert('구매 중 오류가 발생했습니다. 다시 시도해 주세요.');
                    }
                });
            }
        }
    });
});
