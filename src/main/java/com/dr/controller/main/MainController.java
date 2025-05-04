package com.dr.controller.main;

import com.dr.dto.main.SearchDTO;
import com.dr.service.main.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {
    private final SearchService searchService;

    //메인페이지
    @GetMapping("/DRmain")
    public String openMain() {
        return "DRmain";
    }

    //개인정보 처리방침
    @GetMapping("/privacyPolicy")
    public String openPrivacyPolicy() {
        return "main/privacyPolicy";
    }

    //이용약관
    @GetMapping("/terms")
    public String openTerms() {
        return "main/terms";
    }

    //검색창
    @GetMapping("/search")
    public String search(@SessionAttribute(value = "userNumber", required = false) Long userNumber,
                         @RequestParam("searchType") String searchType,
                         @RequestParam("searchValue") String searchValue,
                         Model model) {

        // 로그인하지 않았을 경우 처리
        if (userNumber == null) {
            return "redirect:/user/login"; // 로그인 페이지로 리다이렉션
        }

        // 조회 메소드에 사용할 DTO
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setSearchType(searchType);
        searchDTO.setSearchValue(searchValue);

        // 검색타입 검사후 조회 메소드 실행
        if (searchType.contains("게시판")) {
            // 게시판 검색
            List<SearchDTO> searchResult = searchService.getBoardSearch(searchDTO);
            model.addAttribute("searchResult", searchResult);
            return "main/boardSearchList";
        } else if (searchType.contains("레시피")) {
            //레시피 검색
            List<SearchDTO> searchResult = searchService.getRecipeSearch(searchDTO);
            model.addAttribute("searchResult", searchResult);
            return "main/recipeSearchList";
        } else {
            return "redirect:/chatBot/nangjangbot"; // 에러 페이지로
        }
    }
}
