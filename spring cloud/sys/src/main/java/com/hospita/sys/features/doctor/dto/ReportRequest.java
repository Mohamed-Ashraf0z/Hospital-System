package com.hospita.sys.features.doctor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequest {
    /** The AppointedPatient id chosen from the dropdown list */
    private Long appointedPatientId;
    private String reportContent;
}
