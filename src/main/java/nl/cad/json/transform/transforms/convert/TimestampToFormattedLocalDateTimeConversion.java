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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import nl.cad.json.transform.path.Path;
import nl.cad.json.transform.transforms.Transform;
import nl.cad.json.transform.util.NodeUtils;

public class TimestampToFormattedLocalDateTimeConversion implements Transform {

    private DateTimeFormatter formatter;

    public TimestampToFormattedLocalDateTimeConversion() {
        this(DateTimeFormatter.ISO_DATE_TIME);
    }

    public TimestampToFormattedLocalDateTimeConversion(String format) {
        this(DateTimeFormatter.ofPattern(format));
    }

    public TimestampToFormattedLocalDateTimeConversion(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public Map<String, Object> apply(Path path, Object source) {
        Map<String, Object> target = NodeUtils.newObject();
        long timestamp = Long.valueOf(String.valueOf(source));
        LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
        path.set(target, formatter.format(localDateTime));
        return target;
    }

}
