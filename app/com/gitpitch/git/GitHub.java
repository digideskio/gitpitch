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

import com.gitpitch.git.GRS;
import com.gitpitch.git.GRSManager;
import com.gitpitch.git.GRSService;
import com.gitpitch.utils.PitchParams;
import javax.inject.*;
import play.Logger;
import play.Logger.ALogger;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * GitHub API Service.
 */
@Singleton
public class GitHub implements GRSService {

    private final Logger.ALogger log = Logger.of(this.getClass());

    private final AtomicInteger cacheBypass = new AtomicInteger();
    private final GRSManager grsManager;

    @Inject
    public GitHub(GRSManager grsManager) {
        this.grsManager = grsManager;
    }

    public String raw(PitchParams pp) {

        GRS grs = grsManager.get(pp.grs);

        return new StringBuffer(grs.rawBase())
                .append(pp.user)
                .append(SLASH)
                .append(pp.repo)
                .append(SLASH)
                .append(pp.branch)
                .append(SLASH)
                .toString();
    }

    public String raw(PitchParams pp, String filename) {
        return raw(pp, filename, false);
    }

    public String raw(PitchParams pp,
                      String filename,
                      boolean bypassCache) {

        if (bypassCache) {
            return raw(pp) + filename +
                    "?gp=" + cacheBypass.getAndIncrement();
        } else {
            return raw(pp) + filename;
        }
    }

    public String repo(PitchParams pp) {

        GRS grs = grsManager.get(pp.grs);

        return new StringBuffer(grs.apiBase())
                .append(GITHUB_REPO_API)
                .append(pp.user)
                .append(SLASH)
                .append(pp.repo)
                .toString();
    }

    public boolean isValid(PitchParams pp, String apiPath) {

        if(apiPath != null) {
            GRS grs = grsManager.get(pp.grs);
            return apiPath.startsWith(grs.apiBase()) ||
                    apiPath.startsWith(grs.rawBase());
        } else {
            return false;
        }
    }

    private static final String SLASH = "/";
    private static final String GITHUB_REPO_API = "repo/";
}
