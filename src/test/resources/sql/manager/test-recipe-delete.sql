
DELETE FROM DR_RECIPE; -- (만약 있다면)
DELETE FROM DR_USER WHERE USER_NUMBER IN (1); -- 이 스크립트에서 사용할 USER_NUMBER들
-- 레시피 관리 (삭제) 테스트를 위한 데이터 삽입
-- recipeDelete(31) 실행 후 showRecipe()에서 31번이 사라졌는지 확인
-- 레시피는 사용자에게 외래 키로 연결되므로 사용자 데이터도 필요
INSERT INTO DR_USER (USER_NUMBER, USER_EMAIL, USER_PW, USER_NICKNAME, USER_PHONE, USER_STATUS) VALUES (1, 'user1@example.com', 'pw1', '닉네임1', '010-1111-1111', '일반회원');

-- 삭제 대상 레시피 (recipeNumber = 31)
INSERT INTO DR_RECIPE (RECIPE_NUMBER, USER_NUMBER, RECIPE_TITLE, RECIPE_TEXT, RECIPE_WRITE_DATE)
VALUES (31, 1, '삭제될 레시피 제목', '삭제될 레시피 내용입니다.', NOW());

-- 삭제되지 않을 다른 레시피 (showRecipe() 결과에 포함될 것)
INSERT INTO DR_RECIPE (RECIPE_NUMBER, USER_NUMBER, RECIPE_TITLE, RECIPE_TEXT, RECIPE_WRITE_DATE)
VALUES (32, 1, '다른 레시피 제목1', '다른 레시피 내용1입니다.', NOW());