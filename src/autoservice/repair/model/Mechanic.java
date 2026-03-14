package autoservice.repair.model;

import java.math.BigDecimal;

public class Mechanic extends Person {

    private String specialization;
    private final int yearsOfExperience;
    private BigDecimal hourlyRate;

    public Mechanic(String name, String phone, String specialization, int yearsOfExperience, BigDecimal hourlyRate) {
        super(name, phone);
        this.specialization = specialization;
        this.yearsOfExperience = yearsOfExperience;
        this.hourlyRate = hourlyRate;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

}
