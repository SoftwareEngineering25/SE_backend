package com.dr.mapper.user;

import com.dr.dto.user.EmailFindDTO;
import com.dr.dto.user.PwFindDTO;
import com.dr.dto.user.UserDTO;
import com.dr.dto.user.UserSessionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {
    void insertUser(UserDTO userDTO);

    Optional<UserSessionDTO> userLogin(@Param("userEmail") String userEmail, @Param("userPw") String userPw);


    boolean checkEmailExists(String userEmail);


    boolean checkPhoneExists(String userPhone);

    EmailFindDTO userEmailFind(String userPhone);


    // 비밀번호 찾기 메서드
    PwFindDTO userPwFind(String userPhone, String userEmail);


    // 비밀번호 변경 메서드
    void updatePassword(@Param("userPw") String userPw , @Param("userPhone") String userPhone);


    // 닉네임 중복확인
    int checkNickName(String userNickName);


    // 신규가입유저 포인트0 row 생성
    void insertNewUserPoint(UserDTO user);

    // 신규가입유저 점수0 row 생성
    void insertNewUserScore(UserDTO user);

    // 신규가입유저 점수0 row 생성
    void insertNewUserPhoto(UserDTO user);

    // 방금 가입유저 pk조회
    Long findNewUser(UserDTO user);
}