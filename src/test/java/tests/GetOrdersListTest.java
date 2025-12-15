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
        // Отправка запроса на получение списка заказов
        Response response = orderAct.getOrdersList();

        // Проверка статус-кода 200
        response.then().statusCode(SC_OK);

        // Проверка наличия массива заказов в ответе
        response.then().body("orders", Matchers.notNullValue());

        // Дополнительные проверки структуры ответа (опционально)
        response.then()
                .body("$", Matchers.hasKey("orders")) // Проверка наличия ключа "orders"
                .body("orders", Matchers.instanceOf(java.util.List.class)) // Проверка типа
                .body("pageInfo", Matchers.notNullValue()) // Если есть пагинация
                .log().body(); // Логирование ответа для отладки
    }
}