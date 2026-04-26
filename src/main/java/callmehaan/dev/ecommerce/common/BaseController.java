package callmehaan.dev.ecommerce.common;

import callmehaan.dev.ecommerce.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;

public class BaseController {
    protected <T> ResponseEntity<ApiResponse<T>> ok(int statusCode, String message, T data) {
        return ResponseEntity.ok(ApiResponse.success(statusCode, message, data));
    }
}
