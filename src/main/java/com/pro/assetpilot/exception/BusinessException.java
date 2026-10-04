package com.pro.assetpilot.exception;


import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException{

    private HttpStatus status;

    public BusinessException(HttpStatus status,String message){
        super(message);
        this.status =  status;
    }
    public HttpStatus status(){
        return status;
    }

}
