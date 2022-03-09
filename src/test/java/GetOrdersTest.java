import static org.apache.http.HttpStatus.SC_OK;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Test;

@Story("GET /api/v3/orders - Получение списка заказов")
public class GetOrdersTest {

    @Test
    @DisplayName("Получение списка заказов - 200 ОК")
    @Description("Проверка получения списка заказов без параметров")
    public void checkGetOrdersList() {
        ValidatableResponse response = new OrdersClient().getOrdersList();
        checkResponse(response);
    }

    @Step("Проверка наличия заказов в ответе")
    private void checkResponse(ValidatableResponse response) {
        int total = response.extract().path("pageInfo.total");
        Assert.assertEquals("Wrong status code!", SC_OK, response.extract().statusCode());
        Assert.assertTrue("Wrong orders count!", total > 0);
    }

}
