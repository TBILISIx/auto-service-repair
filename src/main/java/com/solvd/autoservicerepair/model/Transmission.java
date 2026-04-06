package com.solvd.autoservicerepair.model;

import com.solvd.autoservicerepair.enums.TransmissionType;

/**
 * Automatic / Manual / Semi-Automatic
 */
public record Transmission(TransmissionType type, int gears) {

}

