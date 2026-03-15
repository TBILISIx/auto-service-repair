package autoservice.repair.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Mechanic extends Person {

    private String specialization;
    private final int yearsOfExperience;
    private BigDecimal hourlyRate;

    public Mechanic(String name, String idNumber, String phone, String specialization, int yearsOfExperience, BigDecimal hourlyRate) {
        super(name, idNumber, phone);
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
