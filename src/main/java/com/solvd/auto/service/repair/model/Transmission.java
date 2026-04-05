package com.solvd.auto.service.repair.model;

import com.solvd.auto.service.repair.enums.TransmissionType;

/**
 * Automatic / Manual / Semi-Automatic
 */
public record Transmission(TransmissionType type, int gears) {

}

