package com.embedded;



import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class JettyServer {
	
	@Option(name="-cron", metaVar="\"0 26 22 * * ? *\"", usage="Sets cron to start the fly scanner")
    public String cron;
	
	private void start() throws Exception, InterruptedException {
		System.setProperty("br.datamaio.fly.web.SchedulerStartupListener.cron", cron);
		
		ProtectionDomain domain = JettyServer.class.getProtectionDomain();
		URL location = domain.getCodeSource().getLocation();

		// create a web app and configure it to the root context of the server
		WebAppContext webapp = new WebAppContext();
		webapp.setDescriptor("WEB-INF/web.xml");
		webapp.setConfigurations(
				new Configuration[] { 
						new AnnotationConfiguration(), 
						new WebXmlConfiguration(),
						new WebInfConfiguration(), 
						new MetaInfConfiguration() });
		webapp.setContextPath("/");
		webapp.setWar(location.toExternalForm());

		// starts the embedded server and bind it on 8081 port
		Server server = new Server(8081);
		server.setHandler(webapp);
		server.start();
		server.join();
	}
	
	public static void main(String[] args) throws Exception {
		JettyServer server = new JettyServer();
        CmdLineParser parser = new CmdLineParser(server);
        try {
                parser.parseArgument(args);
                server.start();
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
        }
	}

}