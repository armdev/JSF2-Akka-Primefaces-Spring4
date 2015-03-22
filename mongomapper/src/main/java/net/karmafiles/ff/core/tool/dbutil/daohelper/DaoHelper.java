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
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import net.karmafiles.ff.core.tool.IdGenerator;
import net.karmafiles.ff.core.tool.dbutil.ConnectionImpl;
import net.karmafiles.ff.core.tool.dbutil.converter.Converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class DaoHelper<T> extends BaseDaoHelper<T> {

    private static final int MAX_NUMBER_AS_LIST = 10000;

    protected void init(Class<T> type) {
        this.init(type, extractCollectionName(type)); // by default, store in collections per class name
    }

    private String extractCollectionName(Class<T> type) {
        String fqName = type.getName();
        return fqName.substring(fqName.lastIndexOf(".")+1, fqName.length());
    }

    /**
     * Must be overridden in subclasses with specific values for class type and collection name
     * and be given @PostConstruct annotation.
     *
     * @param type class, served by this DAO subclass
     * @param dbCollectionName collection name
     */

    protected void init(Class<T> type, String dbCollectionName) {
        this.init(type, getConnection().getCollection(dbCollectionName));
    }

    public abstract ConnectionImpl getConnection();

    protected void init(Class<T> type, DBCollection dbCollection) {
        super.init(type, dbCollection);

        renewFieldOnCreation("created", new FieldValueProvider() {
            public Object provide(DBCollection dbCollection, DBObject dbObject, String fieldName) {
                return new Date();
            }
        });

        renewFieldOnCreation("modified", new FieldValueProvider() {
            public Object provide(DBCollection dbCollection, DBObject dbObject, String fieldName) {
                return new Date();
            }
        });

        renewFieldOnUpdate("modified", new FieldValueProvider() {
            public Object provide(DBCollection dbCollection, DBObject dbObject, String fieldName) {
                return new Date();
            }
        });

        chainCreationFilter(new DaoHelperFilter() {
            public DBObject doFilter(DBCollection dbCollection, DBObject entity) {
                if(entity.get("_id") == null) {
                    entity.put("_id", generateNewId(entity));
                }
                return entity;
            }
        });
    }

    // either one has to be overridden
    
    public String generateNewId(DBObject entity) {
        return generateNewId();
    }

    public String generateNewId() {
        return IdGenerator.createSecureId();
//        throw new DaoException("Id generator not set. To create entities with null ids please " +
//                "override generateNewId() method in DaoHelper<T>.");
    }

    public List<T> findAll() {

        return findAllWithFilter(null);

    }

    public List<T> findAllWithFilter(EntityFilter<T> filter) {

        List<T> entities = new ArrayList<T>();

        DBCursor cursor = getDbCollection().find();
        if (cursor.count() < MAX_NUMBER_AS_LIST) {
            while (cursor.hasNext()) {
                T t = Converter.toObject(getType(), cursor.next());
                if (filter == null) {
                    entities.add(t);
                } else if (filter.accepts(t)) {
                    entities.add(t);
                }
            }
            return entities;
        } else {
            throw new IllegalStateException("Iterable lists for large collections not implemented yet");
        }

    }

    public T create() {
        try {
            return getType().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Can't instantiate " + getType(), e);
        }
    }



}
