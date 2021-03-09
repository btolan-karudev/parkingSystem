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
                if (dMinutes > 30) {
                    ticket.setPrice((dMinutes - 30) / 60 * Fare.CAR_RATE_PER_HOUR);
                } else {
                    ticket.setPrice(0.0);
                    System.out.println("GRATOS GRATOS");
                }
                break;
            }
            case BIKE: {
                ticket.setPrice(dMinutes * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}