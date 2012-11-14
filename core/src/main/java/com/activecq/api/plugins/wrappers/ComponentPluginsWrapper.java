/*
 * Copyright 2012 david gonzalez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.activecq.api.plugins.wrappers;

import com.activecq.api.plugins.*;
import org.apache.sling.api.SlingHttpServletRequest;

/**
 *
 * @author david
 */
public class ComponentPluginsWrapper {

    public final WCMModePlugin WCMMode;
    public final XSSPlugin XSS;
    public final I18nPlugin I18n;
    public final DiffPlugin Diff;
    public final TextPlugin Text;
    public final PersistencePlugin Persistence;

    public ComponentPluginsWrapper(SlingHttpServletRequest request) {
        /*
        * General Helpers
        */
        this.Persistence = new PersistencePlugin(request);
        this.WCMMode = new WCMModePlugin(request);
        /*
        * Text manipulation
        */
        this.XSS = new XSSPlugin(request);
        this.I18n = new I18nPlugin(request);
        this.Diff = new DiffPlugin(request);
        this.Text = new TextPlugin(request);
    }
}
