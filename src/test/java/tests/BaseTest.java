package tests;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.BeforeClass;
import actions.CourierAct;
import actions.OrderAct;


public class BaseTest {
    protected CourierAct courierAct = new CourierAct();
    protected OrderAct orderAct = new OrderAct();
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
            Integer courierId = courierAct.getCourierId(login, password);
            if (courierId != null) {
                courierAct.deleteCourier(courierId);
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
            orderAct.cancelOrder(track);
        } catch (Exception e) {
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