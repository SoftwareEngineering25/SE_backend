// timeForToday 함수
export function timeForToday(value) {
    const today = new Date();
    const timeValue = new Date(value);

    //Math.floor() : 소수점을 내림처리
    //getTime() : 1970년 1월 1일을 기준으로 지금까지 몇 ms가 지났는지 알려준다
    const betweenTime = Math.floor((today.getTime() - timeValue.getTime()) / 1000 / 60); // 분 단위로 계산

    if (betweenTime < 1) {
        return "방금 전";
    }
    if (betweenTime < 60) {
        return `${betweenTime}분 전`;
    }

    const betweenTimeHour = Math.floor(betweenTime / 60);
    if (betweenTimeHour < 24) {
        return `${betweenTimeHour}시간 전`;
    }

    const betweenTimeDay = Math.floor(betweenTime / 60 / 24);
    if (betweenTimeDay < 365) {
        return `${betweenTimeDay}일 전`;
    }

    return `${Math.floor(betweenTimeDay / 365)}년 전`;
}

// DOMContentLoaded 이벤트를 사용하여 페이지가 로드된 후 실행
document.addEventListener('DOMContentLoaded', () => {
    const chatDates = document.querySelectorAll('.nangjangbot-lastChatDate');

    chatDates.forEach(dateElement => {
        const originalDate = dateElement.textContent; // 원본 날짜 가져오기
        const timeAgo = timeForToday(originalDate); // 변환된 시간 가져오기
        dateElement.textContent = timeAgo; // DOM 업데이트
    });
});

// timeForToday를 최신 HTML에도 적용하기 위한 함수
export function applyTimeForToday() {
    const chatDates = document.querySelectorAll('.nangjangbot-lastChatDate');
    chatDates.forEach(dateElement => {
        const originalDate = dateElement.textContent;
        const timeAgo = timeForToday(originalDate);
        dateElement.textContent = timeAgo;
    });
}
