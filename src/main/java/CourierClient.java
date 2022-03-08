import static io.restassured.RestAssured.given;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.LoginForm;
import pojo.RegisterForm;

public class CourierClient extends ScooterRestClient {

    private static final String COURIER_PATH = "/api/v1/courier";

    @Step("Send POST Request /api/v1/courier")
    public ValidatableResponse createCourier(RegisterForm body) {
        return given()
            .spec(getBaseSpec())
            .body(body)
            .when()
            .post(COURIER_PATH)
            .then();
    }

    @Step("Send POST Request /api/v1/courier/login")
    public ValidatableResponse loginCourier(LoginForm body) {
        return given()
            .spec(getBaseSpec())
            .body(body)
            .when()
            .post(COURIER_PATH + "/login")
            .then();
    }

}
