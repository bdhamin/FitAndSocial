package com.FitAndSocial.app.integration.serviceImpl;

import android.content.Context;
import com.FitAndSocial.app.integration.daoProvider.FASUserDaoProvider;
import com.FitAndSocial.app.integration.service.IFASUserRepo;
import com.FitAndSocial.app.model.FASUser;
import com.google.inject.Inject;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;

/**
 * Created by mint on 22-10-14.
 */
public class FASUserRepo implements IFASUserRepo{

    private Dao<FASUser, String> _userDao;

    @Inject
    public FASUserRepo(Context context, FASUserDaoProvider fasUserDaoProvider){
        _userDao = fasUserDaoProvider.get();
    }

    @Override
    public void save(FASUser user) throws SQLException {
        _userDao.create(user);
    }

    @Override
    public FASUser find(String id) throws SQLException {
        return _userDao.queryForId(id);
    }
}
