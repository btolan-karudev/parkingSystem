package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDAOTest {

  private static ParkingService parkingService;

  private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

  @Mock
  private static InputReaderUtil inputReaderUtil;
  @Mock
  private static ParkingSpotDAO parkingSpotDAO;
  @Mock
  private static TicketDAO ticketDAO;

  @BeforeEach
  private void setUpPerTest() {
    parkingSpotDAO = new ParkingSpotDAO();
    parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;

  }

  @Test
  public void testUpdateParking() throws SQLException {

    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    boolean isTrue = parkingSpotDAO.updateParking(parkingSpot);
    assertEquals(isTrue, Boolean.TRUE);

  }

}

