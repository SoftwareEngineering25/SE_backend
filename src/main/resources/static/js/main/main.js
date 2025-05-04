// 소수점 반올림 함수
function round(value, decimals) {
    return Number(Math.round(value + 'e' + decimals) + 'e-' + decimals);
}
// 소수점 2자리로 반올림 함수
function roundToTwo(value) {
    return Number(Math.round(value + 'e2') + 'e-2');
}
// 음쓰 계산기 함수
function convert() {
    var qnt_kg = document.getElementById("main-qnt").value;

    document.getElementById("main-portion").innerHTML = round(qnt_kg / 0.719, 1) + " 인분";
    document.getElementById("main-co2").innerHTML = round(qnt_kg * 1.7, 1) + " kgCO2eq.";
    document.getElementById("main-tree").innerHTML = round(qnt_kg / 4.87, 1) + " 그루";
    document.getElementById("main-energy").innerHTML = round(qnt_kg * 1.67, 1) + " kWh";
    document.getElementById("main-washingMachine").innerHTML = round(qnt_kg * 12, 1) + " 회";
    document.getElementById("main-transCost").innerHTML = round(qnt_kg * 182, 1) + " 원";
    document.getElementById("main-processCost").innerHTML = round(qnt_kg * 260000, 1) + " 원";
}

// 이벤트 리스너 추가
document.addEventListener("DOMContentLoaded", function () {
    // 무게 입력 필드에 입력할 때마다 convert 함수가 실행되도록 이벤트 리스너를 추가
    var inputField = document.getElementById("main-qnt");
    inputField.addEventListener("input", convert);
});

// 그래프 업데이트 함수
function updateChart(chart, labels, data) {
    chart.data.labels = labels;
    chart.data.datasets[0].data = data;
    chart.update();
}

// 초기 그래프 생성 (빈 데이터로)
let myChart;
document.addEventListener('DOMContentLoaded', function () {
    //chart.js
    const ctx = document.getElementById("myChart").getContext('2d');
    myChart = new Chart(ctx, {
        type: "pie",
        data: {
            labels: [],
            datasets: [{
                backgroundColor: [
                    "#b91d47", "#00aba9", "#2b5797", "#e8c3b9", "#1e7145",
                    "#71491e", "#501347", "#cb75e6", "#45b39d", "#5d6d7e",
                    "#b39d3a", "#2ecc71", "#f39c12", "#c0392b", "#8e44ad",
                    "#3498db", "#9a9a9a", "#2c3e50", "#e67e22", "#f1c40f"
                ],
                data: []
            }]
        },
        options: {
            title: {
                display: true,
                text: "단위(kg)"
            }
        }
    });

    // 초기 로드시 현재 월 데이터로 AJAX 요청
    const monthSelect = document.getElementById('main-monthSelect');
    const now = new Date();
    let year = now.getFullYear();
    let month = now.getMonth() + 1;
    monthSelect.value = `${month}월`;

    // 이전 달 계산: 1월이면 작년 12월, 아니면 현재 연도에 달만 감소
    if (month === 1) {
        month = 12;
        year -= 1;
    } else {
        month -= 1;
    }

    // 처음 로드시에는 이전 달 넣어서
    DataUpdateChart(month);

    // monthSelect 값이 변경될 때마다 데이터를 다시 불러와 업데이트
    monthSelect.addEventListener('change', function () {
        DataUpdateChart(monthSelect.value);
        document.getElementById('currentYearMonth').textContent = `${year}년 ${monthSelect.value} 지역별 가구당 평균 음식물쓰레기 배출량`;

        // 현재 월보다 큰 달, 현재 월이 아직 업데이트 되지 않았을 경우
        if(parseInt(monthSelect.value.replace('월', '')) >= month + 1){
            alert("업데이트 전 입니다")
            document.getElementById('currentYearMonth').textContent = `${year}년 ${monthSelect.value} 지역별 가구당 평균 음식물쓰레기 배출량`;
            DataUpdateChart(month);
        }
    });

    // 그래프 상단 타이틀
    monthSelect.value = `${month}월`;
    document.getElementById('currentYearMonth').textContent = `${year}년 ${monthSelect.value} 지역별 가구당 평균 음식물쓰레기 배출량`;
});

// AJAX 요청을 통해 데이터 가져오고 차트 업데이트
function DataUpdateChart(month) {
    $.ajax({
        type: "GET",
        url: "/publicData",
        contentType: "application/json",
        data: {check: month},
        success: function (publicData) {

            const country = Object.keys(publicData);   // 지역명 리스트
            const amount = Object.values(publicData);  // 지역별 값 리스트

            // 모든 amount 값을 더하여 totalWaste 계산
            const totalWaste = amount.reduce((acc, curr) => acc + roundToTwo(curr), 0);
            document.getElementById("totalWaste").textContent = "전국 월 평균 총 합 " + totalWaste.toFixed(2) + " kg" ; // 소수점 2자리로 표시

            // 차트를 업데이트하는 함수 호출
            updateChart(myChart, country, amount.map(roundToTwo));

        },
        error: function () {
            alert("데이터 불러오기 실패");
        }
    });
}
