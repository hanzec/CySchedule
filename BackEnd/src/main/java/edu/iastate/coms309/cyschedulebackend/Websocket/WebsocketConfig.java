package edu.iastate.coms309.cyschedulebackend.Websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import edu.iastate.coms309.cyschedulebackend.Service.AccountService;

@Configuration
//@ServerEndpoint(value="/websocketDemo/{userId}",configurator = SpringConfigurator.class)
public class WebsocketConfig{
	@Autowired
	AccountService accountservice;
	
	@Bean
	public ServerEndpointExporter serverEndpointExporter(){
		//WebsocketDemo.setAccountService(accountservice);
		return new ServerEndpointExporter();
	}

	@Bean
	public WebsocketDemo websocketDemo(){
		WebsocketDemo.setAccountService(accountservice);
		return new WebsocketDemo();
	}
	//public class tomcatConfig i

}
