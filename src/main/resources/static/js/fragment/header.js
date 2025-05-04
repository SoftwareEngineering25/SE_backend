$(document).ready(function () {
  function setupMenuToggle(menuId, listClass) {
    // 메뉴 클릭 시 하위 메뉴 토글
    $(menuId).on('click', function (event) {
      // 다른 모든 하위 메뉴 닫기
      $('.main-recipeList, .main-boardList, .main-myPageList').not(listClass).slideUp();

      // 현재 하위 메뉴 토글
      $(listClass).slideToggle();

      event.stopPropagation(); // 클릭 이벤트 전파 방지
    });
  }

  // 검색 버튼 클릭 이벤트
  $('.main-searchBar').on('submit', function (event) {
    let searchValue = $(this).find('input[name="searchValue"]').val(); // 입력값 가져오기

    if (!searchValue) { // 입력값이 없을 경우
      event.preventDefault(); // 폼 제출 방지
      alert('검색어를 입력하세요'); // 경고 메시지 표시
    }
  });

  // 각 메뉴에 대해 함수 호출
  setupMenuToggle('.main-recipe', '.main-recipeList');
  setupMenuToggle('.main-board', '.main-boardList');
  setupMenuToggle('.main-myPage', '.main-myPageList');

  // 다른 곳 클릭 시 모든 하위 메뉴 닫기
  $(document).on('click', function () {
    $('.main-recipeList, .main-boardList, .main-myPageList').slideUp(); // 모든 하위 메뉴 닫기
  });

  // header-goLogin 클래스를 가진 요소에 클릭 이벤트 추가
  $('.header-goLogin').on('click', function (event) {
    event.preventDefault(); // 기본 링크 이동 방지
    alert('로그인이 필요합니다'); // 알림 메시지 표시
    window.location.href = $(this).attr('href'); // 로그인 페이지로 이동
  });

  // 로그아웃 안내창
  $('.main-logout').on('click', function (e) {
    e.preventDefault(); // 기본 폼 제출 방지
    let check = confirm('로그아웃 하시겠습니까?');
    if (check) {
      alert('로그아웃 되었습니다.')
      // 로그아웃 요청을 보내기 위해 폼을 제출
      $(this).closest('form').submit();
    }
  });
});

//a태그 post로 전송
function postRequest(url) {
  const form = document.createElement('form');
  form.method = 'post';
  form.action = url;
  document.body.appendChild(form);
  form.submit();
}
