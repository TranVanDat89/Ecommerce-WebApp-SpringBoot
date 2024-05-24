package com.dattran.ecommerceapp.service;

import com.dattran.ecommerceapp.dto.ProductDTO;
import com.dattran.ecommerceapp.entity.Product;
import com.dattran.ecommerceapp.entity.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO);
    List<ProductImage> uploadImages(String productId, List<MultipartFile> files);
}
