package generator;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import pojo.OrderForm;

public class OrderFormGenerator {
    public String field = RandomStringUtils.randomAlphabetic(10);
    public int number = RandomUtils.nextInt();

    public OrderForm getRandomWithColor(String[] color) {
        return new OrderForm(field, field, field, field, field, number, "2020-06-06", field, color);
    }

}
