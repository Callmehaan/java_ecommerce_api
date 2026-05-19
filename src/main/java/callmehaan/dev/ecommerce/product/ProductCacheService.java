package callmehaan.dev.ecommerce.product;

import callmehaan.dev.ecommerce.product.dto.ProductDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductCacheService {
    private final RedisTemplate<String, ProductDto> redisTemplate;

    public ProductCacheService(RedisTemplate<String, ProductDto> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public ProductDto getProduct(UUID productId) {
        return redisTemplate.opsForValue().get(String.format("product:%s", productId));
    }

    public void putProduct(ProductDto productCacheDto) {
        redisTemplate.opsForValue().set(String.format("product:%s", productCacheDto.id()), productCacheDto);
    }
}
