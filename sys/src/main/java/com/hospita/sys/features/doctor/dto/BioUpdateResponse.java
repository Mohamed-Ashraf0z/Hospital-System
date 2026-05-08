package com.hospita.sys.features.doctor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BioUpdateResponse {
    private String oldBio;
    private String newBio;
    private boolean updated;

    public BioUpdateResponse(String oldBio, String newBio, boolean updated) {
        this.oldBio = oldBio;
        this.newBio = newBio;
        this.updated = updated;
    }
}
