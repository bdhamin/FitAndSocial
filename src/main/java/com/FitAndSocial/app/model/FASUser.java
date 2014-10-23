package com.FitAndSocial.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by mint on 22-10-14.
 */
@DatabaseTable(tableName = "fasUser")
public class FASUser {

    @DatabaseField(id =true, generatedId = false, canBeNull = false)
    private String id;
    @DatabaseField(canBeNull = false)
    private String username;
    @DatabaseField(canBeNull = false)
    private String activeSince;

    public FASUser(){}

    public FASUser(String id, String username, String activeSince){
        this.id = id;
        this.username = username;
        this.activeSince = activeSince;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getActiveSince() {
        return activeSince;
    }

    public void setActiveSince(String activeSince) {
        this.activeSince = activeSince;
    }
}
