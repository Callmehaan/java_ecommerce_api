package callmehaan.dev.ecommerce.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    String save(MultipartFile file) throws IOException;
    void delete(String fileName) throws IOException;
}
