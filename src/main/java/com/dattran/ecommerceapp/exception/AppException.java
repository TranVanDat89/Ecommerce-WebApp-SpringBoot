package com.dattran.ecommerceapp.exception;

import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException{
    private ResponseStatus responseStatus;
    public AppException(ResponseStatus responseStatus) {
        super(responseStatus.getMessage());
        this.responseStatus = responseStatus;
    }
}
