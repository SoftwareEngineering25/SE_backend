package com.dr.mapper.shop;

import com.dr.dto.shop.PointShopDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PointShopMapperTest {

    @Autowired
    PointShopMapper pointShopMapper;

    @Test
    void testGetMyPoint() {
        //given 초기값 설정
        Long userNumber = 7L; // 테스트할 사용자 번호

        //when 메소드 검사(실행)
        Long totalPoint = pointShopMapper.getMyPoint(userNumber); // given에 userNumber 넣어서 쿼리 실행

        //then 결과 검증
        assertThat(totalPoint)
                .isNotNull() // 결과가 null이 아님을 확인
                .isGreaterThanOrEqualTo(0L); // 포인트는 0 이상이어야 함

    }

    @Test
    void testSelectAllProduct() {
        //given 초기값 설정
//        매개변수 없음

        //when 메소드 검사(실행)
        List<PointShopDTO> products = pointShopMapper.selectAllProduct();

        //then 결과 검증
        // 1. 행(row) 개수 검증
        assertThat(products).isNotEmpty();

        // 2. 중복된 항목 검증
        assertThat(products).doesNotHaveDuplicates();
    }

    @Test
    void testGetProductCode() {
        // given 초기값 설정
        PointShopDTO pointShopDTO = new PointShopDTO();

        pointShopDTO.setProductName("핫식스");
        pointShopDTO.setQuantity(3L);

        // when 메소드 검사(실행)
        List<String> result = pointShopMapper.getProductCode(pointShopDTO);

        // then: 결과 검증
        // 1. 행(row) 개수 검증: pointShopDTO.getQuantity()와 동일해야 함
        assertThat(result).hasSize(pointShopDTO.getQuantity().intValue());

        // 2. 결과 중에 중복이 없어야 함
        assertThat(result).doesNotHaveDuplicates();
    }

    @Test
    void testGetUserPhone() {
        // given 초기값 설정
        Long userNumber = 7L;

        // when 메소드 검사(실행)
        String phone = pointShopMapper.getUserPhone(userNumber);

        // then 결과 검증
        // 무조건 하나의 결과
        assertThat(phone).isNotNull().isNotEmpty();
    }

    @Test
    void testInsertUserPoint() {
        // given 초기값 설정
        PointShopDTO pointShopDTO = new PointShopDTO();

        pointShopDTO.setTotalCost(3000L);
        pointShopDTO.setUserNumber(7L);

        // when 메소드 검사(실행)


        // then 결과 검증
        // 하나의 row insert되어야 함

    }

    @Test
    void testDeleteCode() {
        // given 초기값 설정
        PointShopDTO pointShopDTO = new PointShopDTO();

        pointShopDTO.setProductName("핫식스");
        pointShopDTO.setQuantity(3L);

        // when 메소드 검사(실행)
        pointShopMapper.deleteCode(pointShopDTO);

        // then 결과 검증
        // getProductCode() 결과가 삭제 되어야 함
    }


}
