package org.example.bank;

import org.example.model.BankCard;
import org.example.model.BankCardType;
import org.example.model.User;

public interface Bank {

    BankCard createBankCard(User user, BankCardType cardType);
}
