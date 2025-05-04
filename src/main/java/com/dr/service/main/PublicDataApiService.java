package com.dr.service.main;

import com.dr.dto.main.ApiDTO;
import com.dr.dto.main.ItemDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PublicDataApiService {

    public List<ItemDTO> getGraphInfo (String check) throws URISyntaxException {
        // 공공데이터 포털 인코딩키 사용
        String serviceKey = "rl8I75tIBgZHmYu5%2BllLFpEs7pKoTnuOO5hejyYQWNgh2wdOPSume7l6xX9wNt7T9v%2FIlZp1lFbJzDR4TRPUkQ%3D%3D";

        // url처리를 위한 변수
        String baseUrl = "https://api.odcloud.kr/api/15125354/v1/";

        // 월별 공공데이터 url 처리
        switch (check) {
            case "1월": baseUrl = baseUrl + "uddi:24ba9530-f1c8-4e6f-bbd5-ca765800480a";
                break;
            case "2월": baseUrl = baseUrl + "uddi:6afe07ea-69c1-493c-b1f8-87d555f419d0";
                break;
            case "3월": baseUrl = baseUrl + "uddi:77b7fb2a-064b-4d00-b23c-89f96a361376";
                break;
            case "4월": baseUrl = baseUrl + "uddi:723a3307-ad9b-4bee-9854-a26fe7486908";
                break;
            case "5월": baseUrl = baseUrl + "uddi:a2445514-e175-40a1-959f-a75667bb61f7";
                break;
            case "6월": baseUrl = baseUrl + "uddi:9e362e72-180b-4599-9b5f-cd9b4ee252c4";
                break;
            case "7월": baseUrl = baseUrl + "uddi:cd059f6e-6d05-4221-95f8-e69b4f529c9c";
                break;
            case "8월": baseUrl = baseUrl + "uddi:420a3ec9-591c-4b9a-80ce-e3b502362bf2";
                break;
            case "9월": baseUrl = baseUrl + "uddi:a46a069c-278f-4d70-83a9-b688dafa222b";
                break;
            case "10월": baseUrl = baseUrl + "uddi:6ed6b06b-631a-459c-a9c8-f533c7fe116a";
                break;
            case "11월": baseUrl = baseUrl + "uddi:6ed6b06b-631a-459c-a9c8-f533c7fe116a"; // 아직 업데이트 되지 않은 공공데이터 마지막 데이터로 대체
                break;
            case "12월": baseUrl = baseUrl + "uddi:6ed6b06b-631a-459c-a9c8-f533c7fe116a"; // 아직 업데이트 되지 않은 공공데이터 마지막 데이터로 대체
                break;
            default : baseUrl = baseUrl + "uddi:6ed6b06b-631a-459c-a9c8-f533c7fe116a"; // 기본 마지막 데이터
                break;
        }

        //쿼리파라미터로 연결하여 달의 모든 페이지 데이터 처리
        HttpHeaders headers = new HttpHeaders();
        List<ItemDTO> itemList = new ArrayList<>();

        // 공공데이터 달마다 페이지가 있어서 모든 페이지 접근하여 List에 추가
        for (int i = 1; i <= 10; i++) {
            String url = baseUrl + "?page=" + i + "&serviceKey=" + serviceKey;

            // URI 객체를 URL에 따라 생성
            URI uri = new URI(url);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // API 호출
            ApiDTO apiDTO = restTemplate.exchange(uri, HttpMethod.GET, entity, ApiDTO.class).getBody();

            // 데이터가 null이 아닐 경우에만 추가
            if (apiDTO != null && apiDTO.getData() != null) {
                for (ItemDTO item : apiDTO.getData()) {
                    itemList.add(item);
                }
            }
        }

        return itemList;
    }
}
