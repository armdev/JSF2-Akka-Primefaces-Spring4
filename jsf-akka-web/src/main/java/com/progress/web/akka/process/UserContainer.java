package com.progress.web.akka.process;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.progress.backend.entities.UserEntity;
import com.progress.backend.services.user.UserService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author Armen
 */
@Service("userContainer")
@Scope("prototype")
@Component
public class UserContainer extends UntypedActor implements Serializable {

    private static final long serialVersionUID = 1L;
    

    @Autowired
    @Qualifier("springService")
    private SpringService springService;  
    

    private final LoggingAdapter LOG = Logging.getLogger(getContext().system(), this);

    private UserEntity value;
    private List<UserEntity> mailList = new ArrayList<UserEntity>();
   

    public void setSpringService(SpringService springService) {
        this.springService = springService;
    }

    public UserContainer(UserEntity value) {
        this.value = value;
    }

    public UserContainer() {
    }

    @Override
    public void onReceive(Object message) throws Exception {

        //    LOG.debug("+ MailContainer message: {} ", message);
        if (message instanceof UserEntity) {
            value = (UserEntity) message;
           
            System.out.println("3. userContainer : With time   " + value.getSendDate());

            springService.add(value);
        }
        postStop();

    }

   

}
