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
package nl.cad.json.transform.transforms.convert;

import nl.cad.json.transform.path.Path;
import nl.cad.json.transform.path.ValuePath;
import nl.cad.json.transform.transforms.ValuePathTransform;
import nl.cad.json.transform.util.NodeUtils;

public class RenamePropertyConversion implements ValuePathTransform {

    private String name;

    public RenamePropertyConversion(String name) {
        this.name = name;
    }

    @Override
    public void apply(ValuePath source, ValuePath target) {
        Object targetSource = target.get();
        Path rename = source.path().parent().enter(name);
        ValuePath renameValue = target.parent().enter(rename, null);
        renameValue.set(targetSource == null ? source.get() : targetSource);
        if (targetSource != null) {
            NodeUtils.toObject(target.parent().get()).remove(target.path().getTop());
        }
    }
}
