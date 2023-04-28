package org.example.service;

import static java.time.temporal.ChronoUnit.YEARS;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import org.example.model.BankCard;
import org.example.model.Subscription;
import org.example.model.User;

public interface Service {

    Function<LocalDate, Long> getAges = birthday -> YEARS.between(birthday, LocalDate.now());

    static boolean isPayableUser(User user) {
        return getAges.apply(user.getBirthday()) >= 18;
    }

    void subscribe(BankCard bankCard);

    List<User> getAllUsers();

    Optional<Subscription> getSubscriptionByBankCardNumber(String number);

    default double getAverageUsersAge() {
        return getAllUsers().stream().mapToLong(user -> getAges.apply(user.getBirthday())).average()
            .orElseThrow();
    }

    List<Subscription> getAllSubscriptionsByCondition(Predicate<Subscription> condition);
}
