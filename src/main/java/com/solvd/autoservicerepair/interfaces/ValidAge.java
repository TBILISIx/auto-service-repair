package com.solvd.autoservicerepair.interfaces;

import com.solvd.autoservicerepair.exceptions.AgeException;
import com.solvd.autoservicerepair.model.Customer;

public interface ValidAge {

    void validateAge(Customer customer) throws AgeException;

}
