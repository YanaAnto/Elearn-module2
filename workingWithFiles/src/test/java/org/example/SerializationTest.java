package org.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.example.model.Address;
import org.example.model.CreditBankCard;
import org.example.model.User;
import org.example.utils.FileUtils;
import org.example.utils.JsonUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SerializationTest {

    private static CreditBankCard testCreditBankCard;

    @BeforeAll
    public static void setUp() {
        Address address = new Address("country", "city", 10.23, -10.12);
        User user = new User("name", "surname",
            LocalDate.of(2000, 2, 20), address);
        testCreditBankCard = new CreditBankCard(user);
    }

    @Test
    public void verifySerializationTest() {
        String actualJsonData = JsonUtils.toJson(testCreditBankCard);
        String expectedJsonData = FileUtils
            .readFromFileNamed("src/test/resources/testData/testCreditBankCard.json");
        assertThat(actualJsonData).isEqualToIgnoringNewLines(expectedJsonData);
    }

    @Test
    public void verifyDeserializationTest() {
        String json = FileUtils
            .readFromFileNamed("src/test/resources/testData/testCreditBankCard.json");
        CreditBankCard newCreditBankCard = JsonUtils.toObject(json, CreditBankCard.class);
        assertThat(newCreditBankCard)
            .usingRecursiveComparison()
            .ignoringFields("user.address.latitude", "user.address.longitude")
            .isEqualTo(testCreditBankCard);
        assertThat(newCreditBankCard.getUser().getAddress().getLatitude()).isNull();
        assertThat(newCreditBankCard.getUser().getAddress().getLongitude()).isNull();
    }
}
