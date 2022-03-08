import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pojo.LoginForm;
import pojo.RegisterForm;

@Story("POST /api/v3/courier - Логин курьера")
public class LoginCourierTest {
    private String field = RandomStringUtils.randomAlphabetic(10);
    private CourierClient courierClient;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Успешный логин - 200 ОК")
    @Description("Проверка успешного логина при существующих валидных данных УЗ")
    public void loginWithValidCredits() {
        courierClient.createCourier(new RegisterForm(field, field, field));
        ValidatableResponse response = courierClient.loginCourier(new LoginForm(field, field));
        checkSuccesLogin(response);
    }

    @Test
    @DisplayName("Запрос без логина - 400 Bad Request")
    @Description("Проверка валидации при попытке авторизации без логина")
    public void loginWithoutLogin() {
        ValidatableResponse response = courierClient.loginCourier(new LoginForm(null, field));
        checkFailLoginWithoutField(response);
    }

    @Test
    @DisplayName("Запрос без пароля - 400 Bad Request")
    @Description("Проверка валидации при попытке авторизации без пароля")
    public void loginWithoutPassword() {
        ValidatableResponse response = courierClient.loginCourier(new LoginForm(field, null));
        checkFailLoginWithoutField(response);
    }

    @Test
    @DisplayName("Запрос без логина & пароля - 400 Bad Request")
    @Description("Проверка валидации при попытке авторизации без логина & пароля")
    public void loginWithoutAccountCredits() {
        ValidatableResponse response = courierClient.loginCourier(new LoginForm(null, null));
        checkFailLoginWithoutField(response);
    }

    @Test
    @DisplayName("Запрос c невалидной УЗ - 404 Not Found")
    @Description("Проверка авторизации с несуществующей парой: логин + пароль")
    public void loginWithFakeAccount() {
        ValidatableResponse response = courierClient.loginCourier(new LoginForm(field, field));
        checkLoginWithFakeAccount(response);
    }

    @Step("Проверка ответа метода - 200 ОК")
    private void checkSuccesLogin(ValidatableResponse response) {
        int id = response.extract().path("id");

        Assert.assertEquals("Courier cannot login!", SC_OK, response.extract().statusCode());
        Assert.assertTrue("Courier ID is incorrect", id > 0);
    }

    @Step("Проверка ответа при авторизации без обязательного параметра - 400 Bad Request")
    private void checkFailLoginWithoutField(ValidatableResponse response) {
        String expectedMessage = "Недостаточно данных для входа";
        String actualMessage = response.extract().path("message");

        Assert.assertEquals("Wrong status code!", SC_BAD_REQUEST, response.extract().statusCode());
        Assert.assertEquals("Wrong message!", expectedMessage, actualMessage);
    }

    @Step("Проверка ответа метода - 404 Not Found")
    private void checkLoginWithFakeAccount(ValidatableResponse response) {
        String expectedMessage = "Учетная запись не найдена";
        String actualMessage = response.extract().path("message");
        int code = response.extract().path("code");

        Assert.assertEquals("Wrong status code!", SC_NOT_FOUND, response.extract().statusCode());
        Assert.assertEquals("Wrong code!", SC_NOT_FOUND, code);
        Assert.assertEquals("Wrong message!", expectedMessage, actualMessage);
    }

}
