package com.dr.controller.main;

import com.dr.dto.main.ItemDTO;
import com.dr.service.main.PublicDataApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class PublicDataApiController {
    private final PublicDataApiService publicDataApiService;

    //지역별 가구당 평균 음식물쓰레기 배출량
    @GetMapping("/publicData")
    public Map<String, Double> publicData(@RequestParam("check") String check) throws URISyntaxException {

        // 공공데이터 접근 메소드 호출
        List<ItemDTO> itemList;
        itemList = publicDataApiService.getGraphInfo(check);

        // county (지역)별로 그룹화하고 평균 amount 계산
        Map<String, Double> groupData = itemList.stream() // 스트림으로
                .collect(Collectors.groupingBy(
                        item -> item.getCountry(), // 지역 이름 가져오기
                        Collectors.averagingDouble(item -> Double.parseDouble(item.getAmount())) // 평균 계산
                ));

        // 미동의가 있으면 미동의 키를 제외한 새로운 Map 생성
        Map<String, Double> filterData = groupData.entrySet().stream()
                .filter(entry -> !entry.getKey().equals("미동의"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return filterData;
    }
}