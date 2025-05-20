-- Delete data in reverse order of dependency for test isolation

-- 1. Delete reports (depend on user, board, reply)
DELETE FROM DR_SIREN WHERE SIREN_NUMBER IN (1, 2); -- 테스트에서 사용할 신고 ID 삭제

-- 2. Delete replies (depend on user, board)
DELETE FROM DR_REPLY WHERE REPLY_NUMBER IN (1); -- 테스트에서 사용할 댓글 ID 삭제

-- 3. Delete boards (depend on user)
DELETE FROM DR_BOARD WHERE BOARD_NUMBER IN (1); -- 테스트에서 사용할 게시글 ID 삭제

-- 4. Delete the user last
DELETE FROM DR_USER WHERE USER_NUMBER IN (1); -- 테스트에서 사용할 사용자 ID 삭제

-- Insert test data

-- User
INSERT INTO DR_USER (USER_NUMBER, USER_EMAIL, USER_PW, USER_NICKNAME, USER_PHONE, USER_STATUS)
VALUES (1, 'user1@example.com', 'pw1', '닉네임1', '010-1111-1111', '일반회원');

-- Board Post
INSERT INTO DR_BOARD (BOARD_NUMBER, USER_NUMBER, BOARD_TITLE, BOARD_TEXT, BOARD_WRITE_DATE, BOARD_TYPE)
VALUES (1, 1, 'Board 1', 'Content 1', NOW(), '자유게시판'); -- '자유게시판'이 유효한 BOARD_TYPE인지 확인 필요

-- Reply to the Board Post
INSERT INTO DR_REPLY (REPLY_NUMBER, USER_NUMBER, BOARD_NUMBER, RECIPE_NUMBER, REPLY_TEXT, REPLY_WRITE_DATE)
VALUES (1, 1, 1, NULL, 'Reply to board 1', NOW()); -- 게시글 댓글이므로 RECIPE_NUMBER는 NULL

-- Report targeting the Reply (SIREN_NUMBER = 1) - This one will be deleted in the test
INSERT INTO DR_SIREN (SIREN_NUMBER, USER_NUMBER, BOARD_NUMBER, REPLY_NUMBER, RECIPE_NUMBER, SIREN_REASON, SIREN_DATE, SIREN_TYPE)
VALUES (1, 1, NULL, 1, NULL, 'Spam comment', NOW(), '댓글'); -- 댓글 신고이므로 BOARD_NUMBER와 RECIPE_NUMBER는 NULL

-- Report targeting the Board Post (SIREN_NUMBER = 2) - This one should remain
INSERT INTO DR_SIREN (SIREN_NUMBER, USER_NUMBER, BOARD_NUMBER, REPLY_NUMBER, RECIPE_NUMBER, SIREN_REASON, SIREN_DATE, SIREN_TYPE)
VALUES (2, 1, 1, NULL, NULL, 'Inappropriate board', NOW(), '게시글'); -- 게시글 신고이므로 REPLY_NUMBER와 RECIPE_NUMBER는 NULL