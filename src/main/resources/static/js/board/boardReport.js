// document.addEventListener("DOMContentLoaded", function() {
//
//   const reportSuccessElement = document.getElementById("reportSuccess");
//   if (reportSuccessElement) {
//     const reportSuccess = reportSuccessElement.value === 'true'; // 문자열을 boolean으로 변환
//
//     if (reportSuccess) {
//       alert("신고가 완료되었습니다.");
//     }
//   }
//
//   // 체크박스 상태 확인
//   const isChecked = $('input[name="reason"]:checked').length > 0;
//
//   if (!isChecked) {
//     // 체크된 신고 사유가 없을 경우 경고 메시지
//     alert("신고사유를 체크해주세요.");
//   }
// });

document.addEventListener("DOMContentLoaded", function() {
    const reportForm = document.querySelector("form"); // 폼 요소
    const reasonRadios = document.querySelectorAll("input[name='reason']"); // 신고 사유 라디오 버튼들
    const reportButton = document.querySelector(".boardreportright-button"); // 신고 버튼

    reportButton.addEventListener("click", function(event) {
        // 라디오 버튼 중 체크된 것이 있는지 확인
        let reasonSelected = false;
        reasonRadios.forEach(radio => {
            if (radio.checked) {
                reasonSelected = true;
            }
        });

        if (!reasonSelected) {
            // 사유가 선택되지 않은 경우 경고 표시 및 제출 중지
            event.preventDefault();
            alert("신고 사유를 선택해 주세요.");
            return;
        } else {
            // 사유가 선택된 경우 확인 메시지 표시
            const confirmReport = confirm("정말 신고하시겠습니까?");
            if (!confirmReport) {
                // 확인하지 않은 경우 제출 중지
                event.preventDefault();
            }
        }
    });
});


