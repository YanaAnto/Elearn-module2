module jmp.cloud.service.impl {
    requires transitive jmp.service.api;
    requires jmp.dto;
    requires com.google.common;
    exports org.example.serviceImpl;
    exports org.example.exception;
    provides org.example.service.Service with org.example.serviceImpl.ServiceImpl;
}