// $(document).ready(function() {
//   // 등록 버튼 클릭 시 이벤트 처리
//   $('.honeyboardwrite-writeButton').on('click', function(e) {
//     e.preventDefault(); // 기본 동작(페이지 이동) 방지
//
//     // 제목과 에디터 내용 가져오기
//     const title = $('.honeyboardwrite-titleInput').val().trim();
//     const content = editor.getMarkdown().trim(); // ToastUI 에디터에서 작성된 내용 가져오기
//
//     // 제목 또는 내용이 비어있는지 확인
//     if (title === '' || content === '') {
//       alert("등록할 제목과 내용을 작성하세요.");
//       return false; // 아무 내용이 없으면 등록되지 않음
//     }
//
//     // 내용이 있을 때는 확인 창 띄우기
//     if (confirm("등록하시겠습니까?")) {
//       // 확인을 누르면 해당 페이지로 이동 (실제 등록 처리 로직 추가 가능)
//       window.location.href = './honeyBoardList.html';
//     }
//   });
// });
