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

import com.mongodb.DBObject;
import net.karmafiles.ff.core.tool.dbutil.converter.bean.BeanEnhancer;
import org.apache.commons.beanutils.PropertyUtils;

import java.util.Map;

public class Converter {


    public static DBObject toDBObject(Object obj) {
        Object val = BaseProxy.passValue(obj);
        if (val instanceof DBObject) {
            return (DBObject) val;
        } else {
            throw new RuntimeException("Converted object is not instance of DBObject");
        }
    }

    @SuppressWarnings({"unchecked"})
    public static <T> T toObject(Class<T> clazz, DBObject source) {
        T newObject = (T) BeanEnhancer.create(clazz, source);
        try {
            T copyObject = clazz.newInstance();
            // todo maybe other way of solving this?
            // if no complex entities with polymorphism are used, it should be safe to just "return newObject" and skip bean copying
            PropertyUtils.copyProperties(copyObject, newObject);
            return copyObject;
        } catch (Exception e) {
            return newObject;
        }
    }

}
