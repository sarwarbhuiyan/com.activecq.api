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
package com.activecq.api.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author david
 */
public class EncodingUtil {

    /**
     * Base64 Decode data from UTF-8/URL Friendly format
     *
     * @param encodedData
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String decode(String encodedData) throws UnsupportedEncodingException {
        return new String(new Base64(0, null, true).decode(encodedData.getBytes("UTF-8")));
    }

    /**
     * Base64 Encode data to UTF-8/URL Friendly format
     *
     * @param unencodedData
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encode(String unencodedData) throws UnsupportedEncodingException {
        return new String(new Base64(0, null, true).encode(unencodedData.getBytes("UTF-8")));
    }
}
