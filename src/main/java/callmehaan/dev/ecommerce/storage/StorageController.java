package callmehaan.dev.ecommerce.storage;

import callmehaan.dev.ecommerce.common.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/storage")
public class StorageController {
    private final StorageService storageService;

    public StorageController(@Qualifier("localStorage") StorageService storageService) {
        this.storageService = storageService;
    }

    @DeleteMapping("{filename}")
    public ResponseEntity<ApiResponse<Void>> deleteFile(@PathVariable String filename) throws IOException {
        this.storageService.delete(filename);

        ApiResponse<Void> response = ApiResponse.success(
                HttpStatus.OK.value(), "Image deleted successfully", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
