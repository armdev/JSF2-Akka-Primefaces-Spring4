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

import com.mongodb.BasicDBList;
import net.karmafiles.ff.core.tool.dbutil.converter.BaseProxy;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DBObjectIterableProxy extends BasicDBList {


    public static DBObjectIterableProxy create(Iterable iterable, PropertyDescriptor propertyDescriptor) {
        if(iterable == null) {
            throw new RuntimeException("Passed object is null");
        }

        DBObjectIterableProxy dbObjectIterableProxy = new DBObjectIterableProxy();
        Class genericClass = null;
        if (propertyDescriptor != null) {
            genericClass = BaseProxy.getGenericListClass(propertyDescriptor.getReadMethod().getGenericReturnType());
        }

        for (Object nextObj : iterable) {

            Object obj = BaseProxy.passValue(nextObj);

            if (obj instanceof DBObjectProxy) { // we have converted POJO
                // and generic field list class doesnt match POJO's class 
                if (genericClass != null && !BaseProxy.getClassName(nextObj).equals(genericClass.getName())) {
                    Map<String, String> genericInfo = new HashMap<String, String>();
                    genericInfo.put("implementation", BaseProxy.getClassName(nextObj));
                    ((DBObjectProxy) obj).addGenericInfo(genericInfo);
                }
            }

            dbObjectIterableProxy.add(obj);
        }

        return dbObjectIterableProxy;
    }

}
