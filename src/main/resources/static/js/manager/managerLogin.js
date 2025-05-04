// 1. id 와 비번 값 없을 시 p 태그에 값 입력하세요 출력
const id = document.querySelector("#manager-loginId");
const pw = document.querySelector("#manager-loginPw");
const result = document.querySelector("#manager-none");
const button = document.querySelector("#manager-loginButton");

button.addEventListener("click", function() {
  let message = "";

  // 아이디가 비어있는지 확인
  if (id.value.trim() === "") {
    message += "아이디를 입력하세요. <br>";
  }

  // 비밀번호가 비어있는지 확인
  if (pw.value.trim() === "") {
    message += "비밀번호를 입력하세요.";
  }

  result.innerHTML = message;
  result.style.color = "red";

});

// 2. 비밀번호 토글 
const noView = document.querySelector(".manager-formTogle1");
const View = document.querySelector(".manager-formTogle2");

// '비밀번호 비이기' 버튼 클릭 시
noView.addEventListener("click", function() {
  pw.type = "text";
  noView.style.display = "none"; 
  View.style.display = "inline";  
});

// '비밀번호 숨기기' 버튼 클릭 시
View.addEventListener("click", function() {
  pw.type = "password"; 
  noView.style.display = "inline"; 
  View.style.display = "none";     
});


