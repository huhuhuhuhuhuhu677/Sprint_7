package tests;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.Auth;
import org.junit.After;
import org.junit.BeforeClass;
import actions.CourierAct;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class BaseTest {
    protected CourierAct courierAct = new CourierAct();

    protected String createdCourierLogin;
    protected String createdCourierPassword;
    protected Integer createdOrderTrack;

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @After
    public void cleanup() {

        if (createdOrderTrack != null) {
            cancelTestOrder(createdOrderTrack);
        }

        if (createdCourierLogin != null && createdCourierPassword != null) {
            deleteTestCourier(createdCourierLogin, createdCourierPassword);
        }
    }

    @Step("Удаление тестового курьера")
    private void deleteTestCourier(String login, String password) {
        try {
            Auth authData = new Auth(login, password);
            Response loginResponse = courierAct.loginCourier(authData);

            if (loginResponse.statusCode() == SC_OK) {
                String courierId = loginResponse.jsonPath().getString("id");
                given()
                        .delete("/api/v1/courier/" + courierId)
                        .then()
                        .statusCode(SC_OK);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при удалении курьера: " + e.getMessage());
        }
    }

    @Step("Отмена тестового заказа")
    private void cancelTestOrder(Integer track) {
        if (track == null) return;

        try {
            System.out.println("Пытаемся отменить заказ с track: " + track);

            given()
                    .header("Content-type", "application/json")
                    .body("{\"track\": " + track + "}")
                    .when()
                    .put("/api/v1/orders/cancel")
                    .then()
                    .log().ifError(); // Логируем только если ошибка

        } catch (Exception e) {
            // Игнорируем ошибки отмены - заказ мог быть уже отменен или не существует
            System.out.println("Не удалось отменить заказ " + track +
                    " (возможно, уже отменен): " + e.getMessage());
        }
    }

    protected void saveCourierForCleanup(String login, String password) {
        this.createdCourierLogin = login;
        this.createdCourierPassword = password;
    }

    protected void saveOrderForCleanup(Integer track) {
        this.createdOrderTrack = track;
    }
}