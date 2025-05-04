$(document).ready(function () {
    $('#nangjangbot-chating').submit(function (e) {
        e.preventDefault();

        // 유저 메세지 가져오기
        let userMsg = $('#nangjangbot-sendMsg').val();
        if (userMsg.trim() === "") {
            return; // 공백 입력은 무시
        }

        // 인풋창 비활성화
        $('#nangjangbot-sendMsg').prop('disabled', true);

        // 유저 메세지를 화면에 바로 추가
        $('#nangjangbot-conversationContainer').append(
            `<div class="nangjangbot-myMsg">
                 <div class="userMsgBox">${userMsg}</div>
            </div>`
        );

        // 로딩중 표시
        $('#loading-spinner').css('display', 'flex');

        //sessionNumber 가져오기
        let sessionNumber = $('#sessionNumber').val();

        // AJAX 요청을 통해 챗봇 응답 가져오기
        $.ajax({
            // ****GET요청에서는 JSON 형식의 데이터를 본문에 포함할 수 없고 쿼리 매개변수로 전달할 수 없음
            type: "POST",
            url: "/api/chatbot/chat",
            contentType: "application/json",
            data: JSON.stringify({
                message: userMsg,
                sessionNumber: sessionNumber
            }),
            success: function (response) {
                $('#loading-spinner').css('display', 'none'); // 로딩중 숨김

                //처음 보냈던 sessionNumber를 응답에 같이 담겨온 세션 번호로 저장
                if (response.sessionNumber != 0) {
                    // AJAX 응답에서 sessionNumber 업데이트
                    $('#sessionNumber').val(response.sessionNumber);
                }

                // 챗봇 응답을 화면에 추가
                $('#nangjangbot-conversationContainer').append(`
                        <img src="/image/nangjangbot/apple.png" class="nangjangbot-mascot" alt="챗봇 마스코트">
                        <div class="botMsgBox">${response.reply}</div>
                `);

                // #temporary 요소에 데이터 추가
                const temporaryDiv = $('#temporary');

                // #temporary 요소가 비어 있을 때만 데이터 추가
                if (temporaryDiv.is(':empty') && sessionNumber == 0) {
                    $('#temporary').append(`
                        <div class="nangjangbot-lastChat">
                            <span class="nangjangbot-lastChatTitle">${response.sessionTitle}</span>
                            <span class="nangjangbot-lastChatDate">${response.createDate}</span>
                            <input type="hidden" class="lastChatSession" value="${response.sessionNumber}"/>
                            <button class="nangjangbot-imgFrame">
                                <img src="/image/nangjangbot/delete.png">
                            </button>
                        </div>
                    `);
                }

                $('html, body').animate({
                    scrollTop: $(document).height()  // 페이지 전체 높이로 스크롤
                }, 500);

                // 인풋창 다시 활성화하고 포커스 설정
                $('#nangjangbot-sendMsg').prop('disabled', false).focus();

            },
            error: function () {
                // 로딩중 숨김
                $('#loading-spinner').css('display', 'none');
                alert("죄송합니다. 서버 오류가 발생했습니다. 다시 시도해주세요.");

                // 인풋창 다시 활성화하고 포커스 설정
                $('#nangjangbot-sendMsg').prop('disabled', false).focus();
            }
        });

        // 인풋창 비우기
        $('#nangjangbot-sendMsg').val('');

        // 유저가 처음 메세지를 입력했을 때 숨길 요소 설정
        $('.nangjangbot-pageTitle, #nangjangbot-text').css('display', 'none');
        $('.nangjangbot-block, .nangjangbot-myMsg, .nangjangbot-chatBotMsg').css('display', 'block');

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


const sidebar = document.getElementById('nangjangbot-sideBar');
const button = document.getElementById('nangjangbot-btnContainer');
const closeBtn = document.getElementById('nangjangbot-closeBtn');
let isOpen = true; // 사이드바가 열려있는 상태

button.addEventListener('click', () => {
    if (isOpen) {
        sidebar.style.left = '-350px'; // 사이드메뉴 닫기
    } else {
        sidebar.style.left = '0'; // 사이드메뉴 열기
    }
    isOpen = !isOpen; // 상태 토글
});

closeBtn.addEventListener('click', () => {
    sidebar.style.left = '-400px'; // 사이드바 닫음
    isOpen = false; // 상태 토글 설정
});

// 지난 채팅 클릭 이벤트 처리
$(document).on('click', '.nangjangbot-lastChat', function () {
    // 클릭한 채팅 항목에서 세션 넘버 추출
    const sessionNumber = $(this).find('.lastChatSession').val();

    // AJAX 요청을 통해 해당 채팅 세션으로 필요한 정보를 서버에 요청
    $.ajax({
        type: "POST",
        url: "/api/chatbot/getChating",
        contentType: "application/json",
        data: JSON.stringify({sessionNumber: sessionNumber}), // 세션 번호를 JSON 형태로 전송
        success: function (chatList) {
            // 서버로부터 응답을 받아 처리
            $('#nangjangbot-conversationContainer').empty(); // 기존 대화 내용 초기화

            // 처음부터 이전 채팅 불러올 때 요소 숨기기
            $('.nangjangbot-pageTitle, #nangjangbot-text').css('display', 'none');
            $('.nangjangbot-block, .nangjangbot-myMsg, .nangjangbot-chatBotMsg').css('display', 'block');

            chatList.forEach(chat => {
                // userMsg를 추가
                if (chat.userMsg) {
                    //현재 방 번호 최신화
                    $('#sessionNumber').val(chat.sessionNumber);
                    const userMessageElement = `<div class="nangjangbot-myMsg">${chat.userMsg}</div>`;
                    $('#nangjangbot-conversationContainer').append(userMessageElement);
                }

                // botReply를 추가
                if (chat.botReply) {
                    const botReplyHtml = `
                        <img src="/image/nangjangbot/apple.png" class="nangjangbot-mascot" alt="챗봇 마스코트">
                        <div class="botMsgBox">${chat.botReply}</div>`;
                    $('#nangjangbot-conversationContainer').append(botReplyHtml);
                }
            });

            $('html, body').animate({
                scrollTop: $(document).height()
            }, 500);
        },
        error: function () {
            alert("채팅을 불러오는 중 오류가 발생했습니다.");
        }
    });
});


// 삭제 버튼 클릭 이벤트 처리
$(document).on('click', '.nangjangbot-imgFrame', function (e) {
    //클릭이벤트 전파 방지(부모요소에도 온클릭 이벤트 때문에 함께 실행됨)
    e.stopPropagation();
    // 모든 요소에서 클릭한 세션넘버 추출
    const sessionNumber = $(this).siblings('.lastChatSession').val();
    const nowSession = $('#sessionNumber').val();

    if (sessionNumber == nowSession) {
        if (confirm("현재 채팅중인 채팅방 입니다.\n계속 진행 하시겠습니까?\n삭제된 채팅은 복구가 불가능합니다.")) {
            $.ajax({
                // ****GET요청에서는 JSON 형식의 데이터를 본문에 포함할 수 없고 쿼리 매개변수로 전달할 수 없음
                type: "DELETE",
                url: "/api/chatbot/delete",
                contentType: "application/json",
                data: JSON.stringify({sessionNumber: sessionNumber}),
                success: function () {
                    alert("삭제되었습니다.");
                    $('#sessionNumber').val(0); // 세션번호 초기화
                    $('#nangjangbot-conversationContainer').empty(); // 기존 대화 내용 초기화
                    $('.nangjangbot-pageTitle, #nangjangbot-text').css('display', 'block'); // 타이틀 다시 보이게

                    // 삭제한 항목만 제거
                    $(`input.lastChatSession[value="${sessionNumber}"]`).closest('.nangjangbot-lastChat').remove();
                },
                error: function () {
                    alert("삭제 중 오류가 발생했습니다.");
                }
            });
        }
    } else {
        if (confirm("정말로 삭제하시겠습니까?\n삭제된 채팅은 복구가 불가능합니다.")) {
            $.ajax({
                // ****GET요청에서는 JSON 형식의 데이터를 본문에 포함할 수 없고 쿼리 매개변수로 전달할 수 없음
                type: "DELETE",
                url: "/api/chatbot/delete",
                contentType: "application/json",
                data: JSON.stringify({sessionNumber: sessionNumber}),
                success: function () {
                    alert("삭제되었습니다.");

                    // 삭제한 항목만 제거
                    $(`input.lastChatSession[value="${sessionNumber}"]`).closest('.nangjangbot-lastChat').remove();
                },
                error: function () {
                    alert("삭제 중 오류가 발생했습니다.");
                }
            });
        }
    }
});

