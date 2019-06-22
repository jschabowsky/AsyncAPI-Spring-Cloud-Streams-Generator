
package com.asyncapi.parser;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({

})
public class Channels {


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Channels.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Channels) == false) {
            return false;
        }
        Channels rhs = ((Channels) other);
        return true;
    }
    private Map<String, Message> messages = new HashMap<String, Message>();

    @JsonAnySetter
    public void setDynamicProperty(String name, Message msg) {
        messages.put(name, msg);
    }

    public Map<String, Message> getMessages() {
        return messages;
    }
}
