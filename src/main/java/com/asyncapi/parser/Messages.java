
package com.asyncapi.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * JSON objects describing the messages being consumed and produced by the API.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({

})
public class Messages {


    @Override
    public String toString() {
    	String sb ="";
        for (Map.Entry<String,Message> entry : messages.entrySet())  
            sb+=" " + entry.getKey() + 
                             ", Value = " + entry.getValue(); 
        return sb;
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
        if ((other instanceof Messages) == false) {
            return false;
        }
        Messages rhs = ((Messages) other);
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
