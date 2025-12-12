package actions;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Auth;
import models.Courier;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.apache.http.HttpStatus.*;

public class CourierAct {

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(Endpoints.CREATE_COURIER.get());
    }

    @Step("Логин курьера")
    public Response loginCourier(Auth courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(Endpoints.LOGIN_COURIER.get());
    }
    @Step("Получение ID курьера по логину и паролю")
    public Integer getCourierId(String login, String password) {
        Auth auth = new Auth(login, password);
        Response response = loginCourier(auth);

        if (response.statusCode() == SC_OK) {
            return response.jsonPath().getInt("id");
        }
        return null;
    }

    @Step("Удаление курьера по ID")
    public void deleteCourier(Integer courierId) {
        given()
                .delete("/api/v1/courier/" + courierId)
                .then()
                .statusCode(SC_OK);
    }


    @Step("Проверка успешного создания курьера")
    public void checkCourierCreatedSuccessfully(Response response) {
        response.then()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }

    @Step("Проверка ошибки при неполных данных")
    public void checkInsufficientDataError(Response response) {
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("Проверка успешной авторизации курьера")
    public void checkCourierLoginSuccessfully(Response response) {
        response.then()
                .statusCode(SC_OK)
                .body("id", notNullValue());
    }

    @Step("Запрос с несуществующей парой логин-пароль")
    public void checkLoginWithWrongPass(Response response){
        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Проверка авторизации при неполных данных")
    public void checkInsufficientError(Response response) {
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
    @Step("Проверка конфликта при создании курьера")
    public void checkCourierConflict(Response response) {
        response.then()
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется"));
    }
}