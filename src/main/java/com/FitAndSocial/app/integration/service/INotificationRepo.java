package com.FitAndSocial.app.integration.service;

import com.FitAndSocial.app.model.Notification;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by mint on 22-10-14.
 */
public interface INotificationRepo {

    public void save(Notification notification) throws SQLException;
    public Notification find(int id) throws SQLException;
    public List<Notification> getAllNotifications() throws SQLException;
    public void delete(Notification notification) throws SQLException;
    public void update(Notification notification) throws SQLException;
}
