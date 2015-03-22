package com.progress.web.akka.process;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.progress.backend.entities.UserEntity;
import static com.progress.spring.config.SpringExtension.SpringExtProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author Armen
 */
@Service("userCollector")
@Scope("prototype")
@Component
public class UserCollector extends UntypedActor {

    private final LoggingAdapter LOG = Logging.getLogger(getContext().system(), this);
    private UserEntity result;

    @Override
    public void preStart() throws Exception {
        System.out.println("2. UserCollector:  preStart ");       
        getContext().actorOf(SpringExtProvider.get(getContext().system()).props("userContainer"), "one");
    }

    @Override
    public void onReceive(Object message) throws Exception {

        System.out.println("2. UserCollector: onReceive  message: {} " + message);
        if (message == null) {
            System.out.println("message is null " + message);
            return;
        }
        if (message instanceof UserEntity) {

            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
            ctx.scan("com");
            ctx.refresh();


            ActorSystem system = ctx.getBean(ActorSystem.class);

            ActorRef counter = system.actorOf(
                    SpringExtProvider.get(system).props("userContainer"));
            
            System.out.println("Pass to mailContainer " + message);
            counter.tell(message, ActorRef.noSender());
            postStop();         
        } else {
            unhandled(message);
        }

    }
}
