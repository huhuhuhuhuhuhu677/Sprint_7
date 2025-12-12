package tests;

import data_test.DataTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import models.Auth;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;

public class LoginCourierTest extends BaseTest {

    private String login;
    private String password;
    private String firstName;
    private Courier courier;

    @Before
    @DisplayName("Подготовка тестовых данных")
    public void setUp() {
        login = DataTest.getRandomLogin();
        password = DataTest.getRandomPassword();
        firstName = DataTest.getRandomFirstName();

        courier = new Courier(login, password, firstName);
        Response createResponse = courierAct.createCourier(courier);

        if (createResponse.statusCode() == SC_OK) {
            saveCourierForCleanup(login, password);
        }
    }

    @Test
    @DisplayName("Успешная авторизация курьера")
    @Description("Курьер может успешно авторизоваться с валидными данными")
    public void courierCanLoginWithValidCredentials() {
        Auth authData = new Auth(login, password);
        Response response = courierAct.loginCourier(authData);
        courierAct.checkCourierLoginSuccessfully(response);
    }

    @Test
    @DisplayName("Авторизация без обязательных полей")
    @Description("Для авторизации нужно передать все обязательные поля")
    public void loginRequiresAllMandatoryFields() {
        Auth authWithoutLogin = new Auth("", password);
        Response response1 = courierAct.loginCourier(authWithoutLogin);
        response1.then()
                .statusCode(400)
                .body("message", org.hamcrest.Matchers.equalTo("Недостаточно данных для входа"));

        Auth authWithoutPassword = new Auth(login, "");
        Response response2 = courierAct.loginCourier(authWithoutPassword);
        response2.then()
                .statusCode(400)
                .body("message", org.hamcrest.Matchers.equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Система возвращает ошибку при неверном пароле")
    public void loginFailsWithWrongPassword() {
        Auth wrongAuth = new Auth(login, "wrong_password");
        Response response = courierAct.loginCourier(wrongAuth);
        response.then()
                .statusCode(400)
                .body("message", org.hamcrest.Matchers.equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неверным логином")
    @Description("Система возвращает ошибку при неверном логине")
    public void loginFailsWithWrongLogin() {
        Auth wrongAuth = new Auth("wrong_login", password);
        Response response = courierAct.loginCourier(wrongAuth);
        response.then()
                .statusCode(400)
                .body("message", org.hamcrest.Matchers.equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация несуществующего пользователя")
    @Description("Авторизация под несуществующим пользователем возвращает ошибку")
    public void loginFailsWithNonExistentUser() {
        Auth nonExistentAuth = new Auth("non_existent_" + DataTest.getRandomLogin(), "some_password");
        Response response = courierAct.loginCourier(nonExistentAuth);
        response.then()
                .statusCode(400)
                .body("message", org.hamcrest.Matchers.equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Проверка получения ID при авторизации")
    @Description("Успешный запрос авторизации возвращает id курьера")
    public void successfulLoginReturnsCourierId() {
        Auth authData = new Auth(login, password);
        Response response = courierAct.loginCourier(authData);
        response.then()
                .statusCode(SC_OK)
                .body("id", org.hamcrest.Matchers.notNullValue())
                .body("id", org.hamcrest.Matchers.greaterThan(0));
    }

    @After
    @DisplayName("Завершение теста")
    public void tearDown() {
        createdCourierLogin = login;
        createdCourierPassword = password;
    }
}