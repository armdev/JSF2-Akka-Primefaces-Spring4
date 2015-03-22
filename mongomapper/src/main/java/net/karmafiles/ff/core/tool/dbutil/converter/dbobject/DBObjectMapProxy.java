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
import net.karmafiles.ff.core.tool.dbutil.converter.MapClasses;
import org.bson.BSONObject;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DBObjectMapProxy extends BaseProxy implements DBObject {

    private Map<String, Object> map;
    private Class genericClass;

    public static DBObjectMapProxy create(Map map, PropertyDescriptor propertyDescriptor) {
        if(map == null) {
            throw new RuntimeException("Passed object is null");
        }

        DBObjectMapProxy dbObjectMapProxy = new DBObjectMapProxy();
        dbObjectMapProxy.map = new HashMap<String, Object>();

        dbObjectMapProxy.genericClass = getGenericMapClasses(propertyDescriptor.getReadMethod().getGenericReturnType()).classB;

        for (Object s: map.keySet()) {
            Object value = map.get(s);
            String key;
            try {
                key = s.getClass().isEnum() ? s.toString() : (String) s;
            } catch (Exception e) {
                throw new IllegalArgumentException("Only String or enum map keys are supported");
            }

            dbObjectMapProxy.put(key, value);
        }
        return dbObjectMapProxy;
    }

    private Object convertAndEnhance(Object value) {
        Object obj = BaseProxy.passValue(value);

        if(obj instanceof DBObjectProxy) { // we have converted POJO
            // and generic field list class doesnt match POJO's class
            if(!BaseProxy.getClassName(value).equals(genericClass.getName())) {
                Map<String, String> genericInfo = new HashMap<String, String>();
                genericInfo.put("implementation", BaseProxy.getClassName(value));
                ((DBObjectProxy)obj).addGenericInfo(genericInfo);
            }
        }
        return obj;
    }


    public Object put(String key, Object v) {
        return map.put(key, convertAndEnhance(v));
    }

    public void putAll(BSONObject o) {
        for(String key : o.keySet()) {
            put(key, o.get(key));
        }
    }

    public void putAll(Map m) {
        map.putAll(m);
    }

    public Object get(String key) {
        return map.get(key);
    }

    public Map toMap() {
        return map;
    }

    public Object removeField(String key) {
        return map.remove(key);
    }

    @SuppressWarnings("deprecated")
    public boolean containsKey(String s) {
        return map.containsKey(s);
    }

    @SuppressWarnings("deprecated")
    public boolean containsField(String s) {
        return containsKey(s);
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public void markAsPartialObject() {
        throw new RuntimeException("Method not implemented.");
    }

    public boolean isPartialObject() {
        return false;
    }
}
