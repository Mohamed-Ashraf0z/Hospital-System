package com.hospita.sys.features.doctor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvailabilityRequest {
    /** e.g. ["MONDAY", "TUESDAY"] */
    private java.util.List<String> days;
    /** "HH:mm" format */
    private String startTime;
    /** "HH:mm" format */
    private String endTime;
    /** Duration of each slot in minutes */
    private int durationMinutes;
}
