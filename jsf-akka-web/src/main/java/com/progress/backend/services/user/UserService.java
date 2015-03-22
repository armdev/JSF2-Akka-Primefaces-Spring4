package com.progress.backend.services.user;

import com.mongodb.*;
import com.progress.backend.connections.DbInitBean;

import com.progress.backend.entities.UserEntity;
import com.progress.backend.utils.CommonUtils;
import com.progress.backend.utils.StatusTypeConstants;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import net.karmafiles.ff.core.tool.dbutil.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
@Service("userService")
@Component
public class UserService implements Serializable {

    private static final long serialVersionUID = 1L;
    @Autowired
    private DbInitBean initDatabase;

    public boolean drop() {
        try {
            initDatabase.getUserCollection().drop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean save(UserEntity userEntity) {
        try {
            boolean check = this.checkEmail(userEntity.getEmail());
            if (check) {
                System.out.println("Email " + userEntity.getEmail() + " already registered");
                return false;
            }
            userEntity.set_id(null);

            userEntity.setId(CommonUtils.longValue(DbInitBean.getNextId(initDatabase.getDatabase(), "userSeqGen")));
            userEntity.setRegisteredDate(new Date(System.currentTimeMillis()));
            DBObject dbObject = Converter.toDBObject(userEntity);
            WriteResult result = initDatabase.getUserCollection().insert(dbObject, WriteConcern.SAFE);
            if (result.getError() == null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<UserEntity> findAll() {
        List<UserEntity> userList = new ArrayList<UserEntity>();
        String sort = "registeredDate";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        DBCursor cursor = initDatabase.getUserCollection().find(query).sort(sortCriteria);
        try {
            while (cursor.hasNext()) {
                DBObject document = cursor.next();
                UserEntity entity = new UserEntity();
                entity = Converter.toObject(UserEntity.class, document);
                userList.add(entity);
            }
        } finally {
            cursor.close();
        }
        return userList;
    }

    public UserEntity findById(Long id) {
        UserEntity entity = null;
        BasicDBObject query = new BasicDBObject();
        query.put("id", id);
        DBCursor cursor = initDatabase.getUserCollection().find(query);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                entity = new UserEntity();
                entity = Converter.toObject(UserEntity.class, document);
            }
        } finally {
            cursor.close();
        }
        return entity;
    }

    public Integer getUserCount() {
        Integer listCount = 0;
        try {
            BasicDBObject query = new BasicDBObject();
            DBCursor cursor = initDatabase.getUserCollection().find(query);
            listCount = cursor.count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCount;
    }

    public boolean checkEmail(String email) {
        boolean retValue = false;
        BasicDBObject query = new BasicDBObject();
        query.put("email", email);
        DBCursor cursor = initDatabase.getUserCollection().find(query);
        try {
            if (cursor == null) {
                return false;
            }
            if (cursor.count() > 0) {
                retValue = true;
            } else {
                return false;
            }
        } finally {
            cursor.close();
        }
        return retValue;
    }
}
