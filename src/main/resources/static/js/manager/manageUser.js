
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


  // 'memberOut' 버튼에 클릭 이벤트 리스너 추가
  document.getElementById('memberOut').addEventListener('click', function() {
    // 관리 페이지의 체크된 체크박스들을 찾아 그 value 값(사용자 번호)을 배열로 수집
    const selectedUserNumbers = Array.from(document.querySelectorAll('#manage-memberUl input[type="checkbox"]:checked'))
        .filter(checkbox => checkbox.value !== "전체선택") // 전체 선택 체크박스를 제외
        .map(checkbox => checkbox.value); // 체크된 체크박스의 value 값만 추출하여 배열로 만듦

    // 선택된 사용자가 없을 경우 경고 메시지를 출력하고 함수 종료
    if (selectedUserNumbers.length === 0) {
      alert("탈퇴할 사용자를 선택하세요."); // 선택되지 않은 경우 알림 메시지 표시
      return;
    }

    // AJAX 요청을 사용해 선택된 사용자 번호 리스트를 JSON 형태로 서버에 전송
    $.ajax({
      type: "DELETE",
      url: "/manager/userOut", // 서버의 userOut API 엔드포인트 URL
      contentType: "application/json", // 전송 데이터 형식을 JSON으로 설정
      data: JSON.stringify({ userNumber: selectedUserNumbers }), // 선택된 사용자 번호 배열을 JSON으로 변환하여 전송
      success: function(response) {
        // 성공 시 알림 메시지 표시하고 관리자 페이지로 리다이렉션
        alert("선택된 사용자가 탈퇴 처리되었습니다.");
        window.location.href = "/manager/manageUser"; // 탈퇴 후 사용자 관리 페이지로 이동
      },
      error: function() {
        // 실패 시 오류 알림 메시지 표시
        alert("탈퇴 처리에 실패했습니다.");
      }
    });
  });



  document.getElementById('memberPause').addEventListener('click', function() {
    // 체크된 체크박스들의 value 값 수집
    const selectedUserNumbers = Array.from(document.querySelectorAll('#manage-memberUl input[type="checkbox"]:checked'))
        .filter(checkbox => checkbox.value !== "전체선택") // 전체 선택 체크박스를 제외
        .map(checkbox => checkbox.value);

    if (selectedUserNumbers.length === 0) {
      alert("정지할 사용자를 선택하세요.");
      return;
    }

    document.getElementById('title-check').addEventListener('change', function() {
      const checkboxes = document.querySelectorAll('#manage-memberUl input[type="checkbox"]');
      checkboxes.forEach(checkbox => {
        checkbox.checked = this.checked; // 전체 선택 체크박스의 상태에 따라 개별 체크박스 체크
      });
    });

    // AJAX 요청을 통해 체크된 userNumber 값들을 서버에 전송
    $.ajax({
      type: "PUT",
      url: "/manager/userPause",
      contentType: "application/json",
      data: JSON.stringify({ userNumber: selectedUserNumbers }), // 배열을 JSON으로 변환
      success: function(response) {
        alert("선택된 사용자가 정지 처리되었습니다.");
        window.location.href = "/manager/manageUser"; // 리다이렉션
      },
      error: function() {
        alert("정지 처리에 실패했습니다.");
      }
    });
  });

