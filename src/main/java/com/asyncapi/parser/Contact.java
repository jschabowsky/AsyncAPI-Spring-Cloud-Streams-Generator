
package com.asyncapi.parser;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Contact information for the owners of the API.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "url",
    "email"
})
public class Contact {

    /**
     * The identifying name of the contact person/organization.
     * 
     */
    @JsonProperty("name")
    @JsonPropertyDescription("The identifying name of the contact person/organization.")
    private String name;
    /**
     * The URL pointing to the contact information.
     * 
     */
    @JsonProperty("url")
    @JsonPropertyDescription("The URL pointing to the contact information.")
    private URI url;
    /**
     * The email address of the contact person/organization.
     * 
     */
    @JsonProperty("email")
    @JsonPropertyDescription("The email address of the contact person/organization.")
    private String email;

    /**
     * The identifying name of the contact person/organization.
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * The identifying name of the contact person/organization.
     * 
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The URL pointing to the contact information.
     * 
     */
    @JsonProperty("url")
    public URI getUrl() {
        return url;
    }

    /**
     * The URL pointing to the contact information.
     * 
     */
    @JsonProperty("url")
    public void setUrl(URI url) {
        this.url = url;
    }

    /**
     * The email address of the contact person/organization.
     * 
     */
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    /**
     * The email address of the contact person/organization.
     * 
     */
    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Contact.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("url");
        sb.append('=');
        sb.append(((this.url == null)?"<null>":this.url));
        sb.append(',');
        sb.append("email");
        sb.append('=');
        sb.append(((this.email == null)?"<null>":this.email));
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
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.url == null)? 0 :this.url.hashCode()));
        result = ((result* 31)+((this.email == null)? 0 :this.email.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Contact) == false) {
            return false;
        }
        Contact rhs = ((Contact) other);
        return ((((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name)))&&((this.url == rhs.url)||((this.url!= null)&&this.url.equals(rhs.url))))&&((this.email == rhs.email)||((this.email!= null)&&this.email.equals(rhs.email))));
    }

}
