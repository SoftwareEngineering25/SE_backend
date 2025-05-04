$(function() {
    const items = $('.myPage-myPointTitleMainOne ul li'); // 게시글 항목 선택

    // 게시글 수가 10개 이하인 경우 페이지네이션 처리
    if (items.length <= 10) {
        items.show(); // 모든 항목 표시
        return; // 페이지네이션 초기화 중지
    }

    // 처음 10개 항목만 보이게 하고 나머지는 숨김
    items.hide().slice(0, 10).show(); // 첫 10개 항목만 표시

    // 페이지네이션 설정
    const container = $('#pagination');
    const pageSize = 10; // 한 페이지에 보여줄 항목 수 (첫 페이지 10개)

    container.pagination({
        dataSource: items.toArray(), // 게시글 항목을 배열로 변환
        pageSize: pageSize,
        callback: function(data, pagination) {
            items.hide(); // 기존에 보여진 항목 숨김
            $.each(data, function(index, item) {
                $(item).show(); // 현재 페이지에 해당하는 항목만 표시
            });
        }
    });

    // 처음 로드 시 첫 번째 페이지의 항목만 보여주기
    container.pagination('goToPage', 1);
});