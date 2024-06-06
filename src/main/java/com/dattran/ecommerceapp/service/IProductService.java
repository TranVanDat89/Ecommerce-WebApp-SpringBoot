package com.dattran.ecommerceapp.service;

import com.dattran.ecommerceapp.dto.ProductDTO;
import com.dattran.ecommerceapp.dto.response.WishListResponse;
import com.dattran.ecommerceapp.entity.Product;
import com.dattran.ecommerceapp.entity.ProductImage;
import com.dattran.ecommerceapp.entity.WishList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IProductService {
    Product createProduct(ProductDTO productDTO);
    List<ProductImage> uploadImages(String productId, List<MultipartFile> files);
    Page<Product> getAllProducts(Pageable pageable);
    List<Product> getAllProducts();
    Product getProductById(String id);
    List<Product> findTop4();
    WishList addToWishList(String userId, String productId);
    List<WishListResponse> getAllFavorites(String userId);
    List<WishList> getAllFavoriteProducts(String userId);
    List<Product> getAllProductByCategory(String categoryId);
    Map<String, Long> countByCategory();
}
