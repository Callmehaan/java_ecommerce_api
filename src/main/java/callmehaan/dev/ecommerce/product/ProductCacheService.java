package callmehaan.dev.ecommerce.product;

import callmehaan.dev.ecommerce.product.dto.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class ProductCacheService {
    private static final Logger log = LoggerFactory.getLogger(ProductCacheService.class);
    private final RedisTemplate<String, ProductDto> redisTemplate;

    public ProductCacheService(RedisTemplate<String, ProductDto> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public ProductDto getProduct(UUID productId) {
        return redisTemplate.opsForValue().get(String.format("product:%s", productId));
    }

    @Async("cacheExecutor")
    public void cacheProduct(ProductDto productCacheDto) {
        redisTemplate.opsForValue().set(
                String.format("product:%s", productCacheDto.id()), productCacheDto, Duration.ofMinutes(30)
        );
        log.info("Created cache product: {}", productCacheDto);
    }

    @Async("cacheExecutor")
    public void deleteCachedProduct(UUID productId) {
        redisTemplate.delete(String.format("product:%s", productId));
        log.info("Deleted cache product id: {}", productId);
    }
}
