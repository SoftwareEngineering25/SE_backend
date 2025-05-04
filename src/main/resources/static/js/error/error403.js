document.addEventListener('mousemove', (event) => {
    createSparkle(event.clientX, event.clientY);
});

function createSparkle(x, y) {
    const sparkle = document.createElement('div');
    sparkle.className = 'sparkle';
    sparkle.style.left = `${x}px`;
    sparkle.style.top = `${y}px`;

    // 랜덤한 크기
    const size = Math.random() * 5 + 5; // 5px에서 10px 사이의 크기
    sparkle.style.width = `${size}px`;
    sparkle.style.height = `${size}px`;

    // 랜덤 색상 생성
    sparkle.style.backgroundColor = getRandomColor();

    // 랜덤한 이동 효과
    const offsetX = (Math.random() - 0.5) * 100; // -50px에서 50px 사이
    const offsetY = (Math.random() - 0.5) * 100; // -50px에서 50px 사이
    sparkle.style.transform = `translate(${offsetX}px, ${offsetY}px)`;

    document.body.appendChild(sparkle);

    // 애니메이션 후 사라지기
    setTimeout(() => {
        sparkle.style.opacity = '0';
    }, 50); // 짧은 지연 후 투명도 변화

    // 요소 제거
    setTimeout(() => {
        sparkle.remove();
    }, 500); // 사라진 후 제거
}

// 랜덤 색상 생성 함수
function getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}



