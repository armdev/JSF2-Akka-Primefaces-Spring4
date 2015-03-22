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

package net.karmafiles.ff.core.tool.dbutil.converter;

import net.karmafiles.ff.core.tool.dbutil.converter.dbobject.DBObjectArrayProxy;
import net.karmafiles.ff.core.tool.dbutil.converter.dbobject.DBObjectIterableProxy;
import net.karmafiles.ff.core.tool.dbutil.converter.dbobject.DBObjectMapProxy;
import net.karmafiles.ff.core.tool.dbutil.converter.dbobject.DBObjectProxy;
import org.bson.BSONObject;
import org.bson.types.ObjectId;

import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public class BaseProxy {

    private static Class<?> matchType = (new byte[]{}).getClass().getComponentType();
    public static final String ENHANCED_SIGNATURE = "$$EnhancerByCGLIB$$";

    public static boolean isStandardClass(Class clazz) {
        if (clazz.isAssignableFrom(Date.class)) // standard, pass
            return true;
        else if (clazz.isAssignableFrom(Number.class)) // standard, pass
            return true;
        else if (clazz.isAssignableFrom(String.class)) // standard, pass
            return true;
        else if (clazz.isAssignableFrom(ObjectId.class)) // standard, pass
            return true;
        else if (clazz.isAssignableFrom(BSONObject.class)) // standard, pass
            return true;
        else if (clazz.isAssignableFrom(Boolean.class)) // standard, pass
            return true;
        else if (clazz.isAssignableFrom(Double.class)) // standard, pass
            return true;
        else if (clazz.isAssignableFrom(Integer.class)) // standard, pass
            return true;
        else if (clazz.isAssignableFrom(Long.class)) // standard, pass
            return true;
        else if (clazz.isAssignableFrom(Pattern.class)) // standard, pass
            return true;
        else if (clazz.isArray() && clazz.getComponentType().isAssignableFrom(matchType)) // TODO: check byte[] 
            return true;
        else if (clazz.isAssignableFrom(UUID.class)) // standard, pass
            return true;

        return false;
    }


    public static Object passValue(Object val, PropertyDescriptor propertyDescriptor) {
        if(val == null) {
            return null;
        }
        if(isStandardClass(val.getClass())) {
            return val;
        } else {
            if (val instanceof Map) // create proxy
                return DBObjectMapProxy.create((Map) val, propertyDescriptor);
            else if (val instanceof Iterable) {// create proxy
                return DBObjectIterableProxy.create((Iterable) val, propertyDescriptor);
            } else if (val.getClass().isArray()) // create proxy
                return DBObjectArrayProxy.create((Object[]) val);
            else if (val.getClass().isEnum()) // create proxy
                return ((Enum) val).name();
        }
        return DBObjectProxy.create(val);
    }

    public static Object passValue(Object val) {
        return passValue(val, null);
    }

    public static Class getGenericListClass(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            Type[] typeArgs = ((ParameterizedType) type).getActualTypeArguments();
            if(typeArgs == null || typeArgs.length == 0) {
                throw new RuntimeException("Unable to get generic type info (1).");
            }
            return getGenericListClass(typeArgs[0]);
        }
        throw new RuntimeException("Unable to get generic type info (2).");
    }


    public static MapClasses getGenericMapClasses(Type type) {
       MapClasses mapClasses = new MapClasses();
       if (type instanceof Class) {
            mapClasses.classA = Object.class;
            mapClasses.classB = (Class) type;
       } else if (type instanceof ParameterizedType) {
           Type[] typeArgs = ((ParameterizedType) type).getActualTypeArguments();
           if(typeArgs == null || typeArgs.length == 0) {
               throw new RuntimeException("Unable to get generic type info (3).");
           }
           mapClasses.classA = (Class)typeArgs[0];
           mapClasses.classB = (Class)typeArgs[1];
       }
       return mapClasses;
    }

    public static String getClassName(Object nextObj) {
        String className = nextObj.getClass().getName();
        if (!className.contains(ENHANCED_SIGNATURE)) {
            return className;
        }
        return className.substring(0, className.indexOf(ENHANCED_SIGNATURE));
    }
}
