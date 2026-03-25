package autoservice.repair.model;

import autoservice.repair.exceptions.AgeException;

public interface ValidAge {

    void validateAge(Customer customer) throws AgeException;

}
