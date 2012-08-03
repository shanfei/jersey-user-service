jersey-user-service
===================

An Openfire plugin that provides a Jersey REST API for administering users remotely.

This builds upon the work provided by the original **userservice** plugin. It replaces the query parameter style with a CRUD REST api. It provides responses in both JSON and XML format.
It also provies a working example of embedding a Jersey web server within Openfire.

Installation
------------

To build the plugin simply place the directory structure in the Openfire plugins directory. You can then use the ANT **plugin** target to build the plugin. After building simply place the .jar file that is created in the **target/plugins** directory in to your Openfire installation's plugin directory.