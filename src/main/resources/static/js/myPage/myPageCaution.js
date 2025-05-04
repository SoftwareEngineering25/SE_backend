document.getElementById("myPageDeleteForm").addEventListener("submit", function(event) {
    const checkbox = document.getElementById("myPage-input");

    // 안내 사항 확인 여부 검사
    if (!checkbox.checked) {
        alert("안내 사항에 동의해야 회원 탈퇴가 가능합니다.");
        event.preventDefault();
    } else {
        // 탈퇴 확인 메시지
        const isConfirmed = confirm("정말 탈퇴하시겠습니까?\n탈퇴 시, 계정은 삭제되며 포인트는 복구되지 않습니다.");

        if (!isConfirmed) {
            event.preventDefault();
        }
    }
});
