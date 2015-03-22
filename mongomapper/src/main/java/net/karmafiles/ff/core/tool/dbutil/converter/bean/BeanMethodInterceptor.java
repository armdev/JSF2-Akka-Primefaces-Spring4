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

package net.karmafiles.ff.core.tool.dbutil.converter.bean;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import net.karmafiles.ff.core.tool.dbutil.converter.*;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.*;

public abstract class BeanMethodInterceptor implements MethodInterceptor {
    protected abstract DBObject getSource();
    protected abstract BeanDescription getBeanDescription();

    private Set<String> fieldsSet = new HashSet<String>();

    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        String property;
        property = getBeanDescription().getPropertyNameByReadMethod(method.getName());
        if (property != null) { // we've intercepted getter met hod
            if(fieldsSet.contains(property)) { // value was retrieved and already set, passing to original getter
                return proxy.invokeSuper(obj, args);
            }
            if (getSource().containsField(property)) { // we have such field in source
                PropertyDescriptor propertyDescriptor = getBeanDescription().getPropertyDescriptor(property);
                Object sourceValue = getSource().get(property);
                Object resultValue = null;

                if(sourceValue != null) {
                    if(propertyDescriptor.getPropertyType().isAssignableFrom(Set.class)) {
                        if(!(sourceValue instanceof BasicDBList)) { // all lists in DBObject are instances of BasicDBList
                            throw new RuntimeException("Source value is not instance of BasicDBList");
                        }

                        Class c = BaseProxy.getGenericListClass(propertyDescriptor.getReadMethod().getGenericReturnType());
                        boolean isStandardClass = BaseProxy.isStandardClass(c);
                        BasicDBList basicDBList = (BasicDBList)sourceValue;
                        Set set = new HashSet();
                        for(Object o : basicDBList) {
                            if(isStandardClass) {
                                set.add(o);
                            } else if (c.isEnum() && o instanceof String) {
                                set.add(Enum.valueOf(c, (String) o));
                            } else {
                                set.add(createInstance(c, o));
                            }
                        }

                        resultValue = set;

                    } else if(propertyDescriptor.getPropertyType().isAssignableFrom(Map.class)) {
                        if(!(sourceValue instanceof DBObject)) { // Map is filled from DBObject 
                            throw new RuntimeException("Source value is not instance of DBObject");
                        }
                        MapClasses mapClasses = // get generic descriptors, expecting <String, ?>
                                BaseProxy.getGenericMapClasses(propertyDescriptor.getReadMethod().getGenericReturnType());
                        boolean enumKey = mapClasses.classA.isEnum();
                        if(!mapClasses.classA.isAssignableFrom(String.class) && !enumKey) { // the key must be string
                            throw new RuntimeException("Map keys must be of class String or enum");
                        }
                        boolean isStandardClass = BaseProxy.isStandardClass(mapClasses.classB);
                        DBObject dbObject = (DBObject)sourceValue;
                        Map map = new HashMap();
                        for(String k : dbObject.keySet()) {

                            Object value = dbObject.get(k);
                            Object key = enumKey ? Enum.valueOf(mapClasses.classA, k) : k;

                            if (isStandardClass) {
                                // to prevent Mongo from supplying us Integers and Longs in Map<String,String>
                                map.put(key, (value != null &&
                                               !(value instanceof String)
                                                && String.class.equals(mapClasses.classB)) ?
                                            value.toString() : value);

                            } else if (mapClasses.classB.isEnum() && value instanceof String) {
                                map.put(key, Enum.valueOf(mapClasses.classB, (String) value));
                            } else {
                                map.put(key, createInstance(mapClasses.classB, value));
                            }
                        }

                        resultValue = map;
                        
                    } else if(propertyDescriptor.getPropertyType().isAssignableFrom(List.class)) {
                        if(!(sourceValue instanceof BasicDBList)) { // all lists in DBObject are instances of BasicDBList
                            throw new RuntimeException("Source value is not instance of BasicDBList");
                        }

                        Class c = BaseProxy.getGenericListClass(propertyDescriptor.getReadMethod().getGenericReturnType());
                        boolean isStandardClass = BaseProxy.isStandardClass(c);
                        BasicDBList basicDBList = (BasicDBList)sourceValue;
                        List list = new ArrayList();
                        for(Object o : basicDBList) {
                            if(isStandardClass) {
                                list.add(o);
                            } else {
                                list.add(createInstance(c, o));
                            }
                        }

                        resultValue = list;                           

                    } else if(propertyDescriptor.getPropertyType().isEnum()) {

                        Class c = BaseProxy.getGenericListClass(propertyDescriptor.getReadMethod().getGenericReturnType());
                        resultValue = Enum.valueOf(c, (String) sourceValue);

                    } else if(sourceValue instanceof DBObject) {
                        resultValue = Converter.toObject(propertyDescriptor.getPropertyType(), (DBObject)sourceValue);
                    }

                    if(resultValue == null) { // other types are passed with no change
                        resultValue = sourceValue;
                    }
                }

                // setProperty will fire the setter method interception, and the setter will call invokeSuper
                PropertyUtils.setProperty(obj, property, resultValue);
                fieldsSet.add(property);

                return resultValue;

            } else { // we have no value in source.
                if(!getBeanDescription().getPropertyDescriptor(property).getPropertyType().isPrimitive()) {
                    PropertyUtils.setProperty(obj, property, null);
                }
                fieldsSet.add(property);
                
                return null;
            }
        } else {
            // we are in setter method, or property is unavailable, or other method is called (why?).
            // Passing call to the implementation
            property = getBeanDescription().getPropertyNameByWriteMethod(method.getName());
            if(property != null) {
                fieldsSet.add(property);
            }
            return proxy.invokeSuper(obj, args);             
        }
    }

    private Object createInstance(Class c, Object o) throws ClassNotFoundException {
        DBObject dbObject = (DBObject)o;
        Object instance;
        if(dbObject.containsField("implementation")) {
            Class genericClass = Class.forName((String)dbObject.get("implementation"));
            instance = Converter.toObject(genericClass, dbObject);
        } else {
            instance = Converter.toObject(c, dbObject);
        }
        return instance;
    }
}
