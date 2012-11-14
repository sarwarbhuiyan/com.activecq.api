/*
 * Copyright 2012 david.
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
package com.activecq.api.plugins.options;

/**
 *
 * @author david
 */
public enum TextOptions {

    DIFF, RAW, XSS, I18N;

    public boolean isIn(TextOptions... options) {
        if (options == null) {
            return false;
        }
        for (TextOptions option : options) {
            if (this.equals(option)) {
                return true;
            }
        }

        return false;
    }
}
