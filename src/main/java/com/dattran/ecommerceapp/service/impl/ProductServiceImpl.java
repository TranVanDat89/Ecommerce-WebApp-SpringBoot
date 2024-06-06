package com.dattran.ecommerceapp.service.impl;

import com.dattran.ecommerceapp.aws.S3Service;
import com.dattran.ecommerceapp.dto.ProductDTO;
import com.dattran.ecommerceapp.dto.response.WishListResponse;
import com.dattran.ecommerceapp.entity.*;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.exception.AppException;
import com.dattran.ecommerceapp.mapper.EntityMapper;
import com.dattran.ecommerceapp.repository.*;
import com.dattran.ecommerceapp.service.IProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements IProductService {
    @NonFinal
    private static final int MAX_SIZE_OF_IMAGE_10MB = 10 * 1024 * 1024;
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    FlavorRepository flavorRepository;
    UserRepository userRepository;
    EntityMapper entityMapper;
    ProductImageRepository productImageRepository;
    WishListRepository wishListRepository;
    S3Service s3Service;
    @Transactional
    @Override
    public Product createProduct(ProductDTO productDTO) {
        if (productRepository.existsByName(productDTO.getName().trim())) {
            throw new AppException(ResponseStatus.PRODUCT_EXISTED);
        }
        Product product = entityMapper.toProduct(productDTO);
        Optional<Category> optionalCategory = categoryRepository.findByName(productDTO.getCategoryName());
        Category category = optionalCategory.orElseGet(() -> categoryRepository.save(Category.builder().name(productDTO.getCategoryName().trim()).build()));
        product.setCategory(category);
        String flavors = productDTO.getFlavors();
        Ingredient ingredient = entityMapper.toIngredient(productDTO);
        StringTokenizer stringTokenizer = new StringTokenizer(flavors, ",");
        Set<Flavor> flavorSet = new HashSet<>();
        while (stringTokenizer.hasMoreTokens()) {
            String currentFlavor = stringTokenizer.nextToken().trim();
            Optional<Flavor> optionalFlavor = flavorRepository.findByName(currentFlavor);
            Flavor flavor = optionalFlavor.orElseGet(() -> flavorRepository.save(Flavor.builder().name(currentFlavor).build()));
            flavorSet.add(flavor);
        }
        ingredient.setFlavors(flavorSet);
        product.setIngredient(ingredient);
        ProductDetail productDetail = entityMapper.toProductDetail(productDTO);
        product.setProductDetail(productDetail);
        return productRepository.save(product);
    }
    @Transactional
    @Override
    public List<ProductImage> uploadImages(String productId, List<MultipartFile> files) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ResponseStatus.PRODUCT_NOT_FOUND));
        if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new AppException(ResponseStatus.PRODUCT_IMAGES_OVERLOAD);
        }
        List<ProductImage> productImages = new ArrayList<>();
        for (MultipartFile file : files) {
            if(file.getSize() == 0) {
                continue;
            }
            if (file.getSize() > MAX_SIZE_OF_IMAGE_10MB) {
                throw new AppException(ResponseStatus.IMAGE_SIZE_TOO_LARGE);
            }
            String contentType = file.getContentType();
            if(contentType == null || !contentType.startsWith("image/")) {
                throw new AppException(ResponseStatus.UNSUPPORTED_FILE);
            }
            String path = s3Service.uploadFile(file, "product-images/");
            ProductImage productImage = ProductImage.builder().product(product).imageUrl(path).build();
            productImages.add(productImageRepository.save(productImage));
        }
//        Set first image of list images to thumbnail of product
        product.setThumbnail(productImages.get(0).getImageUrl());
        productRepository.save(product);
        return productImages;
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        // Lấy danh sách sản phẩm theo trang (page), giới hạn (limit), và categoryId (nếu có)
        return productRepository.findAll(pageable);
    }
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(()->new AppException(ResponseStatus.PRODUCT_NOT_FOUND));
    }

    @Override
    public List<Product> findTop4() {
        return productRepository.findTop4ByOrderByCreatedAtDesc();
    }

    @Override
    public WishList addToWishList(String userId, String productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new AppException(ResponseStatus.USER_NOT_FOUND));
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new AppException(ResponseStatus.PRODUCT_NOT_FOUND));
        if (wishListRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new AppException(ResponseStatus.PRODUCT_ALREADY_EXIST_IN_WISHLIST);
        }
        WishList wishList = WishList.builder()
                .user(user)
                .product(product)
                .build();
        return wishListRepository.save(wishList);
    }

    @Override
    public List<WishListResponse> getAllFavorites(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ResponseStatus.USER_NOT_FOUND);
        }
        List<WishList> wishLists = wishListRepository.findByUserId(userId);
        List<WishListResponse> wishListResponses = new ArrayList<>();
        wishLists.forEach(
                wishList -> {
                    WishListResponse wishListResponse = WishListResponse.builder()
                            .id(wishList.getId())
                            .product(wishList.getProduct())
                            .build();
                    wishListResponses.add(wishListResponse);
                }
        );
        return wishListResponses;
    }

    @Override
    public List<WishList> getAllFavoriteProducts(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ResponseStatus.USER_NOT_FOUND);
        }
        return wishListRepository.findByUserId(userId);
    }

    @Override
    public List<Product> getAllProductByCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ResponseStatus.CATEGORY_NOT_FOUND));
        List<Product> products = productRepository.findByCategoryId(categoryId);
        if (!products.isEmpty()) {
            return products;
        }
        return List.of();
    }

    @Override
    public Map<String, Long> countByCategory() {
        Map<String, Long> result = new HashMap<>();
        List<Category> categories = categoryRepository.findAll();
        categories.forEach(category -> result.put(category.getName(), productRepository.countByCategoryId(category.getId())));
        return result;
    }
}
