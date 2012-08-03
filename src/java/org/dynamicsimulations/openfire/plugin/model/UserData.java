package org.dynamicsimulations.openfire.plugin.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UserData")
public class UserData {
    private String password;
    private String email;
    private String name;
    private String groups;

    public UserData() {
    }

    public UserData(String password, String email, String name, String groupsNames) {
        this.password = password;
        this.email = email;
        this.name = name;
        this.groups = groupsNames;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }
}
