package ru.netology;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@UtilityClass
public class DataGenerator {

    public static class RegistrationCard {
        public static RegistrationInfo generateInfo(String locale) {
            Faker faker = new Faker(new Locale(locale));
            return new RegistrationInfo(
                    faker.address().city(),
                    faker.name().fullName(),
                    faker.phoneNumber().phoneNumber());
        }
    }

    public static String getDate(int plusDays) {
        return LocalDate.now().plusDays(plusDays).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}
