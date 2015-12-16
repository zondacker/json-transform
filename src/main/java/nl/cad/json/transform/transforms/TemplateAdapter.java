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
package nl.cad.json.transform.transforms;

import java.util.Map;

import nl.cad.json.transform.merge.MergeFactory;
import nl.cad.json.transform.merge.MergeStrategy;
import nl.cad.json.transform.path.Path;
import nl.cad.json.transform.template.Template;
import nl.cad.json.transform.util.NodeUtils;

/**
 * Allows you to use a {@link Template} as {@link Transform}.
 */
public class TemplateAdapter implements Transform {

    private Template template;
    private MergeStrategy merge;

    public TemplateAdapter(Template template) {
        this.template = template;
        this.merge = MergeFactory.join();
    }

    @Override
    public void apply(Path path, Object source, Map<String, Object> target) {
        Map<String, Object> result = template.fill(NodeUtils.toObject(source));
        merge.merge(result, target);
    }

}
