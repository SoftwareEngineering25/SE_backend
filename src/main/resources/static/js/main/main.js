// main.js 수정 제안

let myChart; // 전역 또는 모듈 스코프로 유지

document.addEventListener('DOMContentLoaded', function () {
    const ctxElement = document.getElementById("myChart");
    const monthSelectElement = document.getElementById('main-monthSelect');
    const currentYearMonthElement = document.getElementById('currentYearMonth');
    const totalWasteElement = document.getElementById('totalWaste');

    if (!ctxElement) {
        console.error("Canvas element with id 'myChart' not found.");
        return; // 차트 그릴 수 없으므로 함수 종료
    }

    const ctx = ctxElement.getContext('2d');
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
            title: { // Chart.js 2.x title 옵션. 3.x 이상은 plugins.title
                display: true,
                text: "단위(kg)"
            },
            // Chart.js 3.x 이상에서는 legend, title 등이 plugins 객체 하위에 위치합니다.
            // plugins: {
            //   title: {
            //     display: true,
            //     text: "단위(kg)"
            //   },
            //   legend: {
            //     display: true // 필요에 따라
            //   }
            // }
        }
    });

    // --- 초기 월 설정 및 데이터 로드 ---
    const now = new Date();
    let currentDisplayYear = now.getFullYear(); // 화면 표시용 연도
    let currentDisplayMonth = now.getMonth() + 1; // 화면 표시용 월 (1-12)

    let dataFetchYear = now.getFullYear(); // 데이터 요청용 연도
    let dataFetchMonth = now.getMonth() + 1; // 데이터 요청용 월 (1-12)

    // 이전 달 계산 (데이터 요청용)
    if (dataFetchMonth === 1) {
        dataFetchMonth = 12;
        dataFetchYear -= 1;
    } else {
        dataFetchMonth -= 1;
    }

    // 처음 로드시에는 "계산된 이전 달" 데이터로 차트 요청
    DataUpdateChart(dataFetchMonth, dataFetchYear); // 연도 정보도 함께 전달 고려

    if (monthSelectElement) {
        monthSelectElement.value = `${currentDisplayMonth}월`; // 드롭다운은 "현재 월"로 초기화

        if (currentYearMonthElement) {
            currentYearMonthElement.textContent = `${currentDisplayYear}년 ${currentDisplayMonth}월 지역별 가구당 평균 음식물쓰레기 배출량`;
        }

        monthSelectElement.addEventListener('change', function () {
            const selectedMonthValue = parseInt(this.value.replace('월', ''));
            let requestYear = currentDisplayYear; // 기본적으로 현재 연도

            // TODO: 연도 선택 기능이 없다면, 여기서 연도를 어떻게 결정할지 명확히 해야 합니다.
            // 예시: 만약 12월에서 1월로 넘어가는 경우 등 연도 변경 로직이 필요할 수 있습니다.
            // 현재 로직은 단순하게 현재 연도를 기준으로 합니다.

            // 현재 월보다 큰 달을 선택했는지, 아니면 데이터가 없는 미래의 달인지 확인하는 로직 개선 필요
            // 서버에서 해당 월의 데이터 유무를 판단하는 것이 더 정확합니다.
            const today = new Date();
            const actualCurrentMonth = today.getMonth() + 1;
            const actualCurrentYear = today.getFullYear();

            if (requestYear > actualCurrentYear || (requestYear === actualCurrentYear && selectedMonthValue > actualCurrentMonth)) {
                alert("선택하신 월의 데이터는 아직 업데이트 전입니다. 이전 달의 데이터를 표시합니다.");
                if (currentYearMonthElement) {
                     currentYearMonthElement.textContent = `${dataFetchYear}년 ${dataFetchMonth}월 지역별 가구당 평균 음식물쓰레기 배출량 (업데이트 예정)`;
                }
                DataUpdateChart(dataFetchMonth, dataFetchYear); // 이전 달 데이터로 다시 로드
                monthSelectElement.value = `${dataFetchMonth}월`; // 드롭다운도 이전 달로
                return;
            }

            DataUpdateChart(selectedMonthValue, requestYear); // 선택한 월과 연도로 데이터 요청

            if (currentYearMonthElement) {
                currentYearMonthElement.textContent = `${requestYear}년 ${this.value} 지역별 가구당 평균 음식물쓰레기 배출량`;
            }
        });
    } else {
        console.error("Element with id 'main-monthSelect' not found.");
        // monthSelect가 없어도 초기 차트 로드는 시도 (위에서 dataFetchMonth, dataFetchYear 사용)
        if (currentYearMonthElement) {
             currentYearMonthElement.textContent = `${dataFetchYear}년 ${dataFetchMonth}월 지역별 가구당 평균 음식물쓰레기 배출량 (월 선택 불가)`;
        }
    }
});

