// document.addEventListener('DOMContentLoaded', function () {
//     const modifyButton = document.querySelector('.chatBotRecipeModify-writeButton'); // 수정 버튼 선택
//
//     modifyButton.addEventListener('click', function (event) {
//         event.preventDefault(); // 기본 링크 동작 막기
//
//         // alert 창 띄우기
//         const isConfirmed = confirm('수정하시겠습니까?');
//         if (isConfirmed) {
//             // '확인' 버튼 클릭 시
//             alert('수정 완료되었습니다.');
//             window.location.href = './chatBotDetailPage.html'; // 수정 완료 후 이동할 페이지
//         } else {
//             // '취소' 버튼 클릭 시
//             window.location.href = './chatBotDetailPage.html'; // 취소 시 이동할 페이지
//         }
//     });
// });