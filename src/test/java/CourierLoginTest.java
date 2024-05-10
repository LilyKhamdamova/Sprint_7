import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.courier.Courier;
import org.example.courier.CourierChecks;
import org.example.courier.CourierClient;
import org.example.courier.CourierCredentials;
import org.junit.After;
import org.junit.Test;

public class CourierLoginTest {


    private final CourierChecks check = new CourierChecks();
    private final CourierClient client = new CourierClient();
    int courierId;

    @After
    public void deleteCourier() {
        if (courierId != 0) {
            ValidatableResponse response = client.deleteCourier(courierId);
            check.deletedSuccesfully(response);
        }
    }


    @Test
    @DisplayName("Вход с правильными учетными данными /courier/login")
    @Description("Проверка успешного входа курьера с правильными учетными данными")

    public void testCourierLoginSuccess() {
        var courier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.createdSuccessfully(createResponse);

        var creds = CourierCredentials.from(courier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        courierId = check.loggedInSuccessfully(loginResponse);
    }

    @Test
    @DisplayName("Вход с неправильным паролем /courier/login")
    @Description("Проверка сообщения об ошибке при входе с неправильным паролем")
    public void testCourierWrongPassword() {

        var creds = new CourierCredentials("kham_test", "wrong");
        ValidatableResponse loginResponse = client.loginCourier(creds);
        check.notFoundLogin(loginResponse);
    }

    @Test
    @DisplayName("Вход с несуществующим аккаунтом /courier/login")
    @Description("Проверка сообщения об ошибке при входе с несуществующим аккаунтом")

    public void testCourierLoginAccountNotFound() {
        var creds = new CourierCredentials("nologin");
        ValidatableResponse loginResponse = client.loginCourier(creds);
        check.badRequestLogin(loginResponse);
    }

    @Test
    @DisplayName("Вход без логина /courier/login")
    @Description("Проверка сообщения об ошибке при входе без указания логина")
    public void testCourierLoginNoLogin() {
        var courier = Courier.random();
        var creds = CourierCredentials.from(courier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        check.notFoundLogin(loginResponse);
    }

//    @Test
//    @DisplayName("Вход без пароля /courier/login")
//    @Description("Проверка сообщения об ошибке при входе без указания пароля. Тест упадет")
//
//    public void testCourierLoginNoPassword() {
//    var creds = new CourierCredentials();
//    creds.setLogin("kham_test");
//    ValidatableResponse loginResponse = client.loginCourier(creds);
//     check.badRequestLogin(loginResponse);
//}
}
