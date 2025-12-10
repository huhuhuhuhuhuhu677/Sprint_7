package actions;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Order;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.apache.http.HttpStatus.*;

public class OrderAct {

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(Endpoints.ORDERS.get());
    }

    @Step("Получение списка заказов")
    public Response getOrdersList() {
        return given()
                .get(Endpoints.ORDERS.get());
    }

    @Step("Проверка успешного создания заказа")
    public void checkOrderCreatedSuccessfully(Response response) {
        response.then()
                .statusCode(SC_CREATED)
                .body("track", notNullValue());
    }

    @Step("Проверка наличия списка заказов в ответе")
    public void checkOrdersListNotEmpty(Response response) {
        response.then()
                .statusCode(SC_OK)
                .body("orders", notNullValue());
    }
}