package com.progress.spring.config;

import akka.actor.ActorSystem;
import static com.progress.spring.config.SpringExtension.SpringExtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackages = {"com"}, excludeFilters = {
    @ComponentScan.Filter(Configuration.class)})
public class CommonCoreConfig {

    // the application context is needed to initialize the Akka Spring Extension
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Actor system singleton for this application.
     * @return 
     */
    @Bean
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("AkkaJavaSpring");
        // initialize the application context in the Akka Spring Extension
        SpringExtProvider.get(system).initialize(applicationContext);
        return system;
    }
}
