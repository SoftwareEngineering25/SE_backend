// -- 환경기여 랭킹 물음표 -- //
$(".userRankquestion").on("mouseenter", () => {
    $(".userRankAnswer").css('display', 'block');
})
$(".userRankquestion").on("mouseleave", () => {
    $(".userRankAnswer").css('display', 'none');
})



// ----- 랭킹 페이지 네이션 ---- //

$(function() {
    const items = $('.userRankTypeOne'); // 사용자 랭크 항목 선택
    const pageSize = 10; // 한 페이지에 보여줄 항목 수

    // 처음 10개 항목만 보이게 하고 나머지는 숨김
    items.hide().slice(0, pageSize).show(); // 첫 10개 항목만 표시

    // 페이지네이션 설정
    const container = $('#pagination');

    container.pagination({
        dataSource: items.toArray(), // 사용자 랭크 항목을 배열로 변환
        pageSize: pageSize,
        callback: function(data) {
            items.hide(); // 기존에 보여진 항목 숨김
            $.each(data, function(index, item) {
                $(item).show(); // 현재 페이지에 해당하는 항목만 표시
            });
        }
    });

    // 처음 로드 시 첫 번째 페이지의 항목만 보여주기
    container.pagination('goToPage', 1);
});