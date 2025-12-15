package tests;

import data_test.DataTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Order;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import actions.OrderAct;
import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class CreateOrderTest extends BaseTest {

    private final OrderAct orderAct = new OrderAct();
    private final List<String> colors;
    private final String testName;

    public CreateOrderTest(String testName, List<String> colors) {
        this.testName = testName;
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Object[][] getColorData() {
        return new Object[][] {
                {"Заказ с черным самокатом", Arrays.asList("BLACK")},
                {"Заказ с серым самокатом", Arrays.asList("GREY")},
                {"Заказ с двумя цветами", Arrays.asList("BLACK", "GREY")},
                {"Заказ без указания цвета", Arrays.asList()}
        };
    }

    @Test
    @DisplayName("Создание заказа с различными цветами самоката")
    @Description("Комплексная проверка создания заказа: статус код, наличие track и корректность данных")
    public void createOrderWithDifferentColorOptions() {
        // 1. Подготовка тестовых данных
        Order order = new Order(
                DataTest.getRandomFirstName(),
                DataTest.getRandomLastName(),
                DataTest.getRandomAddress(),
                DataTest.getRandomMetroStation(),
                DataTest.getRandomPhone(),
                DataTest.getRandomRentTime(),
                DataTest.getRandomDeliveryDate(),
                DataTest.getRandomComment(),
                colors
        );

        // 2. Отправка запроса на создание заказа
        Response response = orderAct.createOrder(order);

        // 3. Проверка статус-кода (бывший отдельный тест)
        response.then().statusCode(HttpStatus.SC_CREATED);

        // 4. Проверка наличия track в ответе (бывший отдельный тест)
        response.then()
                .body("track", Matchers.notNullValue())
                .body("track", Matchers.instanceOf(Integer.class));

        // 5. Проверка успешного создания заказа через метод orderAct
        orderAct.checkOrderCreatedSuccessfully(response);

        // 6. Сохранение track для последующей очистки
        Integer track = response.jsonPath().getInt("track");
        saveOrderForCleanup(track);

        // 7. Дополнительная проверка: track должен быть положительным числом
        response.then().body("track", Matchers.greaterThan(0));
    }
}