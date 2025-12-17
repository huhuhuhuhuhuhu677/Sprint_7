package tests;

import data_test.DataTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import org.junit.Test;


public class CreateCourierTest extends BaseTest {

    @Test
    @DisplayName("Создание курьера с валидными данными")
    @Description("Курьер должен успешно создаваться при передаче всех обязательных полей")
    public void courierShouldBeCreatedWithValidData() {
        String login = DataTest.getRandomLogin();
        String password = DataTest.getRandomPassword();
        String firstName = DataTest.getRandomFirstName();
        Courier courier = new Courier(login, password, firstName);

        saveCourierForCleanup(login, password);
        Response response = courierAct.createCourier(courier);
        courierAct.checkCourierCreatedSuccessfully(response);
    }

    @Test
    @DisplayName("Попытка создания двух курьеров с одинаковым логином")
    @Description("Нельзя создать двух курьеров с одинаковым логином")
    public void courierShouldNotBeCreatedWithDuplicateLogin() {
        String login = DataTest.getRandomLogin();
        String password = DataTest.getRandomPassword();
        String firstName = DataTest.getRandomFirstName();

        Courier firstCourier = new Courier(login, password, firstName);
        saveCourierForCleanup(login, password);
        Response firstResponse = courierAct.createCourier(firstCourier);
        courierAct.checkCourierCreatedSuccessfully(firstResponse);

        Courier secondCourier = new Courier(login, "differentPassword", "DifferentName");
        Response secondResponse = courierAct.createCourier(secondCourier);
        courierAct.checkCourierConflict(secondResponse);

    }

    @Test
    @DisplayName("Попытка создания курьера без логина")
    @Description("Нельзя создать курьера без логина")
    public void courierShouldNotBeCreatedWithoutLogin() {
        String login = "";
        String password = DataTest.getRandomPassword();
        String firstName = DataTest.getRandomFirstName();
        Courier courier = new Courier(login, password, firstName);

        Response response = courierAct.createCourier(courier);
        courierAct.checkInsufficientDataError(response);
    }

    @Test
    @DisplayName("Попытка создания курьера без пароля")
    @Description("Нельзя создать курьера без пароля")
    public void courierShouldNotBeCreatedWithoutPassword() {
        String login = DataTest.getRandomLogin();
        String password = "";
        String firstName = DataTest.getRandomFirstName();
        Courier courier = new Courier(login, password, firstName);

        Response response = courierAct.createCourier(courier);
        courierAct.checkInsufficientDataError(response);;
    }

    @Test
    @DisplayName("Проверка статуса 201 и поля ok: true при успешном создании курьера")
    @Description("Успешный запрос создания курьера возвращает код 201")
    public void successfulCreationReturnsStatusCode201() {
        String login = DataTest.getRandomLogin();
        String password = DataTest.getRandomPassword();
        String firstName = DataTest.getRandomFirstName();
        Courier courier = new Courier(login, password, firstName);

        saveCourierForCleanup(login, password);
        Response response = courierAct.createCourier(courier);
        courierAct.checkInsufficientDataError(response);
    }
}

