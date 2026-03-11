package com.phengmengheak._3_pheng_mengheak_spring_homework001.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkTicketUpdateRequest {
    private List<Long> ticketId;
    private Boolean paymentStatus;
}
