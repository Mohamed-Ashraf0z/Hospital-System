package com.hospita.sys.features.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DoctorVerificationDecisionDto {

    @JsonProperty("isAccepted")
    private boolean isAccepted;

    public boolean getIsAccepted() {
        return isAccepted;
    }

    @JsonProperty("isAccepted")
    public void setIsAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }
}
