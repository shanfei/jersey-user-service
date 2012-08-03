package org.dynamicsimulations.openfire.plugin.servlet;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.dynamicsimulations.openfire.plugin.JerseyUserServicePlugin;
import org.jivesoftware.admin.AuthCheckFilter;
import javax.servlet.ServletException;
import java.util.HashMap;
import java.util.Map;

/**
 * Auto configured Jersey Servlet. All Resource classes should be at the same package or deeper.
 * @author Dmitriy Savascool
 */
public class JerseyServletWrapper extends ServletContainer {

    private static final String SERVLET_URL = JerseyUserServicePlugin.PLUGIN_NAME + "/*";
    private static final String SCAN_PACKAGE_KEY = "com.sun.jersey.config.property.packages";
    private static final String SCAN_PACKAGE_DEFAULT = JerseyServletWrapper.class.getPackage().getName();

    private static final String RESOURCE_CONFIG_CLASS_KEY = "com.sun.jersey.config.property.resourceConfigClass";
    private static final String RESOURCE_CONFIG_CLASS = "com.sun.jersey.api.core.PackagesResourceConfig";

    private static final String CONTAINER_REQUEST_FILTER_KEY = "com.sun.jersey.spi.container.ContainerRequestFilters";
    private static final String TOKEN_AUTHENTICATION_FILTER = "org.dynamicsimulations.openfire.plugin.servlet.filter.TokenAuthenticationFilter";
    private static final String ALLOWED_IP_FILTER = "org.dynamicsimulations.openfire.plugin.servlet.filter.AllowedIPFilter";

    private static Map<String, Object> config;
    private static PackagesResourceConfig prc;

    static {
        config = new HashMap<String, Object>();
        config.put(RESOURCE_CONFIG_CLASS_KEY, RESOURCE_CONFIG_CLASS);
        config.put(SCAN_PACKAGE_KEY, SCAN_PACKAGE_DEFAULT);
        config.put(CONTAINER_REQUEST_FILTER_KEY, TOKEN_AUTHENTICATION_FILTER + ";" + ALLOWED_IP_FILTER);

        prc = new PackagesResourceConfig(SCAN_PACKAGE_DEFAULT);
        prc.setPropertiesAndFeatures(config);
        prc.getClasses().add(UserResource.class);
    }

    public JerseyServletWrapper() {
        super(prc);
    }

    @Override
    public void init() throws ServletException {
        super.init();
        AuthCheckFilter.addExclude(SERVLET_URL);
    }

    @Override
    public void destroy() {
        super.destroy();
        AuthCheckFilter.removeExclude(SERVLET_URL);
    }
}
