package citytech.global.platform.exception;

public enum MoneyRequestExceptionType {

    EMAIL_ALREADY_EXIST("EX110","failed","Email address already taken"),
    INVALID_EMAIL_FORMAT("EX111","failed","Invalid Email "),
    INVALID_CREDENTIAL("EX112", "failed", "Invalid credential"),
    USER_NOT_FOUND("EX113","failed","No user found"),

    ;

    private String code;
    private String message;
    private String data;

    MoneyRequestExceptionType(String code, String message, String data) {
        this.code = code;
        this.message = message;
        this.data = data;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ExceptionType{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
