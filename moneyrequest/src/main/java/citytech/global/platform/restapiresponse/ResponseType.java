package citytech.global.platform.restapiresponse;

public enum ResponseType {
    USER_CREATED_SUCCESSFULLY("User has been successfully created.");


    private String data;

    ResponseType(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseType{" +
                "data='" + data + '\'' +
                '}';
    }
}
