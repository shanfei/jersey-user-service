package org.dynamicsimulations.openfire.plugin.servlet.response;

import org.jivesoftware.openfire.user.User;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SuccessResponse")
public class SuccessResponse {
    private String userName;
    private String action;

    public SuccessResponse() {
    }

    public SuccessResponse(String userName, String action) {
        this.userName = userName;
        this.action = action;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
