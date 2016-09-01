/*
 * MIT License
 *
 * Copyright (c) 2016 David Russell
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.gitpitch.git;

import com.gitpitch.utils.PitchParams;
import java.util.Map;

/*
 * Git Respository Service model.
 */
public class GRS {

    private final String id;
    private final String type;
    private final String apiBase;
    private final String apiToken;
    private final String rawBase;
    private final boolean isDefault;

    private GRS(String id,
                String type,
                String apiBase,
                String apiToken,
                String rawBase,
                boolean isDefault) {

        this.id = id;
        this.type = type;
        this.apiBase = apiBase;
        this.apiToken = apiToken;
        this.rawBase = rawBase;
        this.isDefault = isDefault;
    }

    public static GRS build(Map<String,String> grsCfg) {

        String id = grsCfg.get("id");
        String type = grsCfg.get("type");
        String apiBase = grsCfg.get("apibase");
        String apiToken = grsCfg.get("apitoken");
        String rawBase = grsCfg.get("rawbase");
        boolean isDefault = Boolean.parseBoolean(grsCfg.get("default"));

        if(id != null && type != null && apiBase != null && rawBase != null) {
            return new GRS(id, type, apiBase, apiToken, rawBase, isDefault);
        } else {
            return null;
        }
    }

    public String id() { return id; }
    public String type() { return type; }
    public String apiBase() { return apiBase; }
    public String apiToken() { return apiToken; }
    public String rawBase() { return rawBase; }
    public boolean isDefault() { return isDefault; }

}
