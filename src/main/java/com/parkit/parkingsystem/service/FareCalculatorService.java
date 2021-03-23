package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.time.Duration;
import java.time.LocalDateTime;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        LocalDateTime inHour = ticket.getInTime();
        LocalDateTime outHour = ticket.getOutTime();


        //TODO: Some tests are failing here. Need to check if this logic is correct
        Duration duration = Duration.between(inHour, outHour);
        System.out.println(duration);
        float dMinutes = duration.toMinutes();
        System.out.println(dMinutes);

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                double price = free30Minutes(Fare.CAR_RATE_PER_HOUR, dMinutes);
                System.out.println(price);
                ticket.setPrice(price);
                break;
            }
            case BIKE: {
                double price = free30Minutes(Fare.BIKE_RATE_PER_HOUR, dMinutes);
                ticket.setPrice(price);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }
    }

    private double free30Minutes(double rate, double minutes) {
        if (minutes > 30) {
            System.out.println((minutes - 30) / 60 * rate);
            return ((minutes - 30) / 60 * rate);
        } else {
            return 0;
        }
    }
}