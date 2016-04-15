package tw.openedu.www.model.api;

public class SocialLoginResponse {

    public String error;
    public String json;
    public String cookie;
    
    public boolean isSuccess() {
        return (error == null && cookie != null);
    }

    public boolean isAccountNotLinked() {
        return (error != null && error.equals("401"));
    }
}
