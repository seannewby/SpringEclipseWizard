	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>/WEB-INF/context/applicationContext.xml</param-value>
	</context-param>
	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>
	<servlet>
		<servlet-name>ViewRendererServlet</servlet-name>
		<servlet-class>org.springframework.web.servlet.ViewRendererServlet</servlet-class>
		<load-on-startup>1</load-on-startup>
	</servlet>		
	<servlet-mapping>
		<servlet-name>ViewRendererServlet</servlet-name>
		<url-pattern>/WEB-INF/servlet/view</url-pattern>
	</servlet-mapping>	