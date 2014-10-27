package com.FitAndSocial.app.RoboGuiceBaseModule;

import com.FitAndSocial.app.integration.service.IFASUserRepo;
import com.FitAndSocial.app.integration.service.INotificationRepo;
import com.FitAndSocial.app.integration.serviceImpl.FASUserRepo;
import com.FitAndSocial.app.integration.serviceImpl.NotificationRepo;
import com.google.inject.AbstractModule;

/**
 * Created by mint on 22-10-14.
 */
public class BaseModule extends AbstractModule{

    /**
     * Bind an interface to concrete implementation
     */
    @Override
    protected void configure() {
        bind(IFASUserRepo.class).to(FASUserRepo.class);
        bind(INotificationRepo.class).to(NotificationRepo.class);
    }
}
