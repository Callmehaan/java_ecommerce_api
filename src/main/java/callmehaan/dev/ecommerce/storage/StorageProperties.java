package callmehaan.dev.ecommerce.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.storage")
@Component
@Getter
@Setter
public class StorageProperties {
    private String location;
    private String baseUrl;
}
