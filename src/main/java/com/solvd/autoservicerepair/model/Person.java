package com.solvd.autoservicerepair.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString

public class Person {

    private final String name;
    private final String idNumber;
    private final String phone;

}
