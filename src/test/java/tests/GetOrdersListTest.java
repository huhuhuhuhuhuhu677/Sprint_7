package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import actions.OrderAct;
import org.hamcrest.Matchers;

import static org.apache.http.HttpStatus.SC_OK;

public class GetOrdersListTest extends BaseTest {

    private final OrderAct orderAct = new OrderAct();

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка статуса кода и структуры ответа при получении списка заказов")
    public void getOrdersListReturnsValidResponse() {

        Response response = orderAct.getOrdersList();
        response.then().statusCode(SC_OK);
        response.then().body("orders", Matchers.notNullValue());
        response.then()
                .body("$", Matchers.hasKey("orders"))
                .body("orders", Matchers.instanceOf(java.util.List.class))
                .body("pageInfo", Matchers.notNullValue())
                .log().body();
    }
}