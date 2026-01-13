package com.omar.dynamic_bean_registration.service.Impl;

import com.omar.dynamic_bean_registration.service.MessageService;

public class EmailMessageService implements MessageService {

    @Override
    public String getMessage() {
        return "📧 Email message sent at " + java.time.LocalDateTime.now();
    }

    @Override
    public String getServiceType() {
        return "EMAIL";
    }
}