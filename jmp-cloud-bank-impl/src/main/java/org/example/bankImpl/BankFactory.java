package org.example.bankImpl;

import org.example.bank.Bank;
import org.example.model.BankCard;
import org.example.model.BankCardType;
import org.example.model.CreditBankCard;
import org.example.model.DebitBankCard;
import org.example.model.User;

public class BankFactory implements Bank {

    @Override
    public BankCard createBankCard(User user, BankCardType cardType) {
        return switch (cardType) {
            case CREDIT -> new CreditBankCard(user);
            case DEBIT -> new DebitBankCard(user);
            default -> throw new IllegalArgumentException(
                String.format("No implementation for %s", cardType));
        };
    }
}
