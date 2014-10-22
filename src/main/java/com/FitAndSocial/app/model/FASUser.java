package com.FitAndSocial.app.model;

/**
 * Created by mint on 22-10-14.
 */
public class FASUser {

    private String id;
    private String username;
    private String activeSince;

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
