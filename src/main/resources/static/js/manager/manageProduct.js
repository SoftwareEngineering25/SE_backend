$(function () {
  const items = $('#manage-memberUl li:not(#manage-memberTitle)'); // 제목을 제외한 모든 li 요소 선택
  items.hide().slice(0, 10).show(); // 처음 10개 항목만 보이게 하고 나머지는 숨김

  // 전체 선택 체크박스 및 개별 체크박스 선택자 설정
  const masterCheckbox = $('#title-check');
  const checkboxes = $('#manage-memberUl li input[type="checkbox"]:not(#title-check)');

  // 전체 선택 체크박스 클릭 시 모든 개별 체크박스 체크/해제
  function selectAll() {
    const isChecked = masterCheckbox.prop('checked');
    checkboxes.prop('checked', isChecked);
  }

  // 페이지 전환 시 개별 체크박스의 선택 상태에 따라 전체 선택 체크박스 상태 업데이트
  function updateMasterCheckbox() {
    const allChecked = checkboxes.length === checkboxes.filter(':checked').length;
    masterCheckbox.prop('checked', allChecked);
  }

  // 페이지네이션 설정
  const container = $('#pagination');
  container.pagination({
    dataSource: items.toArray(),
    pageSize: 10,
    callback: function (data) {
      $('#manage-memberUl').children('li:not(#manage-memberTitle)').hide();
      $.each(data, function (index, item) {
        $(item).show();
      });
      updateMasterCheckbox(); // 페이지 변경 시 전체 선택 체크박스 상태 업데이트
    }
  });

  masterCheckbox.on('click', selectAll); // 전체 선택 클릭 시 이벤트
  checkboxes.on('change', updateMasterCheckbox); // 개별 체크박스 클릭 시 이벤트

  container.pagination('goToPage', 1); // 처음 로드 시 첫 페이지로 이동
});


// 삭제 기능 - 전체 선택 체크박스 제외한 값 전송
document.getElementById('memberOut').addEventListener('click', function() {
  const selectedUserNumbers = Array.from(document.querySelectorAll('#manage-memberUl input[type="checkbox"]:checked'))
      .filter(checkbox => checkbox.value !== "전체선택") // "전체선택" 필터링
      .map(checkbox => checkbox.value);

  if (selectedUserNumbers.length === 0) {
    alert("삭제할 댓글을 선택하세요.");
    return;
  }

  $.ajax({
    type: "DELETE",
    url: "/manager/deleteProduct",
    contentType: "application/json",
    data: JSON.stringify({ productName: selectedUserNumbers }),
    success: function(response) {
      alert(response);
      window.location.href = "/manager/manageProduct";
    },
    error: function() {
      alert("삭제 처리에 실패했습니다.");
    }
  });
});


// 수정 기능 - 전체 선택 체크박스 제외하고 하나의 선택만 허용
const updateButton = document.getElementById("memberUpdate");
if (updateButton) {
  updateButton.addEventListener("click", redirectToUpdate);
}

function redirectToUpdate() {
  const checkedProducts = document.querySelectorAll('input[type="checkbox"]:checked');
  const selectedItems = Array.from(checkedProducts).filter(item => item.value !== "전체선택");

  if (selectedItems.length === 1) {
    const productName = selectedItems[0].value;
    window.location.href = `/manager/showProduct?productName=${encodeURIComponent(productName)}`;
  } else if (selectedItems.length > 1) {
    alert("하나의 상품만 선택해주세요.");
  } else {
    alert("수정할 상품을 선택해주세요.");
  }
}
