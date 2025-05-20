DELETE FROM DR_RECIPE; -- (만약 있다면)
DELETE FROM DR_USER WHERE USER_NUMBER IN (1); -- 이 스크립트에서 사용할 USER_NUMBER들
-- 댓글 관리 (조회) 테스트를 위한 데이터 삽입
-- showReply() 쿼리가 실행되고, get(0) 접근이 가능하도록 최소 1개의 댓글 필요
-- 댓글은 사용자 및 게시글/레시피에 외래 키로 연결되므로 관련 데이터도 필요
INSERT INTO DR_USER (USER_NUMBER, USER_EMAIL, USER_PW, USER_NICKNAME, USER_PHONE, USER_STATUS) VALUES (1, 'user1@example.com', 'pw1', '닉네임1', '010-1111-1111', '일반회원');
INSERT INTO DR_BOARD (BOARD_NUMBER, USER_NUMBER, BOARD_TITLE, BOARD_TEXT, BOARD_WRITE_DATE, BOARD_TYPE) VALUES (1, 1, 'Board 1', 'Content 1', NOW(), '자유게시판');

-- 테스트 코드에서 "UniqueText_127" 내용을 기대하는 댓글
INSERT INTO DR_REPLY (REPLY_NUMBER, USER_NUMBER, BOARD_NUMBER, RECIPE_NUMBER, REPLY_TEXT, REPLY_WRITE_DATE)
VALUES (1, 1, 1, null, 'UniqueText_127', NOW()); -- 첫 번째 댓글로 예상

-- 다른 댓글 (목록에 포함될 것)
INSERT INTO DR_REPLY (REPLY_NUMBER, USER_NUMBER, BOARD_NUMBER, RECIPE_NUMBER, REPLY_TEXT, REPLY_WRITE_DATE)
VALUES (2, 1, 1, null, 'Another reply content', NOW());