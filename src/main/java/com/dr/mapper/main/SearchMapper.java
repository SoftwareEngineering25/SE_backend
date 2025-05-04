package com.dr.mapper.main;

import com.dr.dto.main.SearchDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SearchMapper {

    // 게시판 검색 결과 조회
    List<SearchDTO> getBoardSearch (SearchDTO searchDTO);

    // 레시피 검색 결과 조회
    List<SearchDTO> getRecipeSearch (SearchDTO searchDTO);
}
