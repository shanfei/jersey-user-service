package org.dynamicsimulations.openfire.plugin.servlet;

import org.dynamicsimulations.openfire.plugin.JerseyUserServicePlugin;
import org.jivesoftware.openfire.XMPPServer;
import org.dynamicsimulations.openfire.plugin.servlet.exception.NotAuthorizedException;
import org.dynamicsimulations.openfire.plugin.servlet.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path(JerseyUserServicePlugin.PLUGIN_NAME)
public class UserResource {

    private static final Logger Log = LoggerFactory.getLogger(JerseyUserServicePlugin.class);
    private final JerseyUserServicePlugin plugin;

    public UserResource() {
        plugin = (JerseyUserServicePlugin) XMPPServer.getInstance().getPluginManager().getPlugin(JerseyUserServicePlugin.PLUGIN_NAME);
        Log.debug(JerseyUserServicePlugin.PLUGIN_NAME);
    }

    @GET
    @Path("/{username}/")
    @Produces({MediaType.APPLICATION_JSON})
    public UserResponse getUser(@PathParam(value = "username") final String userName,
                                @QueryParam("token") final String token) {
        return new UserResponse(plugin.getUser(userName));
    }
}
