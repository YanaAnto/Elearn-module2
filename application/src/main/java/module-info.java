module application {
    requires jmp.dto;
    requires jmp.cloud.service.impl;
    requires jmp.cloud.bank.impl;
    requires service;
    uses org.example.provider.ServiceProvider;
}