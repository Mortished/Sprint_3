package pojo;

public class RegisterForm {

    private String login;
    private String password;
    private String firstName;

    public RegisterForm() {}

    public RegisterForm(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

}
