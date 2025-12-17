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

    @Step("Отмена заказа по трек-номеру")
    public void cancelOrder(Integer track) {
        given()
                .header("Content-type", "application/json")
                .body("{\"track\": " + track + "}")
                .when()
                .put("/api/v1/orders/cancel")
                .then()
                .extract()
                .response();
    }

    @Step("Проверка успешного создания заказа")
    public void checkOrderCreatedSuccessfully(Response response) {
        response.then()
                .statusCode(SC_CREATED)
                .body("track", notNullValue());
    }

    @Step("Получение списка заказов с параметрами")
    public Response getOrdersListWithParams(Integer courierId, Integer limit, Integer page) {
        return given()
                .queryParam("courierId", courierId)
                .queryParam("limit", limit)
                .queryParam("page", page)
                .get(Endpoints.ORDERS.get());
    }
}