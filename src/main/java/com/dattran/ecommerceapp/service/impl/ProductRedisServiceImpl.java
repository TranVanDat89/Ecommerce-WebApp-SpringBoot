package com.dattran.ecommerceapp.service.impl;

import com.dattran.ecommerceapp.entity.Product;
import com.dattran.ecommerceapp.service.IProductRedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductRedisServiceImpl implements IProductRedisService {
//    RedisTemplate<String, Object> redisTemplate;
//    ObjectMapper redisObjectMapper;
//    @NonFinal
//    @Value("${spring.data.redis.use-redis-cache}")
//    boolean useRedisCache;
//    @Override
//    public void clear() {
//        redisTemplate.getConnectionFactory().getConnection().flushAll();
//    }
//
//    @Override
//    public List<Product> getAllProducts() throws JsonProcessingException {
//        if (useRedisCache) {
//            String key = "all_products";
//            String json = (String) redisTemplate.opsForValue().get(key);
//            List<Product> products = json != null
//                    ? redisObjectMapper.readValue(json, new TypeReference<List<Product>>() {})
//                    :null;
//            return products;
//        }
//        return null;
//    }
//
//    @Override
//    public void saveAll(List<Product> products) throws JsonProcessingException {
//        if (useRedisCache) {
//            String key = "all_products";
//            String json = redisObjectMapper.writeValueAsString(products);
//            redisTemplate.opsForValue().set(key, json);
//        }
//    }
}
