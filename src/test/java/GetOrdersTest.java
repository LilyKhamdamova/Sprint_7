import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.example.courier.Courier;
import org.example.courier.CourierChecks;
import org.example.courier.CourierClient;
import org.example.courier.CourierCredentials;
import org.example.order.OrderClient;
import org.junit.Before;
import org.junit.Test;

public class GetOrdersTest {
    private final CourierChecks check = new CourierChecks();
    private final CourierClient client = new CourierClient();
    int courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/api/v1";
    }

    @Test
    @DisplayName("Получение списка заказов /orders")
    @Description("Позитивный тест получения заказов /orders")
    public void testGetOrders() {
        Response response = OrderClient.getOrdersWithoutCourierId();
        OrderClient.responseIsSuccess(response);
        System.out.println(response.body().asString());
    }

    @Test
    public void testGetOrdersByCourierIdWithValidId() {
        var courier = Courier.generic();
        var creds = CourierCredentials.from(courier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        check.loggedInSuccessfully(loginResponse);
        courierId = check.loggedInSuccessfully(loginResponse);
        Response ordersResponse = OrderClient.getOrdersByCourierId(courierId);
        OrderClient.responseIsSuccess(ordersResponse);
        System.out.println(ordersResponse.body().asString());
    }

    @Test
    public void testGetAvailableOrders() {

        Response response = OrderClient.getAvailableOrders(10, 0);
        OrderClient.bodySizeIsMoreThen10(response);
        System.out.println(response.body().asString());
    }

    @Test
    public void testGetOrdersNearMetroStation() {
        Response response = OrderClient.getOrdersNearMetroStation(10, 0, "[\"110\"]");
        OrderClient.bodySizeIsMoreThen10(response);
        System.out.println(response.body().asString());
    }

    @Test
    public void testGetOrdersAtStations() {
        var courier = Courier.generic();
        var creds = CourierCredentials.from(courier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        check.loggedInSuccessfully(loginResponse);
        courierId = check.loggedInSuccessfully(loginResponse);
        Response ordersResponse = OrderClient.getOrdersAtStations(courierId, "[\"1\", \"2\"]");
        OrderClient.responseIsSuccess(ordersResponse);
        System.out.println(ordersResponse.body().asString());
    }
}
