import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.example.courier.Courier;
import org.example.courier.CourierChecks;
import org.example.courier.CourierClient;
import org.example.courier.CourierCredentials;
import org.example.order.OrderClient;
import org.junit.Test;

public class GetOrdersTest {
    private final CourierChecks check = new CourierChecks();
    private final CourierClient client = new CourierClient();
    int courierId;

    @Test
    @DisplayName("Получение списка заказов /orders")
    @Description("Позитивный тест получения заказов /orders")
    public void testGetOrders() {
        Response response = OrderClient.getOrdersWithoutCourierId();
        OrderClient.responseIsSuccess(response);
        System.out.println(response.body().asString());
    }

    @Test
    @DisplayName("Получение списка заказов курьера по его ID")
    @Description("Позитивный тест получения списка заказов курьера по его ID")
    public void testGetOrdersByCourierIdWithValidId() {
        var courier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.createdSuccessfully(createResponse);

        var creds = CourierCredentials.from(courier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        check.loggedInSuccessfully(loginResponse);
        courierId = check.loggedInSuccessfully(loginResponse);
        Response ordersResponse = OrderClient.getOrdersByCourierId(courierId);
        OrderClient.responseIsSuccess(ordersResponse);
        System.out.println(ordersResponse.body().asString());
    }

    @Test
    @DisplayName("Получение доступных заказов")
    @Description("Позитивный тест получения доступных заказов")
    public void testGetAvailableOrders() {
        Response response = OrderClient.getAvailableOrders(10, 0);
        OrderClient.bodySizeIsMoreThen10(response);
        System.out.println(response.body().asString());
    }

    @Test
    @DisplayName("Получение заказов рядом с заданным метро")
    @Description("Позитивный тест получения заказов рядом с заданным метро")
    public void testGetOrdersNearMetroStation() {
        Response response = OrderClient.getOrdersNearMetroStation(10, 0, "[\"110\"]");
        OrderClient.bodySizeIsMoreThen10(response);
        System.out.println(response.body().asString());
    }

    @Test
    @DisplayName("Получение заказов на станциях метро")
    @Description("Позитивный тест получения заказов на определенных станциях метро для конкретного курьера")
    public void testGetOrdersAtStations() {
        var courier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.createdSuccessfully(createResponse);

        var creds = CourierCredentials.from(courier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        check.loggedInSuccessfully(loginResponse);
        courierId = check.loggedInSuccessfully(loginResponse);
        Response ordersResponse = OrderClient.getOrdersAtStations(courierId, "[\"1\", \"2\"]");
        OrderClient.responseIsSuccess(ordersResponse);
        System.out.println(ordersResponse.body().asString());
    }
}
