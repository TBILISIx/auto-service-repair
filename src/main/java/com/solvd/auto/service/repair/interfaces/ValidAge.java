package com.solvd.auto.service.repair.interfaces;

import com.solvd.auto.service.repair.exceptions.AgeException;
import com.solvd.auto.service.repair.model.Customer;

public interface ValidAge {

    void validateAge(Customer customer) throws AgeException;

}
