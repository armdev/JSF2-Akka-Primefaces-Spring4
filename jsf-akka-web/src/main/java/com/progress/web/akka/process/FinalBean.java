package com.progress.web.akka.process;

import com.progress.backend.entities.UserEntity;
import com.progress.backend.services.user.UserService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class FinalBean implements Serializable {

    private List<UserEntity> userList = new ArrayList<UserEntity>();

    @ManagedProperty("#{userService}")
    private UserService userService;

    public FinalBean() {

    }
    
    public void doDrop(){
        userService.drop();
    }

    public List<UserEntity> getUserList() {
        userList = userService.findAll();
        return userList;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
