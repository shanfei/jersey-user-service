package org.dynamicsimulations.openfire.plugin.servlet;

import org.dynamicsimulations.openfire.plugin.JerseyUserServicePlugin;
import org.dynamicsimulations.openfire.plugin.model.UserData;
import org.dynamicsimulations.openfire.plugin.servlet.response.SuccessResponse;
import org.jivesoftware.openfire.XMPPServer;
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
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public UserResponse getUser(@PathParam(value = "username") final String userName) {
        return new UserResponse(plugin.getUser(userName));
    }

    @GET
    @Path("/purge/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public SuccessResponse deleteAllUsers() {
        plugin.deleteAllUsers();
        return new SuccessResponse("", "PURGE");
    }

    @GET
    @Path("/{username}/enable")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public SuccessResponse enableUser(@PathParam(value = "username") final String userName) {
        plugin.enableUser(userName);
        return new SuccessResponse(userName, "ENABLE");
    }

    @GET
    @Path("/{username}/disable")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public SuccessResponse disableUser(@PathParam(value = "username") final String userName) {
        plugin.disableUser(userName);
        return new SuccessResponse(userName, "DISABLE");
    }

    @DELETE
    @Path("/{username}/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public SuccessResponse deleteUser(@PathParam(value = "username") final String userName) {
        plugin.deleteUser(userName);
        return new SuccessResponse(userName, "DELETE");
    }

    @POST
    @Path("/{username}/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON})
    public SuccessResponse deleteUser(@PathParam(value = "username") final String userName, final UserData userData) {
        plugin.updateUser(userName, userData);
        return new SuccessResponse(userName, "UPDATE");
    }
}
