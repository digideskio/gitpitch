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
package com.gitpitch.git.vendors;

import com.gitpitch.git.*;
import com.gitpitch.models.*;
import com.gitpitch.services.DiskService;
import com.gitpitch.utils.PitchParams;
import com.fasterxml.jackson.databind.JsonNode;
import java.nio.file.Path;
import javax.inject.*;
import play.Logger;
import play.Logger.ALogger;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * GitLab API Service.
 */
@Singleton
public class GitLab implements GRSService {

    private final Logger.ALogger log = Logger.of(this.getClass());

    private final AtomicInteger cacheBypass = new AtomicInteger();
    private GRSManager grsManager;
    private DiskService diskService;

    public void init(GRSManager grsManager,
                     DiskService diskService) {
        this.grsManager = grsManager;
        this.diskService = diskService;
    }

    public boolean call(PitchParams pp, String apiPath) {

        if(apiPath != null) {
            GRS grs = grsManager.get(pp);
            return apiPath.startsWith(grs.apiBase()) ||
                    apiPath.startsWith(grs.rawBase());
        } else {
            return false;
        }
    }

    public int download(PitchParams pp, String filename) {

        int status = 999;

        GRS grs = grsManager.get(pp);
        GRSService grsService = grsManager.getService(grs);
        Path branchPath = diskService.ensure(pp);
        String gitLabLink = raw(pp, filename, true);
        log.debug("download: gitLabLink={}", gitLabLink);

        if (grsService.call(pp, gitLabLink)) {
            status = diskService.download(pp,
                                          branchPath,
                                          gitLabLink,
                                          filename,
                                          grs.headers());
        }

        log.debug("download: returning status={}", status);
        return status;
    }

    public GitRepoModel model(PitchParams pp, JsonNode json) {
        return GitLabRepoModel.build(pp, json);
    }

    public String raw(PitchParams pp) {

        GRS grs = grsManager.get(pp);

        return new StringBuffer(grs.rawBase())
                .append(pp.user)
                .append(SLASH)
                .append(pp.repo)
                .append(GITLAB_RAW)
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

        GRS grs = grsManager.get(pp);

        return new StringBuffer(grs.apiBase())
                .append(GITLAB_PROJECTS_API)
                .append(genProjectId(pp))
                .toString();
    }

    private String genProjectId(PitchParams pp) {

        String userRepo =
            new StringBuffer(pp.user).append(SLASH)
                                     .append(pp.repo)
                                     .toString();

        String pid = java.net.URLEncoder.encode(userRepo);
        log.debug("genProjectId: pid={}", pid);
        return pid;
    }

    public static final String TYPE = "gitlab";

    private static final String SLASH = "/";
    private static final String GITLAB_PROJECTS_API = "projects/";
    private static final String GITLAB_RAW = "/raw/";

}
