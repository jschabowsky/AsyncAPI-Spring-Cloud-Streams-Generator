/*
 * Copyright (C) 2017 Jens Reimann <jreimann@redhat.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.dentrassi.asyncapi.generator.java;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.asyncapi.parser.Channel;

import de.dentrassi.asyncapi.AsyncApi;
import de.dentrassi.asyncapi.Topic;
import de.dentrassi.asyncapi.generator.java.util.Version;

public class ServiceDefinitions {

    public static class VersionedService {
        private final TypeInformation type;
        private final Version version;

        public VersionedService(final TypeInformation type, final Version version) {
            this.type = type;
            this.version = version;
        }

        public TypeInformation getType() {
            return this.type;
        }

        public Version getVersion() {
            return this.version;
        }
    }

    private final Map<Channel, TopicInformation> topics;
    private final Map<String, Map<String, List<Channel>>> versions;
    private final Map<String, VersionedService> latest;

    public ServiceDefinitions(final Map<Channel, TopicInformation> topics, final Map<String, Map<String, List<Channel>>> versions, final Map<String, VersionedService> latest) {
        this.topics = Collections.unmodifiableMap(topics);
        this.versions = Collections.unmodifiableMap(versions); // FIXME: contained maps are still mutable
        this.latest = Collections.unmodifiableMap(latest);
    }

    public Map<Channel, TopicInformation> getTopics() {
        return this.topics;
    }

    public Map<String, Map<String, List<Channel>>> getVersions() {
        return this.versions;
    }

    public Map<String, VersionedService> getLatest() {
        return this.latest;
    }

    public static ServiceDefinitions build(final AsyncApi api, final boolean validateTopicSyntax) {

        final Map<Channel, TopicInformation> topics = new LinkedHashMap<>();
        final Map<String, Map<String, List<Channel>>> versions = new HashMap<>();

        for (final Channel channel : api.getChannel()) {

            TopicInformation ti;
            try {
                ti = TopicInformation.fromString(channel.getChannel());
            } catch (final IllegalArgumentException e) {
                if (validateTopicSyntax) {
                    throw e;
                }
                // fall back to default
                ti = new TopicInformation("Topics", "1", "event", new LinkedList<>(asList(channel.getChannel().split("\\/"))), "send", empty());
            }

            addTopic(versions, ti, channel);
            topics.put(channel, ti);
        }

        final Map<String, VersionedService> latest = new HashMap<>();
        for (final Map.Entry<String, Map<String, List<Channel>>> versionEntry : versions.entrySet()) {
            for (final Map.Entry<String, List<Channel>> serviceEntry : versionEntry.getValue().entrySet()) {

                final TypeInformation serviceType = Generator.createServiceTypeInformation(serviceEntry);

                // record latest version

                final Version v = Version.valueOf(versionEntry.getKey());
                final VersionedService lv = latest.get(serviceType.getName());
                if (lv == null || v.compareTo(lv.getVersion()) > 0) {
                    latest.put(serviceType.getName(), new VersionedService(serviceType, v));
                }

            }
        }

        return new ServiceDefinitions(topics, versions, latest);

    }

    private static void addTopic(final Map<String, Map<String, List<Channel>>> versions, final TopicInformation ti, final Channel topic) {

        Map<String, List<Channel>> version = versions.get(ti.getVersion());
        if (version == null) {
            version = new HashMap<>();
            versions.put(ti.getVersion(), version);
        }

        List<Channel> service = version.get(ti.getService());
        if (service == null) {
            service = new LinkedList<>();
            version.put(ti.getService(), service);
        }

        service.add(topic);
    }
}
