package com.dr.service.main;

import com.dr.dto.main.SearchDTO;
import com.dr.mapper.main.SearchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchService {

    private final SearchMapper searchMapper;

    // 게시판 검색
    public List<SearchDTO> getBoardSearch(SearchDTO searchDTO) {
        return searchMapper.getBoardSearch(searchDTO);
    }

    // 레시피 검색
    public List<SearchDTO> getRecipeSearch(SearchDTO searchDTO) {
        return searchMapper.getRecipeSearch(searchDTO);
    }
}
