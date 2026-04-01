package autoservice.repair.model;

import autoservice.repair.enums.TransmissionType;

/**
 *  Automatic / Manual / Semi-Automatic
 */
public record Transmission(TransmissionType type, int gears) {

}

