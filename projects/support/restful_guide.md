		<!--  below is for REST web service -->
		<dependency>
		    <groupId>javax.ws.rs</groupId>
		    <artifactId>javax.ws.rs-api</artifactId>
		    <version>2.0.1</version>
		</dependency>
		<dependency>
		    <groupId>javax.servlet</groupId>
		    <artifactId>javax.servlet-api</artifactId>
		    <version>3.1.0</version>
		</dependency>
		<dependency>
		    <groupId>org.glassfish.jersey.containers</groupId>
		    <artifactId>jersey-container-grizzly2-http</artifactId>
		    <version>2.23</version>
		</dependency>
		<dependency>
		    <groupId>org.glassfish.jersey.containers</groupId>
		    <artifactId>jersey-container-grizzly2-servlet</artifactId>
		    <version>2.23</version>
		</dependency>
		<dependency>
		    <groupId>org.glassfish.jersey.containers</groupId>
		    <artifactId>jersey-container-jdk-http</artifactId>
		    <version>2.23</version>
		</dependency>
		<dependency>
		    <groupId>org.glassfish.jersey.containers</groupId>
		    <artifactId>jersey-container-simple-http</artifactId>
		    <version>2.23</version>
		</dependency>
		<dependency>
		    <groupId>org.glassfish.jersey.containers</groupId>
		    <artifactId>jersey-container-jetty-http</artifactId>
		    <version>2.23</version>
		</dependency>
		<dependency>
		    <groupId>org.glassfish.jersey.containers</groupId>
		    <artifactId>jersey-container-jetty-servlet</artifactId>
		    <version>2.23</version>
		</dependency>
		<dependency>
		    <groupId>org.glassfish.jersey.containers</groupId>
		    <artifactId>jersey-container-servlet</artifactId>
		    <version>2.23</version>
		</dependency>
		<dependency>
		    <groupId>org.glassfish.jersey.core</groupId>
		    <artifactId>jersey-client</artifactId>
		    <version>2.23</version>
		</dependency>
		<!--  end of REST web service -->
		
		Start.java
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    	        context.setContextPath("/");

    	        Server jettyServer = new Server(8080);
    	        jettyServer.setHandler(context);

    	        ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/rest/*");
    	        jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "net.sf.ws.rest");

    	        try {
    	            jettyServer.start();
    	            jettyServer.join();
    	        } catch(Exception ex){
    	        	LOGGER.warn(ex);
    	        }finally {
    	            jettyServer.destroy();
    	        }
				
		Service.java
		
		@Path("streamdemo")
    public class DemoService {
	
	@GET
	@Path("query")
	@Produces(MediaType.APPLICATION_JSON)
	public String executeQuery(@QueryParam("sql") String sql) throws Exception{		
			
