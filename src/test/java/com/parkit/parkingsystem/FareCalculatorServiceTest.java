package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

public class FareCalculatorServiceTest {
    /**
     * A fare calculator service.
     */
    private static FareCalculatorService fareCalculatorService;
    /**
     * A ticket.
     */
    private Ticket ticket;
    /**
     * An hour in milliseconds.
     */
    private final int hourInMs = 60 * 60 * 1000;
    /**
     * Three quarters in milliseconds.
     */
    private final int threeQuartersInMs = 45 * 60 * 1000;
    /**
     * A day in milliseconds.
     */
    private final int dayInMs = 24 * 60 * 60 * 1000;
    /**
     * Three quarters in base hourly.
     */
    private final float threeQuartersInBaseHourly = 0.75f;
    /**
     * A day in base hourly.
     */
    private final int dayInBaseHourly = 24;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    /**
     * Calculate fare car.
     */
    @Test
    public void calculateFareCar() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - hourInMs);
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    /**
     * Calculate fare bike.
     */
    @Test
    public void calculateFareBike() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - hourInMs);
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    /**
     * Calculate fare for unknown type.
     */
    @Test
    public void calculateFareUnkownType() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - hourInMs);
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class,
                () -> fareCalculatorService.calculateFare(ticket));
    }

    /**
     * Calculate fare for bike if in time is in the future.
     */
    @Test
    public void calculateFareBikeWithFutureInTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() + hourInMs);
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class,
                () -> fareCalculatorService.calculateFare(ticket));
    }

    /**
     * Calculate fare for bike with less than an hour of parking.
     */
    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime() {
        Date inTime = new Date();
        // 45 minutes parking time should give 3/4th parking fare
        inTime.setTime(System.currentTimeMillis() - threeQuartersInMs);
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((threeQuartersInBaseHourly * Fare.BIKE_RATE_PER_HOUR),
                ticket.getPrice());
    }

    /**
     * Calculate fare for car with less than one hour of parking.
     */
    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime() {
        Date inTime = new Date();
        // 45 minutes parking time should give 3/4th parking fare
        inTime.setTime(System.currentTimeMillis() - threeQuartersInMs);
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((threeQuartersInBaseHourly * Fare.CAR_RATE_PER_HOUR),
                ticket.getPrice());
    }

    /**
     * Calculate fare for car with more than a day of parking.
     */
    @Test
    public void calculateFareCarWithMoreThanADayParkingTime() {
        Date inTime = new Date();
        // 24 hours parking time should give 24 * parking fare per hour
        inTime.setTime(System.currentTimeMillis() - dayInMs);
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((dayInBaseHourly * Fare.CAR_RATE_PER_HOUR),
                ticket.getPrice());
    }

}
