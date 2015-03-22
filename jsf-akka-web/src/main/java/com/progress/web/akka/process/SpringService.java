package com.progress.web.akka.process;

import com.progress.backend.entities.UserEntity;
import com.progress.backend.services.user.UserService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
@Service("springService")
@Component
@Scope("prototype")
public class SpringService implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<UserEntity> mailList = new ArrayList<UserEntity>();
    @Autowired
    @Qualifier("userService")
    private UserService userService;

    public void add(UserEntity user) {

        System.out.println("4. SpringService: Date sent " + user.getSendDate());
        System.out.println("4. SpringService: Date received " + new Date(System.currentTimeMillis()));
        System.out.println("---------------------------------------------------------");
        System.out.println("5. Adding mail with email  " + user.getEmail());
        userService.save(user);

        //  finalBean.addMail(mail);
    }

}
