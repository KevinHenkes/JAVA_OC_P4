package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
    /**
     * 3,600,000 for convert milliseconds to minutes on base hourly.
     */
    private final double coef = 60 * 60 * 1000;

    /**
     * Calcul fees with a duration.
     *
     * @param ticket a ticket used
     */
    public void calculateFare(final Ticket ticket) {
        if ((ticket.getOutTime() == null)
                || (ticket.getOutTime().before(ticket.getInTime()))) {
            
            String message = "Out time provided is incorrect: "
                + ticket.getOutTime().toString();
            
            throw new IllegalArgumentException(message);
        }

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();
        // Diff in milliseconds
        double diff = (double) (outHour - inHour);
        double duration = diff / coef;

        switch (ticket.getParkingSpot().getParkingType()) {
        case CAR:
        ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
            break;
        case BIKE:
        ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
            break;
        default:
            throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}
