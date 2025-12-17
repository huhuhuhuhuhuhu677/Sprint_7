package data_test;

import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataTest {
    private static final Faker faker = new Faker(new Locale("ru"));


    public static String getRandomLogin() {
        return faker.name().firstName().toLowerCase() +
                faker.name().lastName().toLowerCase() +
                "_" + faker.bothify("????####");
    }

    public static String getRandomPassword() {
        return faker.regexify("[A-Za-z0-9!@#$%^&*()]{8,16}");
    }

    public static String getRandomFirstName() {
        return faker.name().firstName();
    }

    public static String getRandomLastName() {
        return faker.name().lastName();
    }


    public static String getRandomAddress() {
        return faker.address().streetAddress() + ", " + faker.address().city();
    }

    public static String getRandomMetroStation() {
        return String.valueOf(faker.number().numberBetween(1, 20));
    }

    public static String getRandomPhone() {
        return "+7" + faker.numerify("##########");
    }

    public static int getRandomRentTime() {
        return faker.number().numberBetween(1, 10);
    }

    public static String getRandomDeliveryDate() {
        LocalDate futureDate = LocalDate.now().plusDays(faker.number().numberBetween(1, 30));
        return futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String getRandomComment() {
        return faker.lorem().characters(10, 50);
    }

}