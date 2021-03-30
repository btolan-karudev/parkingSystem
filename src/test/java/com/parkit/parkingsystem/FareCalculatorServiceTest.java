package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class FareCalculatorServiceTest {

  private static FareCalculatorService fareCalculatorService;
  private Ticket ticket;

  @BeforeAll
  private static void setUp() {
    fareCalculatorService = new FareCalculatorService();
  }

  @BeforeEach
  private void setUpPerTest() {
    ticket = new Ticket();
  }

  @Test
  public void calculateFareCar() throws Exception {
    LocalDateTime inTime = LocalDateTime.now().minusMinutes(90);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);
    assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
  }

  @Test
  public void calculateFareBike() throws Exception {
    LocalDateTime inTime = LocalDateTime.now().minusMinutes(90);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);
    assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
  }

  @Test
  public void calculateFareUnkownType() {
    LocalDateTime inTime = LocalDateTime.now().minusMinutes(90);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
  }

  @Test
  public void calculateFareBikeWithFutureInTime() {
    LocalDateTime initTime = LocalDateTime.now();
    LocalDateTime inTime = initTime.plus(60, ChronoUnit.HOURS);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
  }

  @Test
  public void calculateFareBikeWithLessThanOneHourParkingTime() throws Exception {
    LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);
    assertEquals((0.25 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
  }

  @Test
  public void calculateFareCarWithLessThanOneHourParkingTime() throws Exception {
    LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);
    assertEquals((0.25 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
  }

  @Test
  public void calculateFareCarWithMoreThanADayParkingTime() throws Exception {
    LocalDateTime inTime = LocalDateTime.now().minusMinutes((60 * 24) + 30);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);
    assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
  }

  @Test
  public void calculateFareCarWithLessThan30MinFreeTime() throws Exception {
    LocalDateTime inTime = LocalDateTime.now().minusMinutes(29);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);
    assertEquals(0, ticket.getPrice());
  }

  @Test
  public void calculateFareCarWith5minParkingTime() throws Exception {
    LocalDateTime inTime = LocalDateTime.now().minusMinutes(5);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);
    assertEquals((0), ticket.getPrice());
  }

  @Test
  public void calculateFareCarWith75MinParkingTime() throws Exception {
    LocalDateTime inTime = LocalDateTime.now().minusMinutes(75);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);
    assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
  }

  @Test
  public void calculateFareBikeWith90MinParkingTimeWithReduction() throws Exception {
    LocalDateTime inTime = LocalDateTime.now().minusMinutes(90);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setVehicleRegNumber("789");
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);
    assertEquals((Fare.CAR_RATE_PER_HOUR * 0.95), ticket.getPrice());
  }


}
