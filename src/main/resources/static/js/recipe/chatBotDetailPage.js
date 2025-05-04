
// 댓글 등록 버튼 클릭 시
function submitComment() {
  const confirmation = confirm("댓글을 등록하시겠습니까?");
  if (!confirmation) return;
  alert("댓글이 등록되었습니다.");
}

// 추천 이미지 클릭 이벤트 추가
document.addEventListener("DOMContentLoaded", function () {
  const recommendImg = document.getElementById("recommendImg");
  const recipeNumber = document.getElementById("recipeNumber").value;

  let isActive = localStorage.getItem(`recommend-active-${recipeNumber}`) === 'true';
  recommendImg.classList.toggle("recommend-active", isActive);
  recommendImg.classList.toggle("recommend-inactive", !isActive);

  let isRequestInProgress = false;

  const handleClick = function () {
    if (isRequestInProgress) return;

    isRequestInProgress = true;
    const url = isActive ? "/recipe/ChatBotgoodMinus" : "/recipe/ChatBotGoodPlus";

    fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ recipeNumber: recipeNumber })
    })
        .then(response => {
          if (!response.ok) {
            throw new Error("요청 실패: " + response.statusText);
          }

          // 추천 상태 업데이트 및 localStorage 저장
          isActive = !isActive;
          recommendImg.classList.toggle("recommend-active", isActive);
          recommendImg.classList.toggle("recommend-inactive", !isActive);
          localStorage.setItem(`recommend-active-${recipeNumber}`, isActive);

          // 요청 성공 시 페이지 새로 고침
          location.reload();
        })
        .catch(error => {
          console.error("Error:", error);
          alert("요청 처리 중 오류가 발생했습니다.");
        })
        .finally(() => {
          isRequestInProgress = false;
        });
  };

  recommendImg.addEventListener("click", handleClick);
});

// 댓글 수정 버튼을 눌렀을 때 실행되는 함수
document.querySelectorAll('.chatBotDetailPage-editBtn').forEach(function (button)  {
  button.addEventListener('click',function ()  {
    let commentContainer = button.closest('.chatBotDetailPage-comment');
    let commentText = commentContainer.querySelector('.chatBotDetailPage-commentText');
    let editInput = commentContainer.querySelector('.chatBotDetailPage-commentInput');
    let editTextarea = editInput.querySelector('textarea');
    let buttonGroup = commentContainer.querySelector('.chatBotDetailPage-buttonGroup');

    commentText.setAttribute('data-original-text', commentText.innerText);
    editTextarea.value = commentText.innerText;

    commentText.style.display = 'none';
    buttonGroup.style.display = 'none';
    editInput.style.display = 'block';
  });
});

// 댓글 수정 취소 버튼을 눌렀을 때 실행되는 함수
document.querySelectorAll('.chatBotDetailPage-cancelBtn').forEach(function (button)  {
  button.addEventListener('click', function ()  {
    let recipeNumber = document.querySelector('[name="recipeNumber"]').value;
    window.location.href = '/recipe/chatBotDetailPage?recipeNumber=' + recipeNumber;
  });
});

// 댓글 저장 버튼을 눌렀을 때 실행되는 함수
document.querySelectorAll('.chatBotDetailPage-saveBtn').forEach(function (button) {
  button.addEventListener('click', function ()  {
    let commentContainer = button.closest('.chatBotDetailPage-comment');
    let replyNumber = commentContainer.querySelector('input[name="replyNumber"]').value;
    let editTextarea = commentContainer.querySelector('textarea').value;

    console.log(commentContainer);
    console.log(replyNumber);
    console.log(editTextarea);
    if (confirm("수정하시겠습니까?")) {
      $.ajax({
        type: 'POST',
        url: '/recipe/updateChatBotReply',
        data: {
          replyNumber: replyNumber,
          replyText: editTextarea
        },

        success: function ()  {
          alert("댓글 수정이 완료되었습니다.");
          window.location.reload();
        },
        error: function ()  {
          alert("댓글 수정에 실패했습니다.");
        }
      });
    }
  });
});

// 댓글 삭제 버튼을 눌렀을 때 실행되는 함수
document.querySelectorAll('.chatBotDetailPage-deleteBtn').forEach(function (button)  {
  button.addEventListener('click', function ()  {
    let replyNumber = button.getAttribute('data-reply-number');
    deleteComment(replyNumber);
  });
});

// 댓글 삭제 함수
function deleteComment(replyNumber) {
  if (confirm("정말로 이 댓글을 삭제하시겠습니까?")) {
    $.ajax({
      type: 'POST',
      url: '/recipe/deleteChatBotReply',
      data: { replyNumber: replyNumber },
      success: function ()  {
        alert("댓글이 삭제되었습니다.");
        //페이지를 새로고침하여 변경 사항 반영
        window.location.reload();
      },
      error: function ()  {
        alert("댓글 삭제에 실패했습니다.");
      }
    });
  }
}

// 찜
$(document).ready(function() {
  const recipeNumber = $('#recipeNumber').val();

  $('#heartImage2').on('click', function() {
    $(this).hide(); // "싫어요" 이미지 숨기기
    $('#heartImage1').show(); // "좋아요" 이미지 보이기

    // AJAX 요청
    $.ajax({
      url: '/recipe/like', // URL 확인
      method: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({ recipeNumber: recipeNumber }),
      success: function(response) {
        console.log('좋아요 처리 성공:', response);

      },
      error: function(xhr, status, error) {
        console.error('좋아요 처리 실패:', error);

      }
    });
  });

  $('#heartImage1').on('click', function() {
    $(this).hide(); // "좋아요" 이미지 숨기기
    $('#heartImage2').show(); // "싫어요" 이미지 보이기

    // AJAX 요청
    $.ajax({
      url: '/recipe/unLike', // URL 확인
      method: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({ recipeNumber: recipeNumber }),
      success: function(response) {
        console.log('싫어요 처리 성공:', response);
      },
      error: function(xhr, status, error) {
        console.error('싫어요 처리 실패:', error);
      }
    });
  });
});

$(function () {
  const items = $('.chatBotDetailPage-comment'); // 게시글 항목 선택

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
  // 첫 번째 알림창: 삭제 확인
  if (confirm("챗봇 레시피를 삭제하시겠습니까?")) {
    // 두 번째 알림창: 삭제 완료 메시지
    alert("챗봇레시피가 삭제되었습니다.");
    return true; // 폼 제출 진행
  } else {
    return false; // 폼 제출 중단
  }
}


