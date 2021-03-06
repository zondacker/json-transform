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
package nl.ctrlaltdev.json.transform.transforms.structural;

import nl.ctrlaltdev.json.transform.path.Path;
import nl.ctrlaltdev.json.transform.path.ValuePath;
import nl.ctrlaltdev.json.transform.path.relative.RelativePath;
import nl.ctrlaltdev.json.transform.transforms.ValuePathTransform;
import nl.ctrlaltdev.json.transform.util.NodeUtils;

public class RelativeMovePropertyConversion implements ValuePathTransform {

    private RelativePath relativePath;

    public RelativeMovePropertyConversion(RelativePath relativePath) {
        this.relativePath = relativePath;
    }

    @Override
    public void apply(ValuePath source, ValuePath target) {
        Path targetPath = relativePath.apply(target.path());
        Object value = target.get();
        if (value == null) {
            value = source.get();
        } else {
            NodeUtils.toObject(target.parent().get()).remove(target.path().getTop());
        }
        targetPath.create(target.getRoot());
        targetPath.set(target.getRoot(), value);
    }

}
