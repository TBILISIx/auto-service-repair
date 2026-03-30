package autoservice.repair.model;

import autoservice.repair.enums.TransmissionType;

/**
 * @param type Automatic / Manual / Semi-Automatic
 */
public record Transmission(TransmissionType type, int gears) {

}

