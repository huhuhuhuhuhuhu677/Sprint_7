package tests;

import data_test.DataTest;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import models.Courier;
import models.Auth;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.apache.http.HttpStatus.SC_OK;

public class LoginCourierTest extends BaseTest {

    private String login;
    private String password;
    private String firstName;

    @Before
    public void setUp() {
        login = DataTest.getRandomLogin();
        password = DataTest.getRandomPassword();
        firstName = DataTest.getRandomFirstName();

        Courier courier = new Courier(login, password, firstName);
        Response createResponse = courierAct.createCourier(courier);

        if (createResponse.statusCode() == SC_OK) {
            saveCourierForCleanup(login, password);
        }
    }

    @Test
    @Description("Курьер может успешно авторизоваться с валидными данными")
    public void courierCanLoginWithValidCredentials() {
        Auth authData = new Auth(login, password);

        Response response = courierAct.loginCourier(authData);

        courierAct.checkCourierLoginSuccessfully(response);
    }

    @Test
    @Description("Для авторизации нужно передать все обязательные поля")
    public void loginRequiresAllMandatoryFields() {
        Auth authWithoutLogin = new Auth("", password);
        Response response1 = courierAct.loginCourier(authWithoutLogin);
        courierAct.checkInsufficientError(response1);

        Auth authWithoutPassword = new Auth(login, "");
        Response response2 = courierAct.loginCourier(authWithoutPassword);
        courierAct.checkInsufficientError(response2);
    }

    @Test
    @Description("Система возвращает ошибку при неверном пароле")
    public void loginFailsWithWrongPassword() {
        Auth wrongAuth = new Auth(login, "wrong_password");

        Response response = courierAct.loginCourier(wrongAuth);

        courierAct.checkLoginWithWrongPass(response);
    }

    @Test
    @Description("Система возвращает ошибку при неверном логине")
    public void loginFailsWithWrongLogin() {
        Auth wrongAuth = new Auth("wrong_login", password);

        Response response = courierAct.loginCourier(wrongAuth);

        courierAct.checkLoginWithWrongPass(response);
    }

    @Test
    @Description("Авторизация под несуществующим пользователем возвращает ошибку")
    public void loginFailsWithNonExistentUser() {
        Auth nonExistentAuth = new Auth("non_existent_" + DataTest.getRandomLogin(),
                "some_password");

        Response response = courierAct.loginCourier(nonExistentAuth);

        courierAct.checkLoginWithWrongPass(response);
    }

    @Test
    @Description("Успешный запрос авторизации возвращает id курьера")
    public void successfulLoginReturnsCourierId() {
        Auth authData = new Auth(login, password);

        Response response = courierAct.loginCourier(authData);

        response.then()
                .statusCode(SC_OK)
                .body("id", Matchers.notNullValue())
                .body("id", Matchers.greaterThan(0));
    }

    @After
    public void tearDown() {
        createdCourierLogin = login;
        createdCourierPassword = password;
    }
}