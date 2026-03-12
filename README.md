# Hello this is Read me File for my project

------------------------------------------------------------------------

# Auto Service Repair -- Java Project

This is a simple Java console program that simulates a small car repair
garage.

The program can store information about: - customers - mechanics -
vehicles - repair services - appointments - payments and invoices

The goal of this project is to practice Java Object Oriented Programming
(OOP).

------------------------------------------------------------------------

# Project Structure

  
    ├── Main.java
    └── autoservice/repair/
        ├── model/
        │   ├── Transmission.java
        │   ├── Car.java
        │   ├── Motorcycle.java
        │   ├── Truck.java
        │   ├── Insurance.java
        │   ├── Customer.java
        │   ├── Mechanic.java
        │   ├── MechanicShift.java
        │   └── Garage.java
        └── services/
            ├── Service.java
            ├── OilChange.java
            ├── TireChange.java
            ├── BrakeRepair.java
            ├── SparePart.java
            ├── RepairOrder.java
            ├── Appointment.java
            ├── Payment.java
            ├── Invoice.java
            └── BookingService.java

The project has two packages:

model -- classes that store data (cars, customers, mechanics and so on.)

services -- classes that perform actions (repairs, orders, payments)

------------------------------------------------------------------------

# Model Package

These classes represent the main objects in the system.

### Transmission

Stores transmission type (manual or automatic) and number of gears.

### Car

Stores: - brand - model - number of doors - tires - engine type - engine
size - transmission

### Motorcycle

Represents a motorcycle.

Stores: - brand - model - engine capacity - motorcycle type

### Truck

Represents a truck.

Stores: - brand and model - engine size - payload capacity - number of
tires - sleeping cabin - transmission

### Insurance

Stores customer insurance information like: - provider name - policy
number - expiry date - monthly cost

It can also check if the insurance is expired.

### Customer

Represents a garage customer.

Stores: - name - age - phone number - insurance - loyalty points

Customers receive loyalty points when they use the garage service.

### Mechanic

Represents a mechanic working in the garage.

Stores: - name - phone - specialization - years of experience - hourly
rate

### MechanicShift

Represents a mechanic working shift.

Stores: - start time - end time - assigned repair orders

### Garage

Represents the whole garage.

This is the root class, that is fully populated.

Stores: - garage name - address - service bays - mechanics - customers -
vehicles - repair orders

It also checks if a service bay is free.

------------------------------------------------------------------------

# Services Package


### Service

Base class for all services.

### OilChange

Represents an oil change service.

### TireChange

Represents a tire change service.

### BrakeRepair

Represents brake repair service.

### SparePart

Represents spare parts used in repairs.

Stores: - part name - part number - price - quantity

### RepairOrder

Represents a repair job.

Connects: - customer - mechanic - vehicle - service

### Appointment

Represents a scheduled visit to the garage.

Appointment can be: - scheduled - in progress - completed - cancelled

### Payment

Stores payment information.

Includes: - amount - payment method - payment time

### Invoice

Represents the bill for a repair.

Calculates total cost and connects payment with the repair order.

### BookingService

Main class that handles repair orders.

It checks if the garage has free space, creates orders, and processes
payments.

------------------------------------------------------------------------

# Main Class

Main.java is used to run the program and show how everything works.

The program: - creates customers - creates mechanics - creates
vehicles - creates services - creates repair orders - processes payments

------------------------------------------------------------------------

# Concepts Used

This project uses basic Java concepts: - classes and objects -
constructors - inheritance - getters and setters - arrays - simple
validation
