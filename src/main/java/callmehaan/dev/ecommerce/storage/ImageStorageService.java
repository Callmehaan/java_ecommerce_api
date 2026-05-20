package callmehaan.dev.ecommerce.storage;

import callmehaan.dev.ecommerce.product.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageStorageService extends StorageService {
    public List<String> storeImages(Product product, List<MultipartFile> images) throws IOException;
    public void deleteUploadedFiles(List<String> urls);
}
