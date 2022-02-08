package com.assignment.helpers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@Builder
public final class ActionResponse {

    private String message;
    private Integer resourceId;

    public ActionResponse(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public ActionResponse(String message) {
        this.message = message;
    }

}
