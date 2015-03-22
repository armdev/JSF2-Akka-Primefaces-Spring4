package com.progress.backend.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private ObjectId _id;
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private Date sendDate;

    private Date registeredDate;

    public UserEntity() {
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 97 * hash + (this.email != null ? this.email.hashCode() : 0);
        hash = 97 * hash + (this.firstname != null ? this.firstname.hashCode() : 0);
        hash = 97 * hash + (this.lastname != null ? this.lastname.hashCode() : 0);
        hash = 97 * hash + (this.registeredDate != null ? this.registeredDate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserEntity other = (UserEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.email == null) ? (other.email != null) : !this.email.equals(other.email)) {
            return false;
        }
        if ((this.firstname == null) ? (other.firstname != null) : !this.firstname.equals(other.firstname)) {
            return false;
        }
        if ((this.lastname == null) ? (other.lastname != null) : !this.lastname.equals(other.lastname)) {
            return false;
        }
        if (this.registeredDate != other.registeredDate && (this.registeredDate == null || !this.registeredDate.equals(other.registeredDate))) {
            return false;
        }
        return true;
    }

}
