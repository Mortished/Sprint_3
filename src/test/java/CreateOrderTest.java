import static org.apache.http.HttpStatus.SC_CREATED;

import generator.OrderFormGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.OrderForm;

@RunWith(Parameterized.class)
@Story("POST /api/v3/orders - Создание заказа")
public class CreateOrderTest {

    private String[] color;
    private OrdersClient ordersClient;
    private OrderForm orderForm;

    public CreateOrderTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] createDifferentColor() {
        return new Object[][]{
            {new String[] {"BLACK"}},
            {new String[] {"GRAY"}},
            {new String[] {"BLACK", "GRAY"}},
            {new String[] {null}},
        };
    }

    @Before
    public void setUp() {
        ordersClient = new OrdersClient();
        orderForm = new OrderFormGenerator().getRandomWithColor(color);
    }

    @Test
    @DisplayName("Успешное создание заказа")
    @Description("Создание заказа с различными цветами и их отсутствием")
    public void checkCreateOrderWithColor() {
        ValidatableResponse response = ordersClient.createOrder(orderForm);
        checkResponse(response);
    }

    @Step("Проверка ответа метода - 201 Created")
    private void checkResponse(ValidatableResponse response) {
        int actual = response.extract().path("track");
        Assert.assertEquals("Order didnt created!", SC_CREATED, response.extract().statusCode());
        Assert.assertTrue("Wrong track",actual > 0);
    }

}
