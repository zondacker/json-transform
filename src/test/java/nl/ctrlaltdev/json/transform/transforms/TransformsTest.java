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
package nl.ctrlaltdev.json.transform.transforms;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import nl.ctrlaltdev.json.transform.path.Path;
import nl.ctrlaltdev.json.transform.transforms.IdentityTransform;
import nl.ctrlaltdev.json.transform.transforms.MoveTransform;
import nl.ctrlaltdev.json.transform.util.NodeUtils;
import nl.ctrlaltdev.json.transform.utils.TestUtils;

import org.junit.Before;
import org.junit.Test;

public class TransformsTest {

    private Map<String, Object> source;

    @Before
    public void init() {
        source = TestUtils.parseJson("/json/identity.json");
    }
    @Test
    public void shouldDoIdentityTransformOnObject() {
        Object dest = new IdentityTransform().apply(source);
        assertEquals(source, dest);
    }

    @Test
    public void shouldDoIdentityTransformOnArray() {
        List<Object> array = NodeUtils.newArray();
        array.add("value");
        Object dest = new IdentityTransform().apply(array);
        assertEquals(array, dest);
    }

    @Test
    public void shouldDoIdentityTransformOnValue() {
        String value = "value";
        Object dest = new IdentityTransform().apply(value);
        assertEquals(value, dest);
    }

    @Test
    public void shouldMoveTransformOnObject() {
        Map<String, Object> dest = NodeUtils.toObject(new MoveTransform(Path.root().enter("move")).apply(source));
        assertEquals(source, dest.get("move"));
    }

    @Test
    public void shouldMoveTransformOnArray() {
        List<Object> array = NodeUtils.newArray();
        array.add("value");
        Map<String, Object> dest = NodeUtils.toObject(new MoveTransform(Path.root().enter("move")).apply(array));
        assertEquals(array, dest.get("move"));
    }

    @Test
    public void shouldMoveTransformOnValue() {
        String value = "value";
        Map<String, Object> dest = NodeUtils.toObject(new MoveTransform(Path.root().enter("move")).apply(value));
        assertEquals(value, dest.get("move"));
    }

    @Test
    public void shouldMoveTransformToRoot() {
        Map<String, Object> dest = NodeUtils.toObject(new MoveTransform(Path.root()).apply(source));
        assertEquals(source, dest);
    }

}
