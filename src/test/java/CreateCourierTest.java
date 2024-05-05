import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.example.Client;
import org.example.courier.CourierChecks;
import org.example.courier.CourierClient;
import org.example.courier.CourierCredentials;
import org.example.courier.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertNotEquals;

public class CreateCourierTest {
    private final CourierClient client = new CourierClient();
    private final CourierChecks check = new CourierChecks();
    int courierId;

    @After
    public void deleteCourier() {
        if (courierId != 0) {
            ValidatableResponse response = client.deleteCourier(courierId);
            check.deletedSuccesfully(response);
        }
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = Client.BASE_URI;
    }

    @Test
    @DisplayName("Создание курьера /courier")
    @Description("Тест на успешное создание курьера")
    public void testCreateCourierSuccess() {
        var courier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.createdSuccessfully(createResponse);

        var creds = CourierCredentials.from(courier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        courierId = check.loggedInSuccessfully(loginResponse);

        assertNotEquals(0, courierId);
    }

    @Test
    @DisplayName("Создание курьера без логина /courier")
    @Description("Тест на создание курьера без указания логина")
    public void testCreateCourierNoLogin() {
        Courier requestBody = new Courier();
        requestBody.setPassword("1234");
        requestBody.setFirstName("Test");
        ValidatableResponse createResponse = client.createCourier(requestBody);
        CourierChecks.badRequestCheck(createResponse);
    }

    @Test
    @DisplayName("Создание курьера без пароля /courier")
    @Description("Тест на создание курьера без указания пароля")
    public void testCreateCourierNoPassword() {
        Courier requestBody = new Courier();
        requestBody.setLogin("kham" + new Random().nextInt(100));
        requestBody.setFirstName("Test");

        ValidatableResponse createResponse = client.createCourier(requestBody);
        CourierChecks.badRequestCheck(createResponse);
    }

    @Test
    @DisplayName("Создание курьера. Дубликат /courier")
    @Description("Тест на создание курьера с дублирующимися учетными данными")
    public void testCreateCourierDuplicate() {
        var courier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.createdSuccessfully(createResponse);

        ValidatableResponse createResponseDuplicate = client.createCourier(courier);
        check.conflictCheck(createResponseDuplicate);

        var creds = CourierCredentials.from(courier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        courierId = check.loggedInSuccessfully(loginResponse);
    }

    @Test
    @DisplayName("Создание курьера с дублирующимся логином /courier")
    @Description("Тест на создание курьера с дублирующимся логином")
    public void testCreateCourierDuplicateLogin() {
        var courier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.createdSuccessfully(createResponse);

        courier.setFirstName("Test");
        ValidatableResponse createDuplicateResponse = client.createCourier(courier);
        check.conflictCheck(createDuplicateResponse);

        var creds = CourierCredentials.from(courier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        courierId = check.loggedInSuccessfully(loginResponse);
    }
}
