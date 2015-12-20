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
package nl.cad.json.transform.select;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import nl.cad.json.transform.path.Path;
import nl.cad.json.transform.select.selector.Selector;

public class SingleResultSelectorChain extends SelectorChain {

    public SingleResultSelectorChain(Selector... selectors) {
        super(selectors);
    }

    public Map<Path, Object> select(Object source) {
        Map<Path, Object> result = new TreeMap<Path, Object>();
        Map<Path, Object> tmp = super.select(source);
        if (tmp.size() > 0) {
            Entry<Path, Object> next = tmp.entrySet().iterator().next();
            result.put(next.getKey(), next.getValue());
        }
        return result;
    }

}