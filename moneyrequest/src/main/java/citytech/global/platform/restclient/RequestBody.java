package citytech.global.platform.restclient;

public class RequestBody {

    private String email;
    private String password;


    public RequestBody() {
    }

    public RequestBody(String userName, String password) {
        this.email = userName;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String userName) {
        this.email = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RequestBody{" +
                "userName='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
