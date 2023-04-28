package org.example.provider;

import org.example.serviceImpl.ServiceImpl;

public interface ServiceProvider {

    ServiceImpl create();
}
