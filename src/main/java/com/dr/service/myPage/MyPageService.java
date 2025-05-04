    package com.dr.service.myPage;

    import com.dr.dto.myPage.*;
    import com.dr.mapper.myPage.MyPageMapper;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.File;
    import java.io.IOException;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.nio.file.StandardCopyOption;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.UUID;

    @Service
    @Transactional
    @RequiredArgsConstructor
    public class MyPageService {

        // myPageMapper 의존성 주입
        @Autowired
        private final MyPageMapper myPageMapper;

        // userNumber를 사용하여 내 정보 정보 조회!
        public UserInfoDTO getUserInfo(Long userNumber) {
            return myPageMapper.getUserInfo(userNumber);
        }

        // 닉네임 중복 확인 !
        public boolean checkNickname(String userNickname, String currentNickname) {
            if (userNickname.equals(currentNickname)) {
                return true; // 자기 자신은 중복이 아님
            }
            int count = myPageMapper.checkNickname(userNickname);
            return count == 0;
        }

        // 닉네임 업데이트 메서드
        public void updateNickname(Long userNumber, String nickname) {
            myPageMapper.updateNickname(userNumber, nickname);
        }

        //마이페이지 내정보 이미지 변경
        public String updateProfileImage(Long userNumber, MultipartFile profileImage) throws IOException {

            // 파일 이름 생성 (예: userNumber_파일이름)
            String fileName = profileImage.getOriginalFilename();

            // 실제 파일 저장 경로
            Path filePath = Paths.get("src/main/resources/static/image/photo/" + fileName); // 실제 경로 설정

            // 파일 복사
            Files.copy(profileImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 데이터베이스에 경로 업데이트
            myPageMapper.updateProfileImage(userNumber, fileName); // 웹 경로

            // 클라이언트가 접근할 수 있는 URL 경로 반환
            return fileName; // 웹 경로

        }

        // userNumber를 사용하여 회원 탈퇴하기!
        public void deleteUser(Long userNumber) {
            myPageMapper.deleteUser(userNumber);
        }

        // userNumber를 사용하여 내 정보 포인트내역 확인!
        public List<PointDetailDTO> pointHistory(Long userNumber) {
            return myPageMapper.pointHistory(userNumber);
        }

        // userNumber를 사용하여 내가 쓴 레시피 목록 확인!
        public List<UserRecipeDTO> getUserRecipe(Long userNumber) {
            return myPageMapper.getUserRecipe(userNumber);
        }

        // userNumber를 사용하여 내가 쓴 게시글 목록 확인!
        public List<UserPostDTO> getUserPost(Long userNumber) {
            return myPageMapper.getUserPost(userNumber);
        }

        // 해당 메서드는 userNumber에 해당하는 사용자의 찜 목록을 반환!
        public List<UserSteamDTO> getUserSteam(Long userNumber) {
            return myPageMapper.getUserSteam(userNumber);
        }

        // 찜 목록 삭제
        public void deleteUserSteam(UserSteamDTO userSteamDTO) {
            myPageMapper.deleteUserSteam(userSteamDTO);
        }

        // 신고 내역
        public List<SirenListDTO> getSirenList(Long userNumber) {
            return myPageMapper.getSirenList(userNumber);
        }

        // 출석 여부 확인
        public boolean todayCheck(Long userNumber) {
            int count = myPageMapper.todayCheck(userNumber);
            return count > 0; // 오늘 출석 여부 반환
        }

        // 출석 기록 삽입
        public void insertCheck(Long userNumber, String dailyDate) {
            CheckDTO checkDTO = new CheckDTO(); // CheckDTO 객체 생성
            checkDTO.setUserNumber(userNumber); // 사용자 번호 설정
            checkDTO.setDailyDate(dailyDate); // 날짜 설정

            myPageMapper.insertCheck(checkDTO); // DTO를 사용하여 출석 기록 삽입
        }

        // 개근 여부 확인
        public boolean monthFullCheck(Long userNumber) {
            int result = myPageMapper.monthFullCheck(userNumber);
            return result == 1; // 개근 여부 반환
        }

        // 포인트 기록 삽입
        public void insertPointRecord(PointCheckDTO pointCheckDTO) {
            myPageMapper.insertPointRecord(pointCheckDTO);
        }


        // 출석 날짜 리스트 조회
        public List<CheckDTO> getAttendanceDates(Long userNumber) {
            return myPageMapper.getAttendanceDates(userNumber);
        }
    }