package com.webkit.travel_safety_backend.global.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/* ApiResponse
* <정적 팩토리 메서드>
* 생성자를 통한 객체 생성이 아닌 메소드 호출을 통한 객체 생성
* new ApiResponse<>(인자) => X
* ApiResponse.success(인자) => O, 가독성이 좋음, 의미가 명확함
* */

@Getter
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private Boolean isSuccess;

    private String message;

    private T data;

    // 외부에서 기본 생성자 호출되는 것을 막음
    private ApiResponse(Boolean isSuccess, String message, T data) {}

    // 정적 메서드
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, null, data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
