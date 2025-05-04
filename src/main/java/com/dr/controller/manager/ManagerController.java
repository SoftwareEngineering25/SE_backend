package com.dr.controller.manager;

import com.dr.dto.manager.*;
import com.dr.service.manager.ManagerService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Manager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ManagerController {
    private final ManagerService managerService;

    // 1-1. 로그인 실패시
    @GetMapping("/managerLogin")
    public String login(HttpSession session, Model model) {
        String loginError = (String) session.getAttribute("loginError");
        if (loginError != null) {
            model.addAttribute("loginError", loginError); // 오류 메시지를 모델에 추가
            session.removeAttribute("loginError"); // 오류 메시지를 세션에서 제거
        }
        return "/manager/managerLogin";
    }

    // 1-2. 로그인 버튼 눌렀을 때
    @PostMapping("/managerLogin")
    public RedirectView login(@RequestParam("managerEmail") String managerEmail, @RequestParam("managerPw") String managerPw, HttpSession session) {

        ManagerSessionDTO loginOk = managerService.managerLogin(managerEmail, managerPw);

        // 로그인 성공 시
        if (loginOk != null) {
            session.setAttribute("managerName", loginOk.getManagerName()); // 이름만 저장
            return new RedirectView("/manager/dashBoard");
        } else {
            session.setAttribute("loginError", "로그인 오류");
            return new RedirectView("/manager/managerLogin");
        }

    }

    // 2. 대시보드
    @GetMapping("/dashBoard")
    public String dashboard(Model model, ManagerDTO managerDTO, DashBoardDTO dashBoardDTO) {

        List<ManagerDTO> managerList = managerService.managerInfo();
        dashBoardDTO = managerService.dashBoardInfo();

        model.addAttribute("manager", managerList);
        model.addAttribute("dashBoard", dashBoardDTO);

        return "manager/dashBoard";
    }

    // 3-1. 회원관리
    @GetMapping("/manageUser")
    public String manageUser(Model model) {
        List<ManagerUserDTO> userList = managerService.manageUser();
        model.addAttribute("userList", userList);

        return "/manager/manageUser";
    }

    // 3-2 회원검색
    @PostMapping("/userSearch")
    public String userSearch(@RequestParam("userNumber") int userNumber, Model model) {
        ManagerUserDTO user = managerService.userSearch(userNumber);
        model.addAttribute("user", user);
        return "/manager/manageUser";
    }

    //4-1. 게시판 관리
    @GetMapping("/manageBoard")
    public String showBoard(Model model) {
        List<ManagerBoardDTO> boardList = managerService.showBoard();
        model.addAttribute("boardList", boardList);
        return "/manager/manageBoard";
    }

    // 4-2. 게시글 검색
    @GetMapping("/boardSearch")
    public String boardSearch(@RequestParam("boardNumber") int boardNumber, Model model) {
        ManagerBoardDTO board = managerService.boardSearch(boardNumber);
        model.addAttribute("board", board);
        return "/manager/manageBoard";
    }

    //5-1. 레시피 관리
    @GetMapping("/manageRecipe")
    public String showRecipe(Model model) {
        List<ManagerRecipeDTO> recipeList = managerService.showRecipe();
        model.addAttribute("recipeList", recipeList);
        return "/manager/manageRecipe";
    }

    // 5-2. 레시피 검색
    @GetMapping("/recipeSearch")
    public String recipeSearch(@RequestParam("recipeNumber") int recipeNumber, Model model) {
        ManagerRecipeDTO recipe = managerService.recipeSearch(recipeNumber);
        model.addAttribute("recipe", recipe);
        return "/manager/manageRecipe";
    }

    // 6-1. 댓글 관리
    @GetMapping("/manageComment")
    public String showComment(Model model) {
        List<ManagerCommentDTO> replyList = managerService.showReply();
        model.addAttribute("replyList", replyList);
        log.info(replyList.toString());
        return "/manager/manageComment";
    }


    // 6-2. 댓글 검색
    @GetMapping("/replySearch")
    public String replySearch(@RequestParam("replyNumber") int replyNumber, Model model) {
        ManagerCommentDTO reply = managerService.replySearch(replyNumber);
        model.addAttribute("reply", reply);
        return "/manager/manageComment";
    }

    // 7-1. 포인트 관리
    @GetMapping("/managePoint")
    public String showPoint(@RequestParam(value = "month", required = false) Integer month, Model model) {
        List<ManagerPointDTO> pointList = managerService.showPoint();

        // 날짜 형식에 맞는 포맷터 생성 (예: "yyyy-MM-dd")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (month != null && month >= 1 && month <= 12) {
            pointList = pointList.stream()
                    .filter(point -> {
                        // 문자열에서 날짜 부분만 추출
                        String dateString = point.getPointDate().split(" ")[0]; // "2024-10-24"
                        LocalDate date = LocalDate.parse(dateString, formatter); // LocalDate로 변환
                        int pointMonth = date.getMonthValue();  // 월 값 추출
                        return pointMonth == month;
                    })
                    .collect(Collectors.toList());
        }

        model.addAttribute("pointList", pointList);
        model.addAttribute("selectedMonth", month);
        return "/manager/managePoint";
    }

    // 7-2. 포인트 검색
    @PostMapping("/pointSearch")
    public String pointSearch(@RequestParam("userNickName") String userNickName, Model model) {
        List<ManagerPointDTO> point = managerService.pointSearch(userNickName);
        model.addAttribute("point", point);
        return "/manager/managePoint";
    }

    // 8-1. 신고 관리
    @GetMapping("/manageReport")
    public String showReport(Model model) {
        List<ManagerReportDTO> reportList = managerService.showReport();
        model.addAttribute("reportList", reportList);
        return "/manager/manageReport";
    }

    //9-1. 상품 관리
    @GetMapping("/manageProduct")
    public String showProduct(Model model) {
        List<ManagerProductDTO> productList = managerService.showProduct();
        model.addAttribute("productList", productList);
        return "/manager/manageProduct";
    }


    // 상품 등록 페이지 이동
    @GetMapping("/registerProduct")
    public String registerProduct(Model model) {
        return "/manager/registerProduct";
    }

    // 9-2. 상품 등록
    @PostMapping("/registerProduct")
    public ResponseEntity<Map<String, String>> registerProduct(
            @RequestParam("productName") List<String> productNames,
            @RequestParam("productPrice") List<Integer> productPrices,
            @RequestParam("productCode") List<String> productCodes,
            @RequestParam("file") MultipartFile file) {

        Map<String, String> response = new HashMap<>();

        try {
            for (int i = 0; i < productCodes.size(); i++) {
                ManagerRegisterDTO managerRegisterDTO = new ManagerRegisterDTO();
                managerRegisterDTO.setProductName(productNames.get(i));
                managerRegisterDTO.setProductPrice(productPrices.get(i));
                managerRegisterDTO.setProductCode(productCodes.get(i));
                // 상품 등록과 파일 저장을 서비스 메서드 호출
                managerService.registerProductAndPhoto(managerRegisterDTO, file);
            }
            response.put("message", "상품이 성공적으로 등록되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "상품 등록 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 9-3. 상품 수정 페이지 이동
    @GetMapping("/showProduct")
    public String updateShow(@RequestParam("productName") String productName, Model model) {
        ManagerRegisterDTO showProduct = managerService.updateShow(productName);
        model.addAttribute("showProduct", showProduct);
        return "/manager/manageUpdate";
    }

    // 9-4. 상품 수정 페이지
    @GetMapping("/updateProduct")
    public String updateProduct() {
        return "/manager/manageUpdate";
    }

    // 상품 추가
    @PostMapping("/updateProduct")
    public ResponseEntity<Map<String, String>> updateProduct(@RequestBody List<ManagerRegisterDTO> products) {
        Map<String, String> response = new HashMap<>();

        log.info(products.toString()+ "dslkfndg");

        try {
            for (ManagerRegisterDTO product : products) {
                ManagerRegisterDTO managerRegisterDTO = new ManagerRegisterDTO();
                managerRegisterDTO.setProductName(product.getProductName());
                managerRegisterDTO.setProductCode(product.getProductCode());
                managerRegisterDTO.setProductPrice(product.getProductPrice());

                managerService.productUpdate(managerRegisterDTO);

            }
            response.put("message", "상품 추가 성공");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "상품 추가 실패");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // 로그아웃
    @GetMapping("/managerLogout")
    public RedirectView logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();
        return new RedirectView("/manager/managerLogin");
    }

}
