package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Генерирует геттеры, сеттеры, equals, hashCode, toString
@NoArgsConstructor // Генерирует конструктор без параметров
@AllArgsConstructor // Генерирует конструктор со всеми параметрами
public class Auth {
    private String login;
    private String password;
}