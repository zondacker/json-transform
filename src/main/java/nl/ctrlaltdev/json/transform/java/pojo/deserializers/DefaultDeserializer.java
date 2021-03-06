/*
 * Copyright 2015 E.Hooijmeijer / www.ctrl-alt-dev.nl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.ctrlaltdev.json.transform.java.pojo.deserializers;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.ctrlaltdev.json.transform.java.pojo.FromDocumentMapper;
import nl.ctrlaltdev.json.transform.java.pojo.deserializers.value.DefaultValueDeserializer;
import nl.ctrlaltdev.json.transform.java.pojo.deserializers.value.ValueDeserializer;

public class DefaultDeserializer implements Deserializer {

    public static final class UnsupportedCollectionTypeException extends RuntimeException {
        public UnsupportedCollectionTypeException(Class<?> type) {
            super(type.getName());
        }
    }
    
    public static final class DocumentMappingException extends RuntimeException {
        public DocumentMappingException(Throwable ex) {
            super(ex);
        }

        public DocumentMappingException(String msg) {
            super(msg);
        }
    }

    private boolean strict;
    private TypeSolver typeSolver;
    private ValueDeserializer[] valueDeserializers;

    public DefaultDeserializer() {
        this(true);
    }

    public DefaultDeserializer(boolean strict) {
        this(strict, new PropertyTypeSolver(), new DefaultValueDeserializer());
    }

    public DefaultDeserializer(boolean strict, ValueDeserializer... deserializers) {
        this(strict, new PropertyTypeSolver(), deserializers);
    }

    public DefaultDeserializer(boolean strict, TypeSolver typeSolver, ValueDeserializer... deserializers) {
        this.strict = strict;
        this.typeSolver = typeSolver;
        this.valueDeserializers = deserializers;
    }

    @Override
    public boolean supports(Class<?> type) {
        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Object toObject(FromDocumentMapper mapper, Class<?> type, Class<?> genericType, Object document) {
        if (document == null) {
            return null;
        } else if (document instanceof Map) {
            return handleObject(mapper, type, (Map) document);
        } else if (document instanceof List) {
            return handleList(mapper, type, genericType, (List) document);
        } else {
            return handleValue(type, document);
        }
    }

    private Object handleValue(Class<?> type, Object document) {
        for (ValueDeserializer valueDeserializer : valueDeserializers) {
            if (valueDeserializer.accept(null, type, null)) {
                return valueDeserializer.deserialize(type, document);
            }
        }
        return document;
    }

    private Object handleList(FromDocumentMapper mapper, Class<?> type, Class<?> genericType, List<Object> document) {
        try {
            if (type.isArray()) {
                Object[] result = (Object[]) Array.newInstance(type.getComponentType(), document.size());
                for (int t = 0; t < document.size(); t++) {
                    result[t] = mapper.toJava(type.getComponentType(), null, document.get(t));
                }
                return result;
            } else {
                Collection<Object> result = createCollectionInstance(type);
                for (Object o : document) {
                    result.add(mapper.toJava(genericType, null, o));
                }
                return result;
            }
        } catch (ReflectiveOperationException ex) {
            throw new DocumentMappingException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    private Collection<Object> createCollectionInstance(Class<?> type) throws InstantiationException, IllegalAccessException {
        Collection<Object> result = null;
        if (type.isInterface()) {
            if (List.class.isAssignableFrom(type)) {
                result = new ArrayList<Object>();
            } else if (Set.class.isAssignableFrom(type)) {
                result = new HashSet<Object>();
            } else {
                throw new UnsupportedCollectionTypeException(type);
            }
        } else {
            result = (Collection<Object>) type.newInstance();
        }
        return result;
    }

    @SuppressWarnings({ "rawtypes" })
    private Object handleObject(FromDocumentMapper mapper, Class<?> type, Map<String, Object> document) {
        try {
            Set<String> handled = new HashSet<String>();
            Class solvedType = typeSolver.solveType(type, document, handled);
            Object target = solvedType.newInstance();
            handleObject(mapper, solvedType, document, target, handled);
            if (strict) {
                Set<String> keySet = document.keySet();
                keySet.removeAll(handled);
                if (!keySet.isEmpty()) {
                    throw new DocumentMappingException("Field(s) " + keySet + " not found in " + type.getName());
                }
            }
            return target;
        } catch (ReflectiveOperationException ex) {
            throw new DocumentMappingException(ex);
        }
    }

    private void handleObject(FromDocumentMapper mapper, Class<?> type, Map<String, Object> document, Object target, Set<String> handled)
            throws ReflectiveOperationException {
        if (!type.getSuperclass().equals(Object.class)) {
            handleObject(mapper, type.getSuperclass(), document, target, handled);
        }
        for (Map.Entry<String, Object> entry : document.entrySet()) {
            Field field = getField(type, entry.getKey());
            if (field != null) {
                Class<?> genericType = getGenericType(field.getGenericType());
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                field.set(target, mapper.toJava(field.getType(), genericType, entry.getValue()));
                handled.add(entry.getKey());
            }
        }
    }

    private Field getField(Class<?> type, String name) {
        try {
            return type.getDeclaredField(name);
        } catch (NoSuchFieldException ex) {
            if (type.getSuperclass() != null) {
                return getField(type.getSuperclass(), name);
            }
            return null;
        }
    }

    private Class<?> getGenericType(Type genericType) {
        if (genericType instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
        }
        return null;
    }

}
