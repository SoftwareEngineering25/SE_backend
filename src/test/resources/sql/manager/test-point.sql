
DELETE FROM DR_RECIPE; -- (만약 있다면)
DELETE FROM DR_USER WHERE USER_NUMBER IN (1,2); -- 이 스크립트에서 사용할 USER_NUMBER들
-- 포인트 관리 (회수) 테스트를 위한 데이터 삽입
-- takePoint(140) 실행 후 showPoint()에서 140번 포인트의 POINT_CHANGE가 0인지 확인
-- 포인트는 사용자에게 외래 키로 연결되므로 사용자 데이터도 필요
INSERT INTO DR_USER (USER_NUMBER, USER_EMAIL, USER_PW, USER_NICKNAME, USER_PHONE, USER_STATUS) VALUES (1, 'user1@example.com', 'pw1', '닉네임1', '010-1111-1111', '일반회원');
INSERT INTO DR_USER (USER_NUMBER, USER_EMAIL, USER_PW, USER_NICKNAME, USER_PHONE, USER_STATUS) VALUES (2, 'user2@example.com', 'pw2', '닉네임2', '010-2222-2222', '일반회원'); -- SUM() OVER 테스트를 위해 다른 사용자 추가

-- 회수 대상 포인트 (pointNumber = 140) - 초기값 100
INSERT INTO DR_POINT (POINT_NUMBER, USER_NUMBER, POINT_CHANGE, POINT_NOTE, POINT_DATE)
VALUES (140, 1, 100, 'Initial points for test', NOW());

-- 다른 포인트 내역 (showPoint() 결과에 포함될 것)
INSERT INTO DR_POINT (POINT_NUMBER, USER_NUMBER, POINT_CHANGE, POINT_NOTE, POINT_DATE)
VALUES (141, 1, 50, 'Earned points', NOW());

INSERT INTO DR_POINT (POINT_NUMBER, USER_NUMBER, POINT_CHANGE, POINT_NOTE, POINT_DATE)
VALUES (142, 2, 200, 'Points for user 2', NOW());