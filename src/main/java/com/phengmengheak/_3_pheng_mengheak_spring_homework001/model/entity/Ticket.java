package com.phengmengheak._3_pheng_mengheak_spring_homework001.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private Long ticketId;
    private String passengerName;
    private String travelDate;
    private String sourceStation;
    private String destinationStation;
    private double price;
    private Boolean paymentStatus;
    private TicketStatus ticketStatus;
    private String seatNumber;
}
