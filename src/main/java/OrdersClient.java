import static io.restassured.RestAssured.given;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.OrderForm;

public class OrdersClient extends ScooterRestClient {

    private static final String ORDER_PATH = "/api/v1/orders";

    @Step("Send POST Request /api/v1/orders")
    public ValidatableResponse createOrder(OrderForm body) {
        return given()
            .spec(getBaseSpec())
            .body(body)
            .when()
            .post(ORDER_PATH)
            .then();
    }

    @Step("Send GET Request /api/v1/orders")
    public ValidatableResponse getOrdersList() {
        return given()
            .spec(getBaseSpec())
            .when()
            .get(ORDER_PATH)
            .then();
    }

}
