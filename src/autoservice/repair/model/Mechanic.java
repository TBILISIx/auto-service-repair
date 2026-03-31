package autoservice.repair.model;

import autoservice.repair.enums.MechanicSeniorityLevel;

import java.math.BigDecimal;

public class Mechanic extends Person {

    private String specialization;
    private final Integer yearsOfExperience;
    private final MechanicSeniorityLevel level;
    private BigDecimal hourlyRate;

    public Mechanic(String name, String idNumber, String phone, String specialization,
                    Integer yearsOfExperience, MechanicSeniorityLevel level, BigDecimal hourlyRate) {
        super(name, idNumber, phone);
        this.specialization = specialization;
        this.yearsOfExperience = yearsOfExperience;
        this.level = level;
        this.hourlyRate = hourlyRate;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }
    public MechanicSeniorityLevel getLevel() {
        return level;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public String toString() {
        return "Mechanic{" +
                "name='" + getName() + '\'' +
                ", idNumber='" + getIdNumber() + '\'' +
                ", phone='" + getPhone() + '\'' +
                ", specialization='" + specialization + '\'' +
                ", yearsOfExperience=" + yearsOfExperience +
                ", hourlyRate=" + hourlyRate +
                '}';
    }

}
