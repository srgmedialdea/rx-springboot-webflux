package com.rxspringboot.fluxmonoplayground;

public class CustomException extends Throwable {


    private String message;

    public CustomException(Throwable e) {
        this.message = e.getMessage();
    }
}
