package org.dynamicsimulations.openfire.plugin.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@XmlRootElement(name = "UserData")
public class UserData {
    private String password;
    private String email;
    private String name;
    private List<String> groups;

    public UserData() {
        groups = new ArrayList<String>();
    }

    public UserData(String password, String email, String name, String groups) {
        this.password = password;
        this.email = email;
        this.name = name;

        setGroups(groups);
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

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public void setGroups(String groups) {
        final StringTokenizer tkn = new StringTokenizer(groups, ",");
        while (tkn.hasMoreTokens()) {
            this.groups.add(tkn.nextToken());
        }
    }
}
