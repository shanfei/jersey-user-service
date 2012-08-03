package org.dynamicsimulations.openfire.plugin.servlet.filter;


import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import org.dynamicsimulations.openfire.plugin.JerseyUserServicePlugin;
import org.dynamicsimulations.openfire.plugin.servlet.exception.NotAuthorizedException;
import org.jivesoftware.openfire.XMPPServer;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

public class AllowedIPFilter implements ContainerRequestFilter {

    @Context
    HttpServletRequest httpRequest;

    private final JerseyUserServicePlugin plugin;

    public AllowedIPFilter() {
        this.plugin = (JerseyUserServicePlugin) XMPPServer.getInstance().getPluginManager().getPlugin(JerseyUserServicePlugin.PLUGIN_NAME);
    }

    @Override
    public ContainerRequest filter(ContainerRequest containerRequest) {
        if (!plugin.getAllowedIPs().isEmpty()) {
            // Get client's IP address
            String ipAddress = containerRequest.getHeaderValue("x-forwarded-for");
            if (ipAddress == null) {
                ipAddress = containerRequest.getHeaderValue("X_FORWARDED_FOR");
                if (ipAddress == null) {
                    ipAddress = containerRequest.getHeaderValue("X-Forward-For");
                    if (ipAddress == null) {
                        ipAddress = httpRequest.getRemoteAddr();
                    }
                }
            }

            if (!plugin.getAllowedIPs().contains(ipAddress)) {
                throw new NotAuthorizedException("Your request IP address is not in the list of allowed Ip addresses.");
            }
        }

        // Return the request
        return containerRequest;
    }
}