/**
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package dev.snowdrop.release.services;

import java.net.URI;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import io.quarkus.arc.DefaultBean;
import picocli.CommandLine;

/**
 * @author <a href="claprun@redhat.com">Christophe Laprun</a>
 */
@ApplicationScoped
public class JiraClientConfiguration {
    @Produces
    @DefaultBean
    @ApplicationScoped
    public JiraRestClient client(CommandLine.ParseResult parseResult) {
        JiraRestClient jiraRestClient;
        // url is optional but with a default value, getting it from the command spec gets the resolved default value
        //if no value was provided. See: https://github.com/remkop/picocli/issues/1148#issuecomment-675753434
        final var jiraServerUri = parseResult.commandSpec().findOption("url").getValue().toString();
        final var pat = parseResult.commandSpec().findOption('t').getValue().toString();
        AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
        if (pat != null) {
            JiraBearerHttpAuthenticationHandler handler = new JiraBearerHttpAuthenticationHandler(pat);
            jiraRestClient = factory.createWithAuthenticationHandler(URI.create(jiraServerUri), handler);
        } else {
            final var user = parseResult.commandSpec().findOption('u').getValue().toString();
            final var password = parseResult.commandSpec().findOption('p').getValue().toString();
            jiraRestClient = factory.createWithBasicHttpAuthentication(URI.create(jiraServerUri), user, password);
        }
        return jiraRestClient;
    }
}
