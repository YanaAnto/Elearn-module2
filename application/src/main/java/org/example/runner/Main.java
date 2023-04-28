package org.example.runner;

import java.time.LocalDate;
import java.util.ServiceLoader;
import org.example.bankImpl.BankFactory;
import org.example.exception.NoSuchSubscriptionException;
import org.example.model.BankCard;
import org.example.model.BankCardType;
import org.example.model.CreditBankCard;
import org.example.model.InterfaceCreditBankCard;
import org.example.model.User;
import org.example.provider.ServiceProvider;
import org.example.serviceImpl.ServiceImpl;

public class Main {

    public static void main(String[] args) {
        User user1 = new User("Andrei", "Ivanov", LocalDate.of(2000, 1, 12));
        User user2 = new User("Slava", "A", LocalDate.of(1986, 10, 6));
        User user3 = new User("Michail", "S", LocalDate.of(1950, 12, 2));

        BankCard creditBankCard = new BankFactory().createBankCard(user1, BankCardType.CREDIT);
        InterfaceCreditBankCard creditBankCard1 = CreditBankCard::new;
        BankCard creditBankCard2 = creditBankCard1.create();
        creditBankCard2.setUser(user3);
        creditBankCard2.setNumber("default number");
        System.out.printf("credit bank card: %s", creditBankCard2);
        creditBankCard.setNumber("Abc-secret-number");
        BankCard debitBankCard = new BankFactory().createBankCard(user2, BankCardType.DEBIT);
        debitBankCard.setNumber("Abc-secret-number2");


        ServiceImpl service = new ServiceImpl();
        service.subscribe(creditBankCard);
        service.subscribe(creditBankCard2);
        service.subscribe(debitBankCard);
        System.out.printf("number of subscribed users: %s%n", service.getAllUsers().size());
        System.out.printf("subscription for card number %s exists: %s%n",
            creditBankCard.getNumber(),
            service.getSubscriptionByBankCardNumber(creditBankCard.getNumber()).orElseThrow(() -> {
                throw new NoSuchSubscriptionException();
            }));

        var subscriptions = service.getAllSubscriptionsByCondition(
            subscription -> subscription.getStartDate().isBefore(LocalDate.of(2024, 1, 1)));
        System.out.printf("number of found subscritions: %s%n", subscriptions.size());
        subscriptions.forEach(elem -> System.out.printf("Subscrition for card: %s started: %s%n",
            elem.getBankcard().getNumber(), elem.getStartDate()));

        System.out.printf("average age of subscribed users: %s years %n", service.getAverageUsersAge());
        ServiceLoader<ServiceProvider> loader = ServiceLoader.load(ServiceProvider.class);
    }
}
