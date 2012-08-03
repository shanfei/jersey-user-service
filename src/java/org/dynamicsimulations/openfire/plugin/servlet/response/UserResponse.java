package org.dynamicsimulations.openfire.plugin.servlet.response;

import org.jivesoftware.openfire.user.User;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UserResponse")
public class UserResponse {
    private String userName;
    private String name;
    private String uid;
    private String email;

    public UserResponse() {
    }

    public UserResponse(final User user) {
        this.userName = user.getUsername();
        this.name = user.getName();
        this.uid = user.getUID();
        this.email = user.getEmail();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
