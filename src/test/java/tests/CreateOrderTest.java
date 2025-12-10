package tests;

import data_test.DataTest;
import io.qameta.allure.Description;
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
    @Description("Создание заказа с различными вариантами цветов")
    public void createOrderWithDifferentColorOptions() {
        // Arrange
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

        Response response = orderAct.createOrder(order);

        orderAct.checkOrderCreatedSuccessfully(response);

        Integer track = response.jsonPath().getInt("track");
        saveOrderForCleanup(track);
    }

    @Test
    @Description("Тело ответа при создании заказа содержит track")
    public void orderCreationResponseContainsTrack() {
        // Arrange
        Order order = new Order(
                DataTest.getRandomFirstName(),
                DataTest.getRandomLastName(),
                DataTest.getRandomAddress(),
                DataTest.getRandomMetroStation(),
                DataTest.getRandomPhone(),
                DataTest.getRandomRentTime(),
                DataTest.getRandomDeliveryDate(),
                DataTest.getRandomComment(),
                Arrays.asList("BLACK")
        );

        Response response = orderAct.createOrder(order);

        response.then()
                .body("track", Matchers.notNullValue())
                .body("track", Matchers.instanceOf(Integer.class));

        Integer track = response.jsonPath().getInt("track");
        saveOrderForCleanup(track);
    }

    @Test
    @Description("Создание заказа возвращает код ответа 201")
    public void orderCreationReturnsStatusCode201() {
        // Arrange
        Order order = new Order(
                DataTest.getRandomFirstName(),
                DataTest.getRandomLastName(),
                DataTest.getRandomAddress(),
                DataTest.getRandomMetroStation(),
                DataTest.getRandomPhone(),
                DataTest.getRandomRentTime(),
                DataTest.getRandomDeliveryDate(),
                DataTest.getRandomComment(),
                Arrays.asList("GREY")
        );

        Response response = orderAct.createOrder(order);

        response.then().statusCode(HttpStatus.SC_CREATED);

        Integer track = response.jsonPath().getInt("track");
        saveOrderForCleanup(track);
    }
}