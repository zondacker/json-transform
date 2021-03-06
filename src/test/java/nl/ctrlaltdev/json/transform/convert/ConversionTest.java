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
package nl.ctrlaltdev.json.transform.convert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import nl.ctrlaltdev.json.transform.path.ValuePath;
import nl.ctrlaltdev.json.transform.transforms.convert.string.ToStringValueConversion;
import nl.ctrlaltdev.json.transform.transforms.convert.time.FormatTimestampToLocalDateTimeConversion;

import org.junit.Test;

public class ConversionTest {

    @Test
    public void shouldToString() {
        ValuePath source = new ValuePath(Long.valueOf(42));
        ValuePath target = new ValuePath(null);
        new ToStringValueConversion().apply(source, target);
        assertEquals("42", target.get());
    }

    @Test
    public void shouldToFormatTimestamp() {
        ValuePath source = new ValuePath(Long.valueOf(0));
        ValuePath target = new ValuePath(null);
        new FormatTimestampToLocalDateTimeConversion("yyyy-MM-dd").apply(source, target);
        assertNotNull(target.get());
        assertTrue(String.valueOf(target.get()).startsWith("19"));
    }

}
