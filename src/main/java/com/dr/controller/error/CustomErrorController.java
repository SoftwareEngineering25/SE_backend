package com.dr.controller.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 커스텀 에러 컨트롤러
 * 에러 발생 시 로그를 기록하고 적절한 에러 페이지로 라우팅
 */
@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    // 로깅을 위한 Logger 설정
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/403")
    public String handleError403(HttpServletRequest request) {
        logError(request);  // 내부적으로 에러 정보 로깅
        return "error/error403";
    }

    @GetMapping("/404")
    public String handleError404(HttpServletRequest request) {
        logError(request);  // 내부적으로 에러 정보 로깅
        return "error/error404";
    }

    @GetMapping("/500")
    public String handleError500(HttpServletRequest request) {
        logError(request);  // 내부적으로 에러 정보 로깅
        return "error/error500";
    }

    @GetMapping
    public String handleError(HttpServletRequest request) {
        // 에러 정보 로깅
        logError(request);

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if(status != null) {
            int statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/error404";
            }
            if(statusCode == HttpStatus.FORBIDDEN.value()) {
                return "error/error403";
            }
            if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/error500";
            }
        }
        return "error/error500";
    }

    /**
     * 에러 정보를 로그로 기록하는 메서드
     * 내부적으로 상세한 에러 정보를 로깅
     */
    private void logError(HttpServletRequest request) {
        String status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) != null ?
                request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString() : "Unknown";
        String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        String path = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        logger.error("=================== Error Log Start ===================");
        logger.error("Status Code: {}", status);
        logger.error("Requested URL: {}", path);
        if(message != null) {
            logger.error("Error Message: {}", message);
        }
        if(exception != null) {
            logger.error("Exception Details:", exception);
        }
        logger.error("=================== Error Log End ===================");
    }
}
