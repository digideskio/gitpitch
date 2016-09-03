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

import com.gitpitch.models.GitRepoModel;
import com.gitpitch.utils.PitchParams;
import com.fasterxml.jackson.databind.JsonNode;

/*
 * Git Respository Service common interface.
 */
public interface GRSService {

    /*
     * Return true if apiPath is a call on this GRSService instance.
     */
    public boolean call(PitchParams pp, String apiPath);

    /*
     * Return zero if file download completes successfully.
     */
    public int download(PitchParams pp, String filename);

    /*
     * Return model representing Git repository meta-data.
     */
    public GitRepoModel model(PitchParams pp, JsonNode json);

    /*
     * Return Raw API path for /user/repo/branch.
     */
    public String raw(PitchParams pp);

    /*
     * Return Raw API path for /user/repo/branch/filename.
     */
    public String raw(PitchParams pp, String filename);

    /*
     * Return Raw API path for /user/repo/branch/filename with 
     * optional query param used to bypass GRS API caching.
     */
    public String raw(PitchParams pp, String filename, boolean bypassCache);

    /*
     * Return API path for repository meta call for /user/repo.
     */
    public String repo(PitchParams pp);

}
