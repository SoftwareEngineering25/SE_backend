// -- 찜 삭제 -- //
$(document).on('click', '.myPage-pictureListDelete', function() {
    // 삭제 확인 창 띄우기
    if (confirm('정말 찜 목록에서 삭제하시겠습니까?')) {
        // 클릭된 버튼에서 recipeNumber와 userNumber 가져오기
        const recipeNumber = $(this).data('recipe-number'); // 데이터 속성에서 recipeNumber 값 가져오기
        const userNumber = $(this).data('user-number'); // 데이터 속성에서 userNumber 값 가져오기 (세션에서 자동 설정됨)

        // 서버로 보낼 DTO 객체 생성
        const userSteamDTO = {
            recipeNumber: recipeNumber, // 숫자값
            userNumber: userNumber     // 숫자값 (세션에서 자동으로 설정되므로 필요하지 않지만 보내는 형태로 설정)
        };

        // AJAX 요청 보내기
        $.ajax({
            url: '/myPage/steamedDelete',  // 요청 URL
            type: 'DELETE',                // 요청 방식 (DELETE)
            contentType: 'application/json', // 요청 헤더에 JSON 데이터 타입 설정
            data: JSON.stringify(userSteamDTO), // DTO 객체를 JSON 문자열로 변환하여 전송
            success: function() {
                // 성공 시 메시지 출력 후 페이지 새로고침
                alert('찜 목록에서 삭제되었습니다.');
                location.reload();  // 페이지 새로고침
            },
            error: function(xhr, status, error) {
                // 오류 발생 시 오류 메시지 출력
                alert('삭제에 실패했습니다: ' + error);
            }
        });
    }
});



$(function() {
    const items = $('.myPage-pictureprideOne'); // 게시글 항목 선택

    // 처음 9개 항목만 보이게 하고 나머지는 숨김
    items.hide().slice(0, 9).show(); // 첫 9개 항목만 표시

    // 페이지네이션 설정
    const container = $('#pagination');
    container.pagination({
        dataSource: items.toArray(), // 게시글 항목을 배열로 변환
        pageSize: 9, // 한 페이지에 보여줄 항목 수 (9개씩)
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