package tests;

import com.github.javafaker.Faker;
import data_test.DataTest;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import models.Courier;
import models.Auth;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;

public class CreateCourierTest extends BaseTest {

    @Test
    @Description("Курьер должен успешно создаваться при передаче всех обязательных полей")
    public void courierShouldBeCreatedWithValidData() {

        String login =  DataTest.getRandomLogin();
        String password = DataTest.getRandomPassword();
        String firstName = DataTest.getRandomFirstName();
        Courier courier = new Courier(login, password, firstName);

        Response response = courierAct.createCourier(courier);

        courierAct.checkCourierCreatedSuccessfully(response);

        saveCourierForCleanup(login, password);
    }

    @Test
    @Description("Нельзя создать двух курьеров с одинаковым логином")
    public void courierShouldNotBeCreatedWithDuplicateLogin() {
        String login = DataTest.getRandomLogin();
        String password = DataTest.getRandomPassword();
        String firstName = DataTest.getRandomFirstName();

        Courier firstCourier = new Courier(login, password, firstName);
        courierAct.createCourier(firstCourier);

        Courier secondCourier = new Courier(login, "differentPassword", "DifferentName");
        Response response = courierAct.createCourier(secondCourier);

        courierAct.checkCourierConflict(response);

        saveCourierForCleanup(login, password);
    }

    @Test
    @Description("Нельзя создать курьера без логина")
    public void courierShouldNotBeCreatedWithoutLogin() {
        String login = "";
        String password = DataTest.getRandomPassword();
        String firstName = DataTest.getRandomFirstName();
        Courier courier = new Courier(login, password, firstName);

        Response response = courierAct.createCourier(courier);

        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", Matchers.equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @Description("Нельзя создать курьера без пароля")
    public void courierShouldNotBeCreatedWithoutPassword() {

        String login = DataTest.getRandomLogin();
        String password = "";
        String firstName = DataTest.getRandomFirstName();
        Courier courier = new Courier(login, password, firstName);

        Response response = courierAct.createCourier(courier);

        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", Matchers.equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @Description("Нельзя создать курьера без имени")
    public void courierShouldNotBeCreatedWithoutFirstName() {

        String login = DataTest.getRandomLogin();
        String password = DataTest.getRandomPassword();
        String firstName = "";
        Courier courier = new Courier(login, password, firstName);

        Response response = courierAct.createCourier(courier);

        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", Matchers.equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @Description("Успешный запрос создания курьера возвращает код 201")
    public void successfulCreationReturnsStatusCode201() {

        String login = DataTest.getRandomLogin();
        String password = DataTest.getRandomPassword();
        String firstName = DataTest.getRandomFirstName();
        Courier courier = new Courier(login, password, firstName);

        Response response = courierAct.createCourier(courier);

        response.then().statusCode(SC_CREATED);

        saveCourierForCleanup(login, password);
    }

    @Test
    @Description("Успешный запрос создания курьера возвращает ok: true")
    public void successfulCreationReturnsOkTrue() {

        String login = DataTest.getRandomLogin();
        String password = DataTest.getRandomPassword();
        String firstName = DataTest.getRandomFirstName();
        Courier courier = new Courier(login, password, firstName);

        Response response = courierAct.createCourier(courier);

        response.then().body("ok", Matchers.equalTo(true));

        saveCourierForCleanup(login, password);
    }
}