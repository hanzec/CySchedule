package edu.iastate.coms309.cyschedulebackend.Websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
//@ServerEndpoint(value="/websocketDemo/{userId}",configurator = SpringConfigurator.class)
public class WebsocketConfig {
	@Bean
	public ServerEndpointExporter serverEndpointExporter(){
		return new ServerEndpointExporter();
	} 
	
	//public class tomcatConfig i

}
