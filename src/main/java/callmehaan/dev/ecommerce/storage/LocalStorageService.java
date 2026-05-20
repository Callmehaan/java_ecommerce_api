package callmehaan.dev.ecommerce.storage;

import callmehaan.dev.ecommerce.common.exception.ResourceNotFoundException;
import callmehaan.dev.ecommerce.product.entity.Product;
import callmehaan.dev.ecommerce.storage.entity.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("localStorage")
public class LocalStorageService implements ImageStorageService {
    private final Logger log = LoggerFactory.getLogger(LocalStorageService.class);
    private final StorageProperties storageProperties;
    private final ImageRepository imageRepository;

    public LocalStorageService(StorageProperties storageProperties, ImageRepository imageRepository) {
        this.storageProperties = storageProperties;
        this.imageRepository = imageRepository;
    }

    @Override
    public String save(MultipartFile file) throws IOException {
        validate(file);

        Path rootLocation = Paths.get(storageProperties.getLocation());

        if(!Files.exists(rootLocation))
            Files.createDirectories(rootLocation);

        String extension = getExtension(file);
        String fileName = UUID.randomUUID().toString() + "." + extension;

        Path filePath = rootLocation.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return storageProperties.getBaseUrl() + "/" + fileName;
    }

    @Override
    public void delete(String fileName) throws IOException {
        log.info("Deleting file {}", String.format("%s/%s", storageProperties.getBaseUrl(), fileName));
        Image image = imageRepository.findByUrl(String.format("%s/%s", storageProperties.getBaseUrl(), fileName))
                .orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Image with name %s not found", fileName))
                );
        Path filePath = Paths.get(storageProperties.getLocation(), fileName);

        if(Files.exists(filePath)) {
            Files.delete(filePath);
            imageRepository.delete(image);
        }
    }

    private void validate(MultipartFile file) {
        if(file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if(!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are supported");
        }
    }

    private String getExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();

        if(originalFilename == null) {
            throw new IllegalArgumentException("Invalid filename");
        }

        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }

    public List<String> storeImages(Product product, List<MultipartFile> images) throws IOException {
        List<String> imageUlrs = new ArrayList<>();

        try {
            if (images != null && !images.isEmpty()) {
                for (int i = 0; i < images.size(); i++) {
                    String imageUrl = save(images.get(i));

                    Image image = new Image(
                            imageUrl,
                            i == 0,
                            product
                    );

                    product.addImage(image);
                    imageUlrs.add(imageUrl);
                }
            }
        } catch (Exception e) {
            log.error("Failed to store images", e);
            throw e;
        }
        return imageUlrs;
    }

    public void deleteUploadedFiles(List<String> urls) {

        for (String url : urls) {
            try {
                delete(url);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }

}
