Due to a bizarre problem with apache tomcat you cannot compile these files without renaming the folder:

../javax/servletx 

to

../javax/servlet

For the server to work when tomcat is started 'servlet' must be renamed back to 'servletx'