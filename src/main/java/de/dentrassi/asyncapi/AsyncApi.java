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

package de.dentrassi.asyncapi;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;

import com.asyncapi.parser.Channel;
import com.asyncapi.parser.Channels;
import com.asyncapi.parser.Info;
import com.asyncapi.parser.Server;

import de.dentrassi.asyncapi.internal.parser.ParserException;
import de.dentrassi.asyncapi.internal.parser.YamlParser;
import de.dentrassi.asyncapi.type.Type;
import de.dentrassi.asyncapi.validate.ValidationException;
import de.dentrassi.asyncapi.validate.Validator;

public class AsyncApi {

    public static final String VERSION = "2.0.0-rc1";

    private Info info;

    private String id; 
    
    private Set<Server> servers = new LinkedHashSet<>();

    private Set<Channel> channels = new LinkedHashSet<>();

    private Set<com.asyncapi.parser.Message> messages = new LinkedHashSet<>();

    private Set<Type> types = new LinkedHashSet<>();

    

    /**
     * Load an AsyncAPI specification encoded in YAML
     *
     * @param in
     *            the stream to read from
     * @return the model
     * @throws ParserException
     *             in case the document cannot be parsed
     */
    public static AsyncApi parseYaml(final InputStream in) throws ParserException {
        return validate(new YamlParser(in).parse());
    }

    /**
     * Load an AsyncAPI specification encoded in YAML
     *
     * @param reader
     *            the reader to read from
     * @return the model
     * @throws ParserException
     *             in case the document cannot be parsed
     */
    public static AsyncApi parseYaml(final Reader reader) throws ParserException {
        return validate(new YamlParser(reader).parse());
    }

    /**
     * Load an AsyncAPI specification encoded in YAML
     *
     * @param path
     *            the file system resource to read from
     * @return the model
     * @throws ParserException
     *             in case the document cannot be parsed
     */
    public static AsyncApi parseYaml(final Path path) throws ParserException {
        try (final InputStream in = Files.newInputStream(path)) {
            return validate(new YamlParser(in).parse());
        } catch (final IOException e) {
            throw new ParserException("Failed to read file", e);
        }
    }

    public static AsyncApi validate(final AsyncApi api) {

        final Validator validator = new Validator();
        validator.validate(api);

        if (validator.hasErrors()) {
            throw new ValidationException(validator.getMarkers());
        }

        return api;
    }

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<Server> getServers() {
		return servers;
	}

	public void setServers(Set<Server> servers) {
		this.servers = servers;
	}

	public Set<Channel> getChannel() {
		return channels;
	}

	public void setChannel(Set<Channel> channel) {
		channels = channel;
	}

	public Set<com.asyncapi.parser.Message> getMessages() {
		return messages;
	}

	public void setMessages(Set<com.asyncapi.parser.Message> messages) {
		this.messages = messages;
	}

	public Set<Type> getTypes() {
		return types;
	}

	public void setTypes(Set<Type> types) {
		this.types = types;
	}

	public static String getVersion() {
		return VERSION;
	}
}
