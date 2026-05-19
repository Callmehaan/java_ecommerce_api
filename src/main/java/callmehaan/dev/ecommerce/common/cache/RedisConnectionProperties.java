package callmehaan.dev.ecommerce.common.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("app.redis")
@Component
@Getter
@Setter
public class RedisConnectionProperties {
    private String host;
    private String port;
}
