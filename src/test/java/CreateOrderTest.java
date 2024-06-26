import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.example.courier.CourierChecks;
import org.example.courier.CourierClient;
import org.example.order.Order;
import org.example.order.OrderClient;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {null}
        });
    }

    private List<String> colors;
    int courierId;
    public CreateOrderTest(List<String> colors) {
        this.colors = colors;
    }
    private final CourierClient client = new CourierClient();
    private final CourierChecks check = new CourierChecks();


    @Test
    @DisplayName("Тест на создание заказа")
    @Description("Тест на создание заказа с различными вариантами цвета")
    @Step("Создание заказа с именем, фамилией, адресом, количеством, телефоном, методом оплаты, датой доставки, комментариями и цветами; проверка ответа")
    public void testCreateOrder() {

        Order order = new Order("Tom",
                "Smite",
                "Testovaya st. apt 4",
                4,
                "+7(997)131-87-85",
                9,
                "08-12-2024",
                "no comments",
                colors);


        Response response = OrderClient.postOrder(order);
        OrderClient.validateResponseStatus(response, HttpURLConnection.HTTP_CREATED);
        int orderId = OrderClient.getOrderId(response);
    }

    @After
    public void deleteCourier() {
        if (courierId != 0) {
            ValidatableResponse response = client.deleteCourier(courierId);
            check.deletedSuccesfully(response);
        }
    }
}
