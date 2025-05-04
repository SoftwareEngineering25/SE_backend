$(document).ready(function () {
    // 페이지 로드 시, sessionStorage에서 현재 active 상태를 가져옴
    const activeButton = sessionStorage.getItem('activeButton') || 'latest'; // 기본값은 'latest'

    // 해당하는 버튼에 active 클래스 추가
    if (activeButton === 'latest') {
        $('.chatBotRecipeList-secondButton').addClass('active');
    } else if (activeButton === 'recommend') {
        $('.chatBotRecipeList-thirdButton').addClass('active');
    }

    // 버튼 클릭 이벤트 설정
    $('.chatBotRecipeList-secondButton').click(function () {
        // 모든 버튼에서 active 클래스 제거
        $('.chatBotRecipeList-rightButton button').removeClass('active');
        // 클릭된 버튼에 active 클래스 추가
        $(this).addClass('active');
        // sessionStorage에 상태 저장
        sessionStorage.setItem('activeButton', 'latest');
    });

    $('.chatBotRecipeList-thirdButton').click(function () {
        $('.chatBotRecipeList-rightButton button').removeClass('active');
        $(this).addClass('active');
        sessionStorage.setItem('activeButton', 'recommend');
    });
});

$(function () {
    const items = $('.chatBotRecipeList-outbox .chatBotRecipeList-listbox'); // 게시글 항목 선택

    // 처음 20개 항목만 보이게 하고 나머지는 숨김
    items.hide().slice(0, 20).show(); // 첫 20개 항목만 표시

    // 페이지네이션 설정
    const container = $('#pagination');
    container.pagination({
        dataSource: items.toArray(), // 게시글 항목을 배열로 변환
        pageSize: 20, // 한 페이지에 보여줄 항목 수 (20개)
        callback: function (data, pagination) {
            items.hide(); // 기존에 보여진 항목 숨김
            $.each(data, function (index, item) {
                $(item).show(); // 현재 페이지에 해당하는 항목만 표시
            });
        }
    });

    // 처음 로드 시 첫 번째 페이지의 항목만 보여주기
    container.pagination('goToPage', 1);
});


