package citytech.global.platform.exception;


import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Introspected
public class MoneyRequestException extends RuntimeException{
    private String code;
    private String message;
    private String data;

    public MoneyRequestException(String code, String message , String data) {
        super(message);
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public MoneyRequestException(MoneyRequestExceptionType type)
    {
       this.code = type.getCode();
       this.message = type.getMessage();
       this.data = type.getData();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
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
        return "CustomException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
