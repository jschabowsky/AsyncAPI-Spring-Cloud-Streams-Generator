
package com.asyncapi.parser;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * General information about the API.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "title",
    "version",
    "description",
    "termsOfService",
    "contact",
    "license"
})
public class Info {

    /**
     * A unique and precise title of the API.
     * (Required)
     * 
     */
    @JsonProperty("title")
    @JsonPropertyDescription("A unique and precise title of the API.")
    private String title;
    /**
     * A semantic version number of the API.
     * (Required)
     * 
     */
    @JsonProperty("version")
    @JsonPropertyDescription("A semantic version number of the API.")
    private String version;
    /**
     * A longer description of the API. Should be different from the title. CommonMark is allowed.
     * 
     */
    @JsonProperty("description")
    @JsonPropertyDescription("A longer description of the API. Should be different from the title. CommonMark is allowed.")
    private String description;
    /**
     * A URL to the Terms of Service for the API. MUST be in the format of a URL.
     * 
     */
    @JsonProperty("termsOfService")
    @JsonPropertyDescription("A URL to the Terms of Service for the API. MUST be in the format of a URL.")
    private URI termsOfService;
    /**
     * Contact information for the owners of the API.
     * 
     */
    @JsonProperty("contact")
    @JsonPropertyDescription("Contact information for the owners of the API.")
    private Contact contact;
    @JsonProperty("license")
    private License license;

    /**
     * A unique and precise title of the API.
     * (Required)
     * 
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * A unique and precise title of the API.
     * (Required)
     * 
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * A semantic version number of the API.
     * (Required)
     * 
     */
    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    /**
     * A semantic version number of the API.
     * (Required)
     * 
     */
    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * A longer description of the API. Should be different from the title. CommonMark is allowed.
     * 
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * A longer description of the API. Should be different from the title. CommonMark is allowed.
     * 
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * A URL to the Terms of Service for the API. MUST be in the format of a URL.
     * 
     */
    @JsonProperty("termsOfService")
    public URI getTermsOfService() {
        return termsOfService;
    }

    /**
     * A URL to the Terms of Service for the API. MUST be in the format of a URL.
     * 
     */
    @JsonProperty("termsOfService")
    public void setTermsOfService(URI termsOfService) {
        this.termsOfService = termsOfService;
    }

    /**
     * Contact information for the owners of the API.
     * 
     */
    @JsonProperty("contact")
    public Contact getContact() {
        return contact;
    }

    /**
     * Contact information for the owners of the API.
     * 
     */
    @JsonProperty("contact")
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @JsonProperty("license")
    public License getLicense() {
        return license;
    }

    @JsonProperty("license")
    public void setLicense(License license) {
        this.license = license;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Info.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null)?"<null>":this.title));
        sb.append(',');
        sb.append("version");
        sb.append('=');
        sb.append(((this.version == null)?"<null>":this.version));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null)?"<null>":this.description));
        sb.append(',');
        sb.append("termsOfService");
        sb.append('=');
        sb.append(((this.termsOfService == null)?"<null>":this.termsOfService));
        sb.append(',');
        sb.append("contact");
        sb.append('=');
        sb.append(((this.contact == null)?"<null>":this.contact));
        sb.append(',');
        sb.append("license");
        sb.append('=');
        sb.append(((this.license == null)?"<null>":this.license));
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
        result = ((result* 31)+((this.license == null)? 0 :this.license.hashCode()));
        result = ((result* 31)+((this.contact == null)? 0 :this.contact.hashCode()));
        result = ((result* 31)+((this.description == null)? 0 :this.description.hashCode()));
        result = ((result* 31)+((this.termsOfService == null)? 0 :this.termsOfService.hashCode()));
        result = ((result* 31)+((this.title == null)? 0 :this.title.hashCode()));
        result = ((result* 31)+((this.version == null)? 0 :this.version.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Info) == false) {
            return false;
        }
        Info rhs = ((Info) other);
        return (((((((this.license == rhs.license)||((this.license!= null)&&this.license.equals(rhs.license)))&&((this.contact == rhs.contact)||((this.contact!= null)&&this.contact.equals(rhs.contact))))&&((this.description == rhs.description)||((this.description!= null)&&this.description.equals(rhs.description))))&&((this.termsOfService == rhs.termsOfService)||((this.termsOfService!= null)&&this.termsOfService.equals(rhs.termsOfService))))&&((this.title == rhs.title)||((this.title!= null)&&this.title.equals(rhs.title))))&&((this.version == rhs.version)||((this.version!= null)&&this.version.equals(rhs.version))));
    }

}
