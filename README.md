jersey-user-service
===================

An Openfire plugin that provides a Jersey REST API for administering users remotely.

This builds upon the work provided by the original **userservice** plugin. It replaces the query parameter style with a CRUD REST api. It provides responses in both JSON and XML format.

Installation
------------

To build the plugin simply place the directory structure in the openfire plugins directory. You can then use the ANT **plugin** target to build the plugin. After building simply place the .jar file that is created in the **target/plugins** directory in to your openfire installation's plugin directory.