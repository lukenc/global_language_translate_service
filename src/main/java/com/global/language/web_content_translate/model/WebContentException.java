package com.global.language.web_content_translate.model;

public enum WebContentException {
    Duplicate(-2,"数据重复，检查数据");
    ;
    int code;
    String  message;
    WebContentException(int code,String message){
        this.code=code;
        this.message=message;
    }
    public static WebContentException getException(int code){
        for (WebContentException exception:values()){
            if (exception.code==code){
                return exception;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
