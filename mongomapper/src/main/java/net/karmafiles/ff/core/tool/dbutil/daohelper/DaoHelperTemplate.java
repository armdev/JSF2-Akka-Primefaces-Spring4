/*
 * Copyright 2011 Dadastream. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY DADASTREAM ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL DADASTREAM OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of Dadastream.
 *
 * @author Ilya Brodotsky
 * @author Timur Evdokimov
 */

package net.karmafiles.ff.core.tool.dbutil.daohelper;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import net.karmafiles.ff.core.tool.Assert;
import net.karmafiles.ff.core.tool.dbutil.converter.Converter;
import org.bson.types.ObjectId;

public abstract class DaoHelperTemplate<T> {

    private Class<T> type;
    private DBCollection dbCollection;

    public abstract void beforeAdd(DBObject dbObject);
    public abstract void beforeUpdate(DBObject dbObject);
    public abstract void beforeRemove(DBObject dbObject);

    protected DBCollection getDbCollection() {
        return dbCollection;
    }

    protected Class<T> getType() {
        return type;
    }

    protected void init(Class<T> type, DBCollection dbCollection) {
        Assert.notNull(type);
        Assert.notNull(dbCollection);

        this.type = type;
        this.dbCollection = dbCollection;
    }

    private DBObject idQuery(String id) {
        Assert.notNull(id);
        return QueryBuilder.start("_id").is(id).get();
    }

    public T add(T entity) {
        return add(entity, true);
    }

    public T add(T entity, boolean checkExists) {
        Assert.notNull(entity, "entity may not be null");

        DBObject dbObject = Converter.toDBObject(entity);
        if (checkExists)  {
            String id = (String)dbObject.get("_id");
            if (id != null) {
                if (getDbCollection().findOne(idQuery(id)) != null) {
                    throw new DaoException("Can't add: object with id='"
                            + id + "' already exists in the collection '" + dbCollection.getFullName() + "'.");
                }
            }
        }

        beforeAdd(dbObject);

        getDbCollection().save(dbObject);

        return Converter.toObject(getType(), dbObject);
    }

    public T get(String id) {
        DBObject dbObject = getDbCollection().findOne(idQuery(id));
        if(dbObject != null) {
            return Converter.toObject(getType(), dbObject);
        } else {
            return null;
        }
    }

    public void remove(String id) {
        Assert.notNull(id);

        DBObject entity = getDbCollection().findOne(idQuery(id));
        if(entity == null) {
            throw new DaoException("Can't remove: object with id='"
                    + id + "' was not found in the collection '" + dbCollection.getFullName() + "'.");
        }

        beforeRemove(entity);
        
        getDbCollection().remove(entity);
    }

    public T update(T entity) {


        Assert.notNull(entity);
        DBObject dbObject = Converter.toDBObject(entity);

        beforeUpdate(dbObject);

        String id = (String)dbObject.get("_id");
        Assert.notNull(id, "Can't update: id may not be null");

        getDbCollection().update(idQuery(id), dbObject);

        // kind of need to do this explicitly to avoid stale data 
        getDbCollection().getDB().command("{fsync:1}");

        return Converter.toObject(getType(), dbObject);
    }

}
