package com.FitAndSocial.app.integration.service;

import com.FitAndSocial.app.model.FASUser;

import java.sql.SQLException;

/**
 * Created by mint on 22-10-14.
 */
public interface IFASUserRepo {
    public void save (FASUser user) throws SQLException;
    public FASUser find(String id) throws SQLException;
}
