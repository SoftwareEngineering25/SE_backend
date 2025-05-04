document.addEventListener('DOMContentLoaded', function() {
    const calendarEl = document.getElementById('calendar');
    const calendar = new FullCalendar.Calendar(calendarEl, {
        locale: 'ko',
        initialView: 'dayGridMonth',
        eventDidMount: function(info) { // eventRender 대신 eventDidMount 사용
            const event = info.event;
            if (event.extendedProps.imageUrl) {
                const img = document.createElement('img');
                img.src = event.extendedProps.imageUrl;
                img.style.width = '80px'; // 원하는 크기로 변경
                img.style.height = '80px'; // 원하는 크기로 변경
                img.style.filter = 'invert(41%) sepia(600%) saturate(5000%)';
                img.style.position = 'absolute'; // 절대 위치 설정
                img.style.left = '16%'; // 수평 중앙 위치
                img.style.transform = 'translateX(-50%, -500%) '; // 중앙 정렬을 위한 변환
                img.style.marginTop = '-40px';

                // 날짜 요소의 위치를 기준으로 이미지 추가하기
                info.el.style.position = 'relative'; // 부모 요소에 상대 위치 설정
                info.el.appendChild(img); // 이벤트 요소에 이미지 추가
            }
        }
    });
    calendar.render();

    // 출석 체크 버튼 클릭 이벤트
    document.getElementById("checkInButton").addEventListener("click", function() {
        const date = document.getElementById("date").value.trim() || new Date().toISOString().split('T')[0];
        console.log("선택된 날짜: ", date); // 선택된 날짜 로그 출력

        const existingEvent = calendar.getEvents().find(event => event.startStr === date);
        console.log("오늘 날짜 선택 : ", existingEvent);

        if (existingEvent) {
            alert("이미 출석 체크가 완료되었습니다.");
        } else {
            $.ajax({
                url: '/myPage/myPageCheckAttendance',
                method: 'POST',
                data: { date: date },
                success: function(response) {
                    alert(response);

                    // 출석 체크 성공 시 당일 날짜에 체크 표시 추가
                    calendar.addEvent({
                        start: date,
                        allDay: true,
                        color: 'white',
                        rendering: 'background', // 배경으로 표시
                        extendedProps: {
                            imageUrl: '../image/check1.png' // 체크 이미지 경로
                        }
                    });

                    // 개근 여부 확인
                    checkFullAttendance();
                },
                error: function() {
                    alert("출석 체크 중 오류가 발생했습니다.");
                }
            });
        }
    });

    // 개근 여부 확인
    function checkFullAttendance() {
        $.ajax({
            url: '/myPage/myPageFullAttendance', // 개근 여부 확인 URL
            method: 'GET',
            success: function(response) {
                alert(response); // 개근 여부에 대한 응답 알림
            },
            error: function() {
                console.error('개근 여부 확인 중 오류가 발생했습니다.');
            }
        });
    }

    // 출석 날짜 리스트 불러오기
    function loadAttendanceDates() {
        $.ajax({
            url: '/myPage/myPageAttendanceDates',
            method: 'GET',
            success: function(data) {
                const attendanceList = $('#attendanceList');
                attendanceList.empty(); // 기존 목록 비우기
                if (data.length > 0) {
                    data.forEach(date => {
                        attendanceList.append(`<li>${date}</li>`); // 출석 날짜 추가
                        // 달력에 출석 체크 이벤트 추가
                        calendar.addEvent({
                            start: date,   // 날짜 문자열 직접 사용
                            allDay: true,
                            color: 'white',  // 출석 체크 색상
                            rendering: 'background', // 배경으로 표시
                            extendedProps: {
                                imageUrl: '../image/check1.png' // 체크 이미지 경로
                            }
                        });
                    });
                } else {
                    attendanceList.append('<li>출석 기록이 없습니다.</li>');
                }
            },
            error: function() {
                console.error('출석 날짜를 가져오는 데 오류가 발생했습니다.');
            }
        });
    }

    loadAttendanceDates(); // 페이지 로드 시 출석 날짜 리스트 불러오기
});
