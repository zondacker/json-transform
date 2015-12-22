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
package nl.cad.json.transform.select.selector;

import nl.cad.json.transform.path.Path;

public class PropertyValueSelector implements Selector {

    private final String property;
    private Object value;

    public PropertyValueSelector(String property, Object value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public boolean matches(Path path, Object value) {
        if (path.isProperty()) {
            return property.equals(path.getTop()) && (this.value.equals(value));
        }
        return false;
    }

}