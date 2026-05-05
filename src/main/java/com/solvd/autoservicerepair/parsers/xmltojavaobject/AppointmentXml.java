package com.solvd.autoservicerepair.parsers.xmltojavaobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
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

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentXml {

    @JsonProperty("id")
    private int id;

    @JsonProperty("scheduledTime")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime scheduledTime;

    @JsonProperty("status")
    private String status;

    @JsonProperty("customerRef")
    private String customerRef;

    @JsonProperty("mechanicRef")
    private String mechanicRef;

    @JsonProperty("vehicleRef")
    private String vehicleRef;
}