// AJAX 요청을 통해 데이터 가져오고 차트 업데이트
// 연도(year) 파라미터 추가 고려
function DataUpdateChart(month, year) { // year 파라미터 추가
    const requestData = { check: `${month}월` }; // 서버가 "X월" 형태를 받는다면
    // 만약 서버가 숫자 월을 받는다면 requestData = { month: month, year: year }; 등으로 수정

    console.log("Requesting data for:", year, month); // 요청 데이터 확인

    $.ajax({
        type: "GET",
        url: "/publicData", // 실제 API 엔드포인트
        contentType: "application/json",
        data: requestData, // 서버가 받는 파라미터 형식에 맞게
        success: function (publicData) {
            console.log("Received data:", publicData); // 받은 데이터 확인

            if (publicData && Object.keys(publicData).length > 0) {
                const country = Object.keys(publicData);
                const amount = Object.values(publicData);

                const totalWasteCalculated = amount.reduce((acc, curr) => acc + roundToTwo(parseFloat(curr) || 0), 0); // parseFloat 추가 및 기본값 0
                if(document.getElementById("totalWaste")){
                     document.getElementById("totalWaste").textContent = "전국 월 평균 총 합 " + totalWasteCalculated.toFixed(2) + " kg";
                }

                updateChart(myChart, country, amount.map(val => roundToTwo(parseFloat(val) || 0))); // parseFloat 추가 및 기본값 0
            } else {
                console.warn("Received empty or invalid data for chart.");
                // 데이터가 없을 경우 차트를 비우거나 메시지 표시
                updateChart(myChart, [], []);
                if(document.getElementById("totalWaste")){
                    document.getElementById("totalWaste").textContent = "데이터 없음";
                }
            }
        },
        error: function (xhr, status, error) {
            console.error("데이터 불러오기 실패:", status, error, xhr.responseText);
            alert("데이터를 불러오는 데 실패했습니다.");
            // 에러 발생 시 차트를 비우거나 기본 상태로 돌릴 수 있음
            updateChart(myChart, [], []);
             if(document.getElementById("totalWaste")){
                document.getElementById("totalWaste").textContent = "데이터 로드 실패";
            }
        }
    });
}

// --- 음쓰 계산기 관련 코드 ---
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
    const qntInput = document.getElementById("main-qnt");
    if (!qntInput) return; // 요소 없으면 종료

    const qnt_kg = parseFloat(qntInput.value) || 0; // 숫자로 변환, NaN이면 0

    // 각 ID 요소 존재 확인 후 innerHTML 설정
    const portionEl = document.getElementById("main-portion");
    if (portionEl) portionEl.innerHTML = round(qnt_kg / 0.719, 1) + " 인분";

    const co2El = document.getElementById("main-co2");
    if (co2El) co2El.innerHTML = round(qnt_kg * 1.7, 1) + " kgCO2eq.";

    const treeEl = document.getElementById("main-tree");
    if (treeEl) treeEl.innerHTML = round(qnt_kg / 4.87, 1) + " 그루";

    const energyEl = document.getElementById("main-energy");
    if (energyEl) energyEl.innerHTML = round(qnt_kg * 1.67, 1) + " kWh";

    const washingMachineEl = document.getElementById("main-washingMachine");
    if (washingMachineEl) washingMachineEl.innerHTML = round(qnt_kg * 12, 1) + " 회";

    const transCostEl = document.getElementById("main-transCost");
    if (transCostEl) transCostEl.innerHTML = round(qnt_kg * 182, 1) + " 원";

    const processCostEl = document.getElementById("main-processCost");
    if (processCostEl) processCostEl.innerHTML = round(qnt_kg * 260000, 1) + " 원";
}

// DOMContentLoaded는 이미 위에서 한 번 사용했으므로, 해당 리스너 내부에 로직 통합 권장
// 여기서는 별도로 두지만, 실제로는 통합하는 것이 좋음
const inputField = document.getElementById("main-qnt");
if (inputField) {
    inputField.addEventListener("input", convert);
    convert(); // 페이지 로드 시 초기 계산 실행
}

// 그래프 업데이트 함수 (myChart가 초기화된 후에 접근해야 함)
function updateChart(chart, labels, data) {
    if (chart && chart.data) { // chart 객체 및 data 속성 존재 확인
        chart.data.labels = labels;
        if (chart.data.datasets && chart.data.datasets.length > 0) {
            chart.data.datasets[0].data = data;
        } else { // datasets이 없거나 비어있으면 새로 생성 (방어적)
            chart.data.datasets = [{
                 backgroundColor: [ /* ... 기존 색상 배열 ... */ ],
                 data: data
            }];
        }
        chart.update();
    } else {
        console.error("Chart object is not properly initialized for updateChart function.");
    }
}