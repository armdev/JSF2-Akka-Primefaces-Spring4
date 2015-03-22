
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

import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;



public class BeanDescription {
    private Map<String, PropertyDescriptor> description = new HashMap<String, PropertyDescriptor>();
    private Map<String, PropertyDescriptor> writeMethodToDescriptor = new HashMap<String, PropertyDescriptor>();
    private Map<String, PropertyDescriptor> readMethodToDescriptor = new HashMap<String, PropertyDescriptor>();
    private Map<String, String> writeMethodToPropertyName = new HashMap<String, String>();
    private Map<String, String> readMethodToPropertyName = new HashMap<String, String>();

    private BeanDescription() {
    }

    private static Map<String, BeanDescription> knownBeanDescriptions = new ConcurrentHashMap<String, BeanDescription>();

    public static BeanDescription describe(Class beanClass) {
        BeanDescription beanDescription = knownBeanDescriptions.get(beanClass.getName());
        if(beanDescription != null) {
            return beanDescription;
        }

        try {
            beanDescription = new BeanDescription();
            PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(beanClass);

            for(PropertyDescriptor descriptor : descriptors) {
                if(descriptor.getReadMethod() == null || descriptor.getWriteMethod() == null) {
                    continue;
                }

                beanDescription.description.put(descriptor.getName(), descriptor);

                beanDescription.readMethodToDescriptor.put(
                        descriptor.getReadMethod().getName(), descriptor);

                beanDescription.writeMethodToDescriptor.put(
                        descriptor.getWriteMethod().getName(), descriptor);

                beanDescription.readMethodToPropertyName.put(
                        descriptor.getReadMethod().getName(), descriptor.getName());

                beanDescription.writeMethodToPropertyName.put(
                        descriptor.getWriteMethod().getName(), descriptor.getName());
            }
            knownBeanDescriptions.put(beanClass.getName(), beanDescription);
            return beanDescription;
        } catch(Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public PropertyDescriptor getPropertyDescriptor(String propertyName) {
        return description.get(propertyName);
    }

    public PropertyDescriptor getWriteMethodDescriptor(String writeMethodName) {
        return writeMethodToDescriptor.get(writeMethodName);
    }

    public PropertyDescriptor getReadMethodDescriptor(String readMethodName) {
        return readMethodToDescriptor.get(readMethodName);
    }

    public String getPropertyNameByReadMethod(String readMethodName) {
        return readMethodToPropertyName.get(readMethodName);
    }

    public String getPropertyNameByWriteMethod(String writeMethodName) {
        return writeMethodToPropertyName.get(writeMethodName);
    }

    public Set<String> getPropertyNames() {
        return description.keySet();
    }
}
