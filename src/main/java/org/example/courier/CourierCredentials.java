package org.example.courier;


public class CourierCredentials {
    private String login;
    private String password;

    public static CourierCredentials from(Courier requestBody) {
        return new CourierCredentials(requestBody.getLogin(), requestBody.getPassword());
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CourierCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public CourierCredentials() {
    }
    public CourierCredentials(String password) {
        this.password = password;
    }
}

