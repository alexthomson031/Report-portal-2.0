package ru.netology;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class ChangeOfDate {
    private RegistrationInfo registrationInfo;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static  void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        registrationInfo = DataGenerator.RegistrationCard.generateInfo("ru");
    }

    @Test
    void generateTestDataUsing() {
        $("[data-test-id='city'] input").setValue(registrationInfo.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        String date = DataGenerator.getDate(3);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id=name] input").setValue(registrationInfo.getName());
        $("[data-test-id=phone] input").setValue(registrationInfo.getPhoneNumber());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(7))
                .shouldBe(exactText("Встреча успешно запланирована на " + date))
                .click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        String reDate = DataGenerator.getDate(9);
        $("[data-test-id='date'] input").setValue(reDate);
        $$("button").find(exactText("Запланировать")).click();
        $(withText("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(visible, Duration.ofSeconds(7));
        $(withText("Перепланировать?")).click();
        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(7))
                .shouldHave(exactText("Встреча успешно запланирована на " + reDate));
    }

    @Test
    void loginErrorLetter() {
        $("[data-test-id='city'] input").setValue(registrationInfo.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        String date = DataGenerator.getDate(3);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id=name] input").setValue(DataGenerator.getName());
        $("[data-test-id=phone] input").setValue(registrationInfo.getPhoneNumber());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=name] .input__sub")
                .shouldBe(visible, Duration.ofSeconds(7))
                .shouldBe(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }
}