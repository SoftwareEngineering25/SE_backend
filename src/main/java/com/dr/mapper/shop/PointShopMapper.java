package com.dr.mapper.shop;

import com.dr.dto.shop.PointShopDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PointShopMapper {
    //내 포인트 조회
    Long getMyPoint(Long userNumber);

    //전체 상품 조회
    List<PointShopDTO> selectAllProduct();

    //유저의 선택 상품 및 갯수만큼 코드 조회
    List<String> getProductCode(PointShopDTO pointShopDTO);

    //유저의 핸드폰 번호 조회
    String getUserPhone(Long userNumber);

    //유저가 사용한 포인트 회수 처리(사용컬럼 값 넣어서 행 추가)
    void insertUsePoint(PointShopDTO pointShopDTO);

    //유저에게 전송한 상품코드 삭제
    void deleteCode(PointShopDTO pointShopDTO);
}
