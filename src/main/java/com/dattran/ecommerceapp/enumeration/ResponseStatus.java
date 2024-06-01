package com.dattran.ecommerceapp.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {
    PHONE_NUMBER_EXISTED(401, "Phone number is existed."),
    ROLE_INVALID(501, "Invalid role."),
    USER_CREATED(201, "User created successfully."),
    LOGIN_SUCCESSFULLY(200, "Login successfully."),
    USER_NOT_FOUND(404, "User not found."),
    PASSWORD_NOT_MATCH(405, "Password not match"),
    PRODUCT_CREATED(201, "Product created successfully."),
    UPLOAD_IMAGES_SUCCESSFULLY(201, "Upload images successfully."),
    PRODUCT_NOT_FOUND(404, "Product not found."),
    PRODUCT_IMAGES_OVERLOAD(405, "Cannot upload over 6 images."),
    IMAGE_SIZE_TOO_LARGE(405, "Image size must less than 10MB."),
    UNSUPPORTED_FILE(406, "File is not image."),
    ROLE_NOT_FOUND(404, "Role not found."),
    PRODUCT_EXISTED(405, "Product existed."),
    ORDER_CREATED(201, "Order created successfully."),
    GET_ALL_PRODUCTS_SUCCESSFULLY(200, "Get all product successfully."),
    GET_ALL_CATEGORIES_SUCCESSFULLY(200, "Get all categories successfully."),
    TOKEN_EXPIRED(406, "Token is expired."),
    GET_COMMENTS_SUCCESSFULLY(200, "Get all comments successfully."),
    COMMENT_CREATED_FAILED(406, "Comment created failed, you must buy this product to comment."),
    COMMENT_ONLY_ONE(407, "You can comment only one time."),
    PRODUCT_ALREADY_EXIST_IN_WISHLIST(407, "Product already exists in wish list."),
    WISH_LIST_CREATED(201, "Wish list created successfully."),
    ORDER_NOT_FOUND(404, "Order not found."),
    ORDER_DETAIL_NOT_FOUND(404, "Order detail not found."),
    GET_ORDER_DETAIL_SUCCESSFULLY(200, "Get order detail successfully."),
    GET_USERS_SUCCESS(200, "Get users successfully."),
    ARTICLE_INVALID(408, "Content of article is empty or too short."),
    ARTICLE_CREATED(200, "Created article successfully"),
    GET_ALL_ARTICLE_SUCCESS(200, "Get all articles success."),
    ARTICLE_NAME_EXISTED(405, "Article name existed."),
    CART_NOT_FOUND(404, "Cart not found."),
    ;
    private final int code;
    private final String message;
}
