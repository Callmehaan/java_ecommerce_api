package callmehaan.dev.ecommerce.common.cache;

import callmehaan.dev.ecommerce.product.dto.ProductDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class RedisConfig {
    private final RedisConnectionProperties redisConnectionProperties;

    public RedisConfig(RedisConnectionProperties redisConnectionProperties) {
        this.redisConnectionProperties = redisConnectionProperties;
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        final RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisConnectionProperties.getHost());
        configuration.setPort(Integer.parseInt(redisConnectionProperties.getPort()));

        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, ProductDto> redisTemplate() {

        RedisTemplate<String, ProductDto> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(lettuceConnectionFactory());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        redisTemplate.setValueSerializer(new JacksonJsonRedisSerializer<ProductDto>(ProductDto.class));
        redisTemplate.setHashValueSerializer(new JacksonJsonRedisSerializer<ProductDto>(ProductDto.class));

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    public TaskExecutor cacheExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("cache-async-");
        executor.initialize();

        return executor;
    }
}
