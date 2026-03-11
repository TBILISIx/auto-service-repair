package autoservice.repair.model;

import java.math.BigDecimal;

public class Mechanic {

    private String name;
    private String phone;
    private String specialization;
    private final int yearsOfExperience;
    private BigDecimal hourlyRate;

    public Mechanic(String name, String phone, String specialization, int yearsOfExperience, BigDecimal hourlyRate) {
        this.name = name;
        this.phone = phone;
        this.specialization = specialization;
        this.yearsOfExperience = yearsOfExperience;
        this.hourlyRate = hourlyRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
