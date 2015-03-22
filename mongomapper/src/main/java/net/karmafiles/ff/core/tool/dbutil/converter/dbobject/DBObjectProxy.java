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

package net.karmafiles.ff.core.tool.dbutil.converter.dbobject;

import com.mongodb.DBObject;
import net.karmafiles.ff.core.tool.dbutil.converter.BaseProxy;
import net.karmafiles.ff.core.tool.dbutil.converter.BeanDescription;
import org.apache.commons.beanutils.PropertyUtils;
import org.bson.BSONObject;

import java.util.*;

public class DBObjectProxy extends BaseProxy implements DBObject  {

    private Object bean;
    private BeanDescription beanDescription;
    private Map<String, String> genericValues;

    public static DBObjectProxy create(Object object) {

        if(object == null) {
            throw new RuntimeException("Passed object is null");
        }

        DBObjectProxy dbObjectProxy = new DBObjectProxy();
        dbObjectProxy.bean = object;
        dbObjectProxy.beanDescription = BeanDescription.describe(object.getClass());

        return dbObjectProxy;
    }

    public void addGenericInfo(Map<String,String> values) {
        this.genericValues = values;    
    }

    public Object put(String key, Object v) {
        try {
            PropertyUtils.setProperty(bean, key, v);
            return v;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public void putAll(BSONObject o) {
        for(String key : o.keySet()) {
            put(key, o.get(key));
        }
    }

    public void putAll(Map m) {
        for (Map.Entry entry : (Set<Map.Entry>)m.entrySet()){
            put(entry.getKey().toString(), entry.getValue());
        }
    }

    public Object get(String key) {
        if(beanDescription.getPropertyDescriptor(key) == null) {
            if(genericValues != null && genericValues.containsKey(key)) {
                return genericValues.get(key);
            }
            return null;
        }
        
        Object val;
        try {
            val = PropertyUtils.getProperty(bean, key);
        } catch (Throwable t) {
            throw new RuntimeException("Can't get value from " + key , t);
        }

        return passValue(val, beanDescription.getPropertyDescriptor(key));
    }

    public Map toMap() {
        Map m = new HashMap();

        for (String key : beanDescription.getPropertyNames()) {
            m.put(key, get(key));
        }

        if(genericValues != null) {
            for (String key : genericValues.keySet()) {
                m.put(key, genericValues.get(key));
            }
        }
        
        return m;
    }

    public Object removeField(String key) {
        throw new RuntimeException("Unsupported method.");
    }

    public boolean containsKey(String s) {
        return genericValues != null && genericValues.containsKey(s) || beanDescription.getPropertyDescriptor(s) != null;

    }

    public boolean containsField(String s) {
        return containsKey(s);
    }

    public Set<String> keySet() {
        Set<String> set = new HashSet<String>();
        set.addAll(beanDescription.getPropertyNames());
        if(genericValues != null) {
            set.addAll(genericValues.keySet());
        }
        return set;
    }

    public void markAsPartialObject() {
        throw new RuntimeException("Method not implemented.");
    }

    public boolean isPartialObject() {
        return false;
    }
}
