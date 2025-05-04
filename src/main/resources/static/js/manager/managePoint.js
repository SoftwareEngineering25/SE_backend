
$(function () {
  const items = $('#manage-memberUl li:not(#manage-memberTitle)'); // 제목을 제외한 모든 li 요소 선택

  // 처음 10개 항목만 보이게 하고 나머지는 숨김
  items.hide().slice(0, 10).show();

  // 전체 선택 체크박스
  const masterCheckbox = $('#title-check'); // 전체 선택 체크박스
  const checkboxes = $('#manage-memberUl li input[type="checkbox"]:not(#title-check)'); // 전체선택 체크박스를 제외한 모든 체크박스 선택

  // 전체 선택 체크박스 클릭 시
  function selectAll() {
    // 전체 선택 체크박스가 체크되면 모든 개별 체크박스를 체크, 해제되면 모든 개별 체크박스를 해제
    checkboxes.prop('checked', masterCheckbox.prop('checked'));
  }

  // 개별 체크박스 상태에 따라 전체 선택 체크박스 상태 업데이트
  function updateMasterCheckbox() {
    // 모든 개별 체크박스가 체크되어 있는지 확인
    const allChecked = checkboxes.length === checkboxes.filter(':checked').length;
    masterCheckbox.prop('checked', allChecked);
  }

  // 페이지네이션 설정
  const container = $('#pagination');
  container.pagination({
    dataSource: items.toArray(), // li 요소를 배열로 변환
    pageSize: 10, // 한 페이지에 보여줄 항목 수
    callback: function (data) {
      $('#manage-memberUl').children('li:not(#manage-memberTitle)').hide(); // 기존 항목 숨김
      $.each(data, function (index, item) {
        $(item).show(); // 현재 페이지의 항목 표시
      });
      // 페이지가 변경될 때 전체 선택 체크박스 상태 업데이트
      updateMasterCheckbox();
    }
  });

  // 전체 선택 체크박스 클릭 시 selectAll() 함수 실행
  masterCheckbox.on('click', selectAll);

  // 각 개별 체크박스 클릭 시 updateMasterCheckbox 함수 실행
  checkboxes.on('change', updateMasterCheckbox);

  // 처음 로드 시 첫 번째 페이지의 항목만 보여주기
  container.pagination('goToPage', 1);
});



$(document).ready(function() {
  $('#manager-select select').on('change', function() {
    const selectedMonth = $(this).val();
    if (selectedMonth === "0") {
      window.location.href = "/manager/managePoint"; // 전체 조회
    } else {
      window.location.href = `/manager/managePoint?month=${selectedMonth}`; // 선택한 월로 조회
    }
  });
});

document.getElementById('memberOut').addEventListener('click', function() {
  // 체크된 체크박스들의 value 값 수집
  const selectedUserNumbers = Array.from(document.querySelectorAll('#manage-memberUl input[type="checkbox"]:checked'))
      .map(checkbox => checkbox.value);

  if (selectedUserNumbers.length === 0) {
    alert("삭제할 댓글을 선택하세요.");
    return;
  }

  // AJAX 요청을 통해 체크된 userNumber 값들을 서버에 전송
  $.ajax({
    type: "DELETE",
    url: "/manager/pointDelete",
    contentType: "application/json",
    data: JSON.stringify({ pointNumber: selectedUserNumbers }), // 배열을 JSON으로 변환
    success: function(response) {
      alert("선택된 포인트가 삭제 처리되었습니다.");
      window.location.href = "/manager/managePoint"; // 리다이렉션
    },
    error: function() {
      alert("삭제 처리에 실패했습니다.");
    }
  });
});

document.getElementById('memberPause').addEventListener('click', function() {
  // 체크된 체크박스들의 value 값 수집
  const selectedUserNumbers = Array.from(document.querySelectorAll('#manage-memberUl input[type="checkbox"]:checked'))
      .map(checkbox => checkbox.value);

  if (selectedUserNumbers.length === 0) {
    alert("삭제할 댓글을 선택하세요.");
    return;
  }

  // AJAX 요청을 통해 체크된 userNumber 값들을 서버에 전송
  $.ajax({
    type: "PUT",
    url: "/manager/takePoint",
    contentType: "application/json",
    data: JSON.stringify({ pointNumber: selectedUserNumbers }), // 배열을 JSON으로 변환
    success: function(response) {
      alert("선택된 포인트가 회수 처리되었습니다.");
      window.location.href = "/manager/managePoint"; // 리다이렉션
    },
    error: function() {
      alert("회수 처리에 실패했습니다.");
    }
  });
});