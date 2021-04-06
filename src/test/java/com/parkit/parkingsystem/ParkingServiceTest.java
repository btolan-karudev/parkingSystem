package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

  private static ParkingService parkingService;

  @Mock
  private static InputReaderUtil inputReaderUtil;
  @Mock
  private static ParkingSpotDAO parkingSpotDAO;
  @Mock
  private static TicketDAO ticketDAO;

  @BeforeEach
  private void setUpPerTest() {
    parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
  }

  @Test
  public void processExitingVehicleTest() throws Exception {
    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    Ticket ticket = new Ticket();
    ticket.setInTime(LocalDateTime.now().minusHours(60));
    ticket.setParkingSpot(parkingSpot);
    ticket.setVehicleRegNumber("ABCDEF");
    when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
    when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
    when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
    parkingService.processExitingVehicle();
    verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
  }

  @Test
  public void getNextParkingNumberAvailable() {
    when(inputReaderUtil.readSelection()).thenReturn(1);
    when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
    ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();
    assertNotNull(parkingSpot);
  }

}
