package com.hospita.sys.features.patient.Dto;

import lombok.Getter;

// @Entity
@Getter
public class CancelAppointmentDto {
    private Long slotid;
    private Long patientid;

    public Long getSlotid() { return slotid; }
    public void setSlotid(Long slotid) { this.slotid = slotid; }
    public Long getPatientid() { return patientid; }
    public void setPatientid(Long patientid) { this.patientid = patientid; }
}
