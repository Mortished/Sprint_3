import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_CREATED;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pojo.RegisterForm;

@Story("POST /api/v3/courier - Создание курьера")
public class CreateNewCourierTest {

    private String field = RandomStringUtils.randomAlphabetic(10);
    private CourierClient courierClient;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Успешное создание УЗ - 201")
    @Description("Проверка успешного создания УЗ. Status = 201 Created, ResponseBody: ok = true")
    public void positiveCreateCourier() {
        ValidatableResponse response = courierClient.createCourier(new RegisterForm(field, field, field));
        checkPositiveResponse(response);
    }

    @Test
    @DisplayName("Запрос без логина - 400")
    @Description("Чтобы создать курьера, нужно передать в ручку все обязательные поля. "
        + "Если одного из полей нет, запрос возвращает ошибку - 400"
        + "& message: Недостаточно данных для создания учетной записи")
    public void negativeCreateCourierWithoutLoginField() {
        ValidatableResponse response = courierClient.createCourier(new RegisterForm(null, field, field));
        checkWithoutFieldFail(response);
    }

    @Test
    @DisplayName("Запрос без пароля - 400")
    @Description("Чтобы создать курьера, нужно передать в ручку все обязательные поля. "
        + "Если одного из полей нет, запрос возвращает ошибку - 400"
        + "& message: Недостаточно данных для создания учетной записи")
    public void negativeCreateCourierWithoutPasswordField() {
        ValidatableResponse response = courierClient.createCourier(new RegisterForm(field, null, field));
        checkWithoutFieldFail(response);
    }

    @Test
    @DisplayName("Проверка создания без поля:firstName - 201")
    @Description("Проверка создания с отсутствием опционального параметра - firstName")
    public void createCourierWithoutFirstNameField() {
        ValidatableResponse response = courierClient.createCourier(new RegisterForm(field, field, null));
        checkPositiveResponse(response);
    }

    @Test
    @DisplayName("Запрос с повторяющимся логином - 409")
    @Description("Проверка создания УЗ с повторяющимся логином")
    public void createCourierWithRepeatLogin() {
        courierClient.createCourier(new RegisterForm(field, field, field));
        ValidatableResponse response = courierClient.createCourier(new RegisterForm(field, field, field));
        checkRepeatLoginResponse(response);
    }

    @Step("Проверка ответа метода - 201 & ok:true")
    private void checkPositiveResponse(ValidatableResponse response) {
        boolean actual = response.extract().path("ok");

        Assert.assertEquals("Courier cannot create login", SC_CREATED, response.extract().statusCode());
        Assert.assertEquals("Wrong body!", Boolean.TRUE, actual);
    }

    @Step("Проверка ответа метода - 400 & message = Недостаточно данных для создания учетной записи")
    private void checkWithoutFieldFail(ValidatableResponse response) {
        String expectedMessage = "Недостаточно данных для создания учетной записи";
        String actualMessage = response.extract().path("message");
        int actualCode = response.extract().path("code");

        Assert.assertEquals("Wrong status code!", SC_BAD_REQUEST, response.extract().statusCode());
        Assert.assertEquals("Wrong code!", SC_BAD_REQUEST, actualCode);
        Assert.assertEquals("Wrong message!", expectedMessage, actualMessage);
    }

    @Step("Проверка ответа метода - 409 & message:Этот логин уже используется")
    private void checkRepeatLoginResponse(ValidatableResponse response) {
        String expectedMessage = "Этот логин уже используется. Попробуйте другой.";
        String actualMessage = response.extract().path("message");
        int code = response.extract().path("code");

        Assert.assertEquals("Wrong status code!", SC_CONFLICT, response.extract().statusCode());
        Assert.assertEquals("Wrong code!", SC_CONFLICT, code);
        Assert.assertEquals("Wrong message!", expectedMessage, actualMessage);
    }

}
