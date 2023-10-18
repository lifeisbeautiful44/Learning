package citytech.global.platform.exception;

public enum ExceptionType {

    EMAIL_SEND_FAILED("EM101","Email Send Failed")
    ;

    private String code;
    private String message;


    ExceptionType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
