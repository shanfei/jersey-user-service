package org.dynamicsimulations.openfire.plugin.servlet.request;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Authorization")
public class Authorization {
    private String authCode;

    public Authorization() {
    }

    public Authorization(final String authCode) {
        this.authCode = authCode;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(final String authCode) {
        this.authCode = authCode;
    }
}
