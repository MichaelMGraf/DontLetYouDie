package de.dontletyoudie.frontendapp.data.apiCalls.core;

import com.fasterxml.jackson.annotation.JsonProperty;

class ErrorMessage {
    @JsonProperty("error")
    String errorMessage;
}
