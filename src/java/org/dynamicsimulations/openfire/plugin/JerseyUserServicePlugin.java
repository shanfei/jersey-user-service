/**
 * $Revision: 1722 $
 * $Date: 2005-07-28 15:19:16 -0700 (Thu, 28 Jul 2005) $
 *
 * Copyright (C) 2005-2008 Jive Software. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dynamicsimulations.openfire.plugin;

import org.dynamicsimulations.openfire.plugin.model.UserData;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.jivesoftware.openfire.lockout.LockOutManager;
import org.dynamicsimulations.openfire.plugin.servlet.exception.UserNotFoundException;
import org.dynamicsimulations.openfire.plugin.servlet.exception.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.StringUtils;
import org.jivesoftware.util.PropertyEventListener;
import org.jivesoftware.util.PropertyEventDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;

import java.io.File;
import java.util.*;

/**
 * Plugin that allows the administration of users via HTTP requests.
 *
 * @author Justin Hunt
 */
public class JerseyUserServicePlugin implements Plugin, PropertyEventListener {
    public static final String PLUGIN_NAME = "jerseyuserservice";
    private static final Logger Log = LoggerFactory.getLogger(JerseyUserServicePlugin.class);

    private UserManager userManager;
    private XMPPServer server;

    private String secret;
    private boolean enabled;
    private Collection<String> allowedIPs;

    public void initializePlugin(PluginManager manager, File pluginDirectory) {
        server = XMPPServer.getInstance();
        userManager = server.getUserManager();

        secret = JiveGlobals.getProperty("plugin.servlet.secret", "");
        // If no secret key has been assigned to the user service yet, assign a random one.
        if (secret.equals("")) {
            secret = StringUtils.randomString(8);
            setSecret(secret);
        }

        // See if the service is enabled or not.
        enabled = JiveGlobals.getBooleanProperty("plugin.servlet.enabled", true);

        // Get the list of IP addresses that can use this service. An empty list means that this filter is disabled.
        allowedIPs = StringUtils.stringToCollection(JiveGlobals.getProperty("plugin.servlet.allowedIPs", ""));

        // Listen to system property events
        PropertyEventDispatcher.addListener(this);
    }

    public void destroyPlugin() {
        userManager = null;
        // Stop listening to system property events
        PropertyEventDispatcher.removeListener(this);
    }

    public void createUser(String username, UserData userData) {
        try {
            User user = userManager.createUser(username, userData.getPassword(), userData.getName(), userData.getName());
            addUserGroups(user, userData.getGroups());
        } catch (org.jivesoftware.openfire.user.UserAlreadyExistsException e) {
            throw new UserAlreadyExistsException(e);
        }
    }

    public void deleteUser(String username) {
        User user = getUser(username);
        userManager.deleteUser(user);
    }

    public void disableUser(String username) {
        User user = getUser(username);
        LockOutManager.getInstance().disableAccount(username, null, null);
    }

    public void enableUser(String username) {
        User user = getUser(username);
        LockOutManager.getInstance().enableAccount(username);
    }

    public boolean isRequestAuthorized(final String token) {
        return (token != null && token.equals(this.secret) && enabled);
    }

    public void updateUser(final String username, final UserData userData) {
        final User user = getUser(username);
        if (!"".equals(userData.getPassword())) user.setPassword(userData.getPassword());
        if (!"".equals(userData.getName())) user.setName(userData.getName());
        if (!"".equals(userData.getEmail())) user.setEmail(userData.getEmail());

        addUserGroups(user, userData.getGroups());
    }

    private void addUserGroups(User user, String groups) {
        final List<Group> newGroups = new ArrayList<Group>();
        if (!"".equals(groups)) {
            for (String group : groups.split(",")) {
                try {
                    newGroups.add(GroupManager.getInstance().getGroup(group));
                } catch (GroupNotFoundException e) {
                    // Ignore this group
                }
            }

            Collection<Group> existingGroups = GroupManager.getInstance().getGroups(user);
            // Get the list of groups to add to the user
            List<Group> groupsToAdd = new ArrayList<Group>(newGroups);
            groupsToAdd.removeAll(existingGroups);
            // Get the list of groups to remove from the user
            List<Group> groupsToDelete = new ArrayList<Group>(existingGroups);
            groupsToDelete.removeAll(newGroups);

            // Add the user to the new groups
            for (Group group : groupsToAdd) {
                group.getMembers().add(server.createJID(user.getUsername(), null));
            }
            // Remove the user from the old groups
            for (Group group : groupsToDelete) {
                group.getMembers().remove(server.createJID(user.getUsername(), null));
            }
        }
    }

    public User getUser(String username) {
        JID targetJID = server.createJID(username, null);
        // Check that the sender is not requesting information of a remote server entity
        if (targetJID.getNode() == null) {
            // Sender is requesting presence information of an anonymous user
            throw new UserNotFoundException(
                    new org.jivesoftware.openfire.user.UserNotFoundException("Username is NULL"));
        }

        try {
            return userManager.getUser(targetJID.getNode());
        } catch (org.jivesoftware.openfire.user.UserNotFoundException e) {
            throw new UserNotFoundException(e);
        }
    }

    public void deleteAllUsers() {
        Collection<User> users = userManager.getUsers();

        for (User u : users) {
            try {
                if (!u.getUsername().equals("admin")) {
                    userManager.deleteUser(u);
                }
            } catch (Exception e) {
                // Eat exception
                Log.error("Purge Error", e);
            }
        }
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        JiveGlobals.setProperty("plugin.servlet.secret", secret);
        this.secret = secret;
    }

    public Collection<String> getAllowedIPs() {
        return allowedIPs;
    }

    public void setAllowedIPs(Collection<String> allowedIPs) {
        JiveGlobals.setProperty("plugin.servlet.allowedIPs", StringUtils.collectionToString(allowedIPs));
        this.allowedIPs = allowedIPs;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        JiveGlobals.setProperty("plugin.servlet.enabled", enabled ? "true" : "false");
    }

    public void propertySet(String property, Map<String, Object> params) {
        if (property.equals("plugin.servlet.secret")) {
            this.secret = (String) params.get("value");
        } else if (property.equals("plugin.servlet.enabled")) {
            this.enabled = Boolean.parseBoolean((String) params.get("value"));
        } else if (property.equals("plugin.servlet.allowedIPs")) {
            this.allowedIPs = StringUtils.stringToCollection((String) params.get("value"));
        }
    }

    public void propertyDeleted(String property, Map<String, Object> params) {
        if (property.equals("plugin.servlet.secret")) {
            this.secret = "";
        } else if (property.equals("plugin.servlet.enabled")) {
            this.enabled = false;
        } else if (property.equals("plugin.servlet.allowedIPs")) {
            this.allowedIPs = Collections.emptyList();
        }
    }

    public void xmlPropertySet(String property, Map<String, Object> params) {
        // Do nothing
    }

    public void xmlPropertyDeleted(String property, Map<String, Object> params) {
        // Do nothing
    }
}
