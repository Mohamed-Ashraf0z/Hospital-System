package com.hospita.sys.features.patient.Dto;

import lombok.Getter;

// @Entity
@Getter
public class CancelAppointmentDto {
    private Long slotid;
    private Long patientid;
}
