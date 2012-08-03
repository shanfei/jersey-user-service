package org.dynamicsimulations.openfire.plugin.servlet;


import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import org.dynamicsimulations.openfire.plugin.JerseyUserServicePlugin;
import org.dynamicsimulations.openfire.plugin.servlet.exception.NotAuthorizedException;
import org.jivesoftware.openfire.XMPPServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;

public class TokenAuthenticationFilter implements ContainerRequestFilter {

    private final JerseyUserServicePlugin plugin;

    public TokenAuthenticationFilter() {
        this.plugin = (JerseyUserServicePlugin) XMPPServer.getInstance().getPluginManager().getPlugin(JerseyUserServicePlugin.PLUGIN_NAME);
    }

    @Override
    public ContainerRequest filter(ContainerRequest containerRequest) {
        MultivaluedMap<String, String> queryParams = containerRequest.getQueryParameters();

        final String token = queryParams.getFirst("token");

        if(token == null || !plugin.isRequestAuthorized(token)) {
            throw new NotAuthorizedException();
        }

        // Return the request
        return containerRequest;
    }
}