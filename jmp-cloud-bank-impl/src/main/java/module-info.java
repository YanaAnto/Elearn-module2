module jmp.cloud.bank.impl {

    requires transitive jmp.bank.api;
    requires jmp.dto;
    exports org.example.bankImpl;
    provides org.example.bank.Bank with org.example.bankImpl.BankFactory;
}