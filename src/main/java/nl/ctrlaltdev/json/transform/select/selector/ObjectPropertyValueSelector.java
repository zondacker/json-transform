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
package nl.ctrlaltdev.json.transform.select.selector;

import java.util.Map;

import nl.ctrlaltdev.json.transform.path.ValuePath;

public class ObjectPropertyValueSelector implements Selector {

    private final String property;
    private final Object propertyValue;

    public ObjectPropertyValueSelector(String property, Object propertyValue) {
        this.property = property;
        this.propertyValue = propertyValue;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean matches(ValuePath path) {
        if (path.value() instanceof Map) {
            return propertyValue.equals(((Map) path.value()).get(property));
        }
        return false;
    }

}
