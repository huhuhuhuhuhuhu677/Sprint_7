package tests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.Test;
import actions.OrderAct;

import static org.apache.http.HttpStatus.SC_OK;

public class GetOrdersListTest extends BaseTest {

    private final OrderAct orderAct = new OrderAct();

    @Test
    @Description("В тело ответа возвращается список заказов")
    public void getOrdersListReturnsOrdersArray() {

        Response response = orderAct.getOrdersList();

        response.then()
                .statusCode(SC_OK)
                .body("orders", org.hamcrest.Matchers.notNullValue());
    }

    @Test
    @Description("Ответ содержит корректный код статуса 200")
    public void getOrdersListReturnsStatusCode200() {
        Response response = orderAct.getOrdersList();

        response.then().statusCode(SC_OK);
    }
}