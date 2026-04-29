package com.solvd.autoservicerepair.parser.xmltojavaobject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Mirrors <appointment>
 */

@Getter
@Setter
@ToString

public class AppointmentXml {

    private int id;
    private LocalDateTime scheduledTime;   // THE LocalDateTime from the assignment
    private String status;          // maps to ServiceStatus enum
    private String customerRef;     // idNumber of the customer
    private String mechanicRef;     // idNumber of the mechanic
    private String vehicleRef;      // vin of the vehicle

}