// 추천 이미지를 눌렀을때 (따봉 이미지) 추천수 올라갔다 내려가는 기능
document.addEventListener("DOMContentLoaded", function () {
    const recommendImg = document.getElementById("recommendImg");
    const boardNumber = document.getElementById("boardNumber").value;

    let isActive = localStorage.getItem(`recommend-active-${boardNumber}`) === 'true';
    recommendImg.classList.toggle("recommend-active", isActive);
    recommendImg.classList.toggle("recommend-inactive", !isActive);

    let isRequestInProgress = false;

    const handleClick = function () {
        if (isRequestInProgress) return;

        isRequestInProgress = true;
        const url = isActive ? "/board/goodMinus" : "/board/goodPlus";

        fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({boardNumber: boardNumber})
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("요청 실패: " + response.statusText);
                }

                // 추천 상태 업데이트 및 localStorage 저장
                isActive = !isActive;
                recommendImg.classList.toggle("recommend-active", isActive);
                recommendImg.classList.toggle("recommend-inactive", !isActive);
                localStorage.setItem(`recommend-active-${boardNumber}`, isActive);

                // 요청 성공 시 페이지 새로 고침
                location.reload();
            })
            .catch(error => {
                console.error("Error:", error);
                alert("요청 처리 중 오류가 발생했습니다.");
            })
            .finally(() => {
                // 요청 완료 후 상태 초기화
                isRequestInProgress = false;
            });
    };

    recommendImg.addEventListener("click", handleClick);
});


// 댓글 수정 버튼을 눌렀을 때 실행되는 함수
document.querySelectorAll('.honeyBoardDetail-editBtn').forEach(function (button) {
    button.addEventListener('click', function () {
        let commentContainer = button.closest('.honeyBoardDetail-comment'); // 부모 댓글 컨테이너 선택
        let commentText = commentContainer.querySelector('.honeyBoardDetail-commentText'); // 댓글 텍스트 선택
        let editInput = commentContainer.querySelector('.honeyBoardDetail-commentInput'); // 수정 입력창 선택
        let editTextarea = commentContainer.querySelector('textarea'); // textarea 선택
        let buttonGroup = commentContainer.querySelector('.honeyBoardDetail-buttonGroup'); // 버튼 그룹 선택

        // 댓글 원본 텍스트를 data-original-text 속성에 저장
        commentText.setAttribute('data-original-text', commentText.innerText);

        // 기존 댓글 텍스트를 textarea에 삽입
        editTextarea.value = commentText.innerText;

        // 댓글 텍스트와 버튼 그룹을 숨기고 수정창을 보이게 함
        commentText.style.display = 'none';
        buttonGroup.style.display = 'none'; // 버튼 그룹 숨김
        editInput.style.display = 'block'; // 수정 입력창 보임
    });
});

// 댓글 수정 취소 버튼을 눌렀을 때 실행되는 함수
document.querySelectorAll('.honeyBoardDetail-cancelBtn').forEach(function (button) {
    button.addEventListener('click', function () {
        let boardNumber = document.querySelector('[name="boardNumber"]').value;  // boardNumber 값 가져오기
        window.location.href = '/board/honeyBoardDetail?boardNumber=' + boardNumber;  // detail 페이지로 리다이렉트
    });
});

// 댓글 저장 버튼을 눌렀을 때 실행되는 함수
document.querySelectorAll('.honeyBoardDetail-saveBtn').forEach(function (button) {
    button.addEventListener('click', function () {
        let commentContainer = button.closest('.honeyBoardDetail-comment');
        let replyNumber = commentContainer.querySelector('input[name="replyNumber"]').value;
        let editTextarea = commentContainer.querySelector('textarea').value;

        if (confirm("수정하시겠습니까?")) {
            // AJAX 요청을 통해 서버에 수정된 댓글을 전송
            $.ajax({
                type: 'POST',
                url: '/board/honeyUpdateReply',  // 서버의 댓글 수정 요청 URL
                data: {
                    replyNumber: replyNumber,
                    replyText: editTextarea
                },
                success: function () {
                    // 페이지를 리다이렉트하여 수정된 댓글을 다시 불러옴
                    window.location.reload(); // 페이지를 새로고침하여 업데이트된 내용 표시
                },
                error: function () {
                    alert("댓글 수정에 실패했습니다.");
                }
            });
        }
    });
});

// 댓글 등록 버튼 클릭 시
function submitComment() {
    const confirmation = confirm("댓글을 등록하시겠습니까?");
    if (!confirmation) return;
    alert("댓글이 등록되었습니다.");
}

// 댓글 삭제 버튼을 눌렀을 때 실행되는 함수
document.querySelectorAll('.honeyBoardDetail-deleteBtn').forEach(function (button) {
    button.addEventListener('click', function () {
        let replyNumber = button.getAttribute('data-reply-number');
        deleteHoneyComment(replyNumber);
    });
});

// 댓글 삭제 함수
function deleteHoneyComment(replyNumber) {
    if (confirm("정말로 이 댓글을 삭제하시겠습니까?")) {
        // AJAX 요청을 통해 서버에 댓글 삭제 요청
        $.ajax({
            type: 'POST',
            url: '/board/honeyDeleteReply', // 서버의 댓글 삭제 요청 URL
            data: {replyNumber: replyNumber},
            success: function () {
                alert("댓글이 삭제되었습니다.");
                // 페이지를 새로고침하여 변경 사항 반영
                window.location.reload();
            },
            error: function () {
                alert("댓글 삭제에 실패했습니다.");
            }
        });
    }
}


$(function () {
    const items = $('.honeyBoardDetail-comment'); // 게시글 항목 선택

    // 처음 20개 항목만 보이게 하고 나머지는 숨김
    items.hide().slice(0, 5).show(); // 첫 5개 항목만 표시

    // 페이지네이션 설정
    const container = $('#pagination');
    container.pagination({
        dataSource: items.toArray(), // 게시글 항목을 배열로 변환
        pageSize: 5, // 한 페이지에 보여줄 항목 수 (20개)
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


function confirmDelete() {
    return confirm("정말 삭제하시겠습니까?");
}




