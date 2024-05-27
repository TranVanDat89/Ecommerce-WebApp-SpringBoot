package com.dattran.ecommerceapp.service;

import com.dattran.ecommerceapp.dto.ProductDTO;
import com.dattran.ecommerceapp.entity.Product;
import com.dattran.ecommerceapp.entity.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO);
    List<ProductImage> uploadImages(String productId, List<MultipartFile> files);
    Page<Product> getAllProducts(Pageable pageable);
    List<Product> getAllProducts();
    Product getProductById(String id);
    List<Product> findTop4();
}
