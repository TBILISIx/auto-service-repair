package autoservice.repair.interfaces;

import autoservice.repair.exceptions.AgeException;
import autoservice.repair.model.Customer;

public interface ValidAge {

    void validateAge(Customer customer) throws AgeException;

}
