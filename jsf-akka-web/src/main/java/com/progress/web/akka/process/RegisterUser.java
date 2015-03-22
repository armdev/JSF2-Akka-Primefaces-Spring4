package com.progress.web.akka.process;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.progress.backend.entities.UserEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static com.progress.spring.config.SpringExtension.SpringExtProvider;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "registerUser")
@ViewScoped
public class RegisterUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private transient FacesContext context = null;
    private transient ExternalContext externalContext = null;

    private UserEntity user;

    public RegisterUser() {
    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
        user = new UserEntity();

    }

    public String send() {
        System.out.println("#######SendMailBean: ##Creating new Actor System...#######START TIME : "
                + new Date(System.currentTimeMillis()));

        System.out.println("1. SendMailBean : Send mail...");
        Random randomGenerator = new Random();

        Integer randomInt = randomGenerator.nextInt(150);

        AnnotationConfigApplicationContext ctx
                = new AnnotationConfigApplicationContext();
        ctx.scan("com");
        ctx.refresh();


        ActorSystem system = ctx.getBean(ActorSystem.class);

        ActorRef counter = system.actorOf(
                SpringExtProvider.get(system).props("userCollector"));
        user.setSendDate(new Date(System.currentTimeMillis()));
        counter.tell(user, ActorRef.noSender());
      
        return null;
    }
    
  

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

   
}
