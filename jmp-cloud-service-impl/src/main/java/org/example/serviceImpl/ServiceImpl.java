package org.example.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.example.CacheStore;
import org.example.model.BankCard;
import org.example.model.Subscription;
import org.example.model.User;
import org.example.service.Service;

public class ServiceImpl implements Service {

    private final CacheStore<Subscription> subscriptionCacheStore = new CacheStore<>();

    @Override
    public void subscribe(BankCard bankCard) {
        var subscription = new Subscription(bankCard, LocalDate.now());
        subscriptionCacheStore.add(subscription);
    }

    @Override
    public Optional<Subscription> getSubscriptionByBankCardNumber(String number) {
        return subscriptionCacheStore.getAll().stream()
            .filter(elem -> elem.getBankcard().getNumber().equals(number))
            .findFirst();
    }

    @Override
    public List<Subscription> getAllSubscriptionsByCondition(Predicate<Subscription> condition) {
        return subscriptionCacheStore.getAll().stream().filter(condition).toList();
    }

    @Override
    public List<User> getAllUsers() {
        return subscriptionCacheStore.getAll().stream().map(elem -> elem.getBankcard().getUser())
            .collect(Collectors.toUnmodifiableList());
    }
}
