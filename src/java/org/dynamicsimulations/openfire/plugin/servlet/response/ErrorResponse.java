package org.dynamicsimulations.openfire.plugin.servlet.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Error")
public class ErrorResponse {
    private String type;
    private String reason;

    public ErrorResponse() {
    }

    public ErrorResponse(final Exception e) {
        this.type = e.getClass().getSimpleName();
        this.reason = e.getMessage();
    }

    public ErrorResponse(Class c, String message) {
        this.type = c.getSimpleName();
        this.reason = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
