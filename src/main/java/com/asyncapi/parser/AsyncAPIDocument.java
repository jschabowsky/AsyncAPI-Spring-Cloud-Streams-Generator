
package com.asyncapi.parser;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


/**
 * AsyncAPI 2.0.0-rc1 schema.
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "asyncapi",
    "id",
    "info",
    "servers",
    "defaultContentType",
    "channels",
    "components",
    "tags",
    "externalDocs"
})
public class AsyncAPIDocument {

    /**
     * The AsyncAPI specification version of this document.
     * (Required)
     * 
     */
    @JsonProperty("asyncapi")
    @JsonPropertyDescription("The AsyncAPI specification version of this document.")
    private String asyncapi;
    /**
     * A unique id representing the application.
     * (Required)
     * 
     */
    @JsonProperty("id")
    @JsonPropertyDescription("A unique id representing the application.")
    private URI id;
    /**
     * General information about the API.
     * (Required)
     * 
     */
    @JsonProperty("info")
    @JsonPropertyDescription("General information about the API.")
    private Info info;
    @JsonProperty("servers")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<Server> servers = new LinkedHashSet<Server>();
    @JsonProperty("defaultContentType")
    private String defaultContentType;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("channels")
    private Channels channels;
    /**
     * An object to hold a set of reusable objects for different aspects of the AsyncAPI Specification.
     * 
     */
    @JsonProperty("components")
    @JsonPropertyDescription("An object to hold a set of reusable objects for different aspects of the AsyncAPI Specification.")
    private Components components;
    @JsonProperty("tags")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<Tag> tags = new LinkedHashSet<Tag>();
    /**
     * information about external documentation
     * 
     */
    @JsonProperty("externalDocs")
    @JsonPropertyDescription("information about external documentation")
    private ExternalDocs externalDocs;

    /**
     * The AsyncAPI specification version of this document.
     * (Required)
     * 
     */
    @JsonProperty("asyncapi")
    public String getAsyncapi() {
        return asyncapi;
    }

    /**
     * The AsyncAPI specification version of this document.
     * (Required)
     * 
     */
    @JsonProperty("asyncapi")
    public void setAsyncapi(String asyncapi) {
        this.asyncapi = asyncapi;
    }

    /**
     * A unique id representing the application.
     * (Required)
     * 
     */
    @JsonProperty("id")
    public URI getId() {
        return id;
    }

    /**
     * A unique id representing the application.
     * (Required)
     * 
     */
    @JsonProperty("id")
    public void setId(URI id) {
        this.id = id;
    }

    /**
     * General information about the API.
     * (Required)
     * 
     */
    @JsonProperty("info")
    public Info getInfo() {
        return info;
    }

    /**
     * General information about the API.
     * (Required)
     * 
     */
    @JsonProperty("info")
    public void setInfo(Info info) {
        this.info = info;
    }

    @JsonProperty("servers")
    public Set<Server> getServers() {
        return servers;
    }

    @JsonProperty("servers")
    public void setServers(Set<Server> servers) {
        this.servers = servers;
    }

    @JsonProperty("defaultContentType")
    public String getDefaultContentType() {
        return defaultContentType;
    }

    @JsonProperty("defaultContentType")
    public void setDefaultContentType(String defaultContentType) {
        this.defaultContentType = defaultContentType;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("channels")
    public Channels getChannels() {
        return channels;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("channels")
    public void setChannels(Channels channels) {
        this.channels = channels;
    }

    /**
     * An object to hold a set of reusable objects for different aspects of the AsyncAPI Specification.
     * 
     */
    @JsonProperty("components")
    public Components getComponents() {
        return components;
    }

    /**
     * An object to hold a set of reusable objects for different aspects of the AsyncAPI Specification.
     * 
     */
    @JsonProperty("components")
    public void setComponents(Components components) {
        this.components = components;
    }

    @JsonProperty("tags")
    public Set<Tag> getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    /**
     * information about external documentation
     * 
     */
    @JsonProperty("externalDocs")
    public ExternalDocs getExternalDocs() {
        return externalDocs;
    }

    /**
     * information about external documentation
     * 
     */
    @JsonProperty("externalDocs")
    public void setExternalDocs(ExternalDocs externalDocs) {
        this.externalDocs = externalDocs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(AsyncAPIDocument.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("asyncapi");
        sb.append('=');
        sb.append(((this.asyncapi == null)?"<null>":this.asyncapi));
        sb.append(',');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("info");
        sb.append('=');
        sb.append(((this.info == null)?"<null>":this.info));
        sb.append(',');
        sb.append("servers");
        sb.append('=');
        sb.append(((this.servers == null)?"<null>":this.servers));
        sb.append(',');
        sb.append("defaultContentType");
        sb.append('=');
        sb.append(((this.defaultContentType == null)?"<null>":this.defaultContentType));
        sb.append(',');
        sb.append("channels");
        sb.append('=');
        sb.append(((this.channels == null)?"<null>":this.channels));
        sb.append(',');
        sb.append("components");
        sb.append('=');
        sb.append(((this.components == null)?"<null>":this.components));
        sb.append(',');
        sb.append("tags");
        sb.append('=');
        sb.append(((this.tags == null)?"<null>":this.tags));
        sb.append(',');
        sb.append("externalDocs");
        sb.append('=');
        sb.append(((this.externalDocs == null)?"<null>":this.externalDocs));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.components == null)? 0 :this.components.hashCode()));
        result = ((result* 31)+((this.servers == null)? 0 :this.servers.hashCode()));
        result = ((result* 31)+((this.channels == null)? 0 :this.channels.hashCode()));
        result = ((result* 31)+((this.asyncapi == null)? 0 :this.asyncapi.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.externalDocs == null)? 0 :this.externalDocs.hashCode()));
        result = ((result* 31)+((this.info == null)? 0 :this.info.hashCode()));
        result = ((result* 31)+((this.defaultContentType == null)? 0 :this.defaultContentType.hashCode()));
        result = ((result* 31)+((this.tags == null)? 0 :this.tags.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AsyncAPIDocument) == false) {
            return false;
        }
        AsyncAPIDocument rhs = ((AsyncAPIDocument) other);
        return ((((((((((this.components == rhs.components)||((this.components!= null)&&this.components.equals(rhs.components)))&&((this.servers == rhs.servers)||((this.servers!= null)&&this.servers.equals(rhs.servers))))&&((this.channels == rhs.channels)||((this.channels!= null)&&this.channels.equals(rhs.channels))))&&((this.asyncapi == rhs.asyncapi)||((this.asyncapi!= null)&&this.asyncapi.equals(rhs.asyncapi))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.externalDocs == rhs.externalDocs)||((this.externalDocs!= null)&&this.externalDocs.equals(rhs.externalDocs))))&&((this.info == rhs.info)||((this.info!= null)&&this.info.equals(rhs.info))))&&((this.defaultContentType == rhs.defaultContentType)||((this.defaultContentType!= null)&&this.defaultContentType.equals(rhs.defaultContentType))))&&((this.tags == rhs.tags)||((this.tags!= null)&&this.tags.equals(rhs.tags))));
    }

    public String Asyncapi; 

}
