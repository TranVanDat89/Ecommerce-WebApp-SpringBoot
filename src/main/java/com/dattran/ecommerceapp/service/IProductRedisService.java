package com.dattran.ecommerceapp.service;

import com.dattran.ecommerceapp.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface IProductRedisService {
    void clear();
    List<Product> getAllProducts() throws JsonProcessingException;
    void saveAll(List<Product> products) throws JsonProcessingException;
}
