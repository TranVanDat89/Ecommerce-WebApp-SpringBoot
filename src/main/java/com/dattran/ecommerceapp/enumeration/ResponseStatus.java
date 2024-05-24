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
    UNSUPPORTED_FILE(406, "File is not image.")
    ;
    private final int code;
    private final String message;
}
