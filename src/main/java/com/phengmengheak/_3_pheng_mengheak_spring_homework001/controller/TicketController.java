package com.phengmengheak._3_pheng_mengheak_spring_homework001.controller;

import com.phengmengheak._3_pheng_mengheak_spring_homework001.model.request.BulkTicketUpdateRequest;
import com.phengmengheak._3_pheng_mengheak_spring_homework001.model.entity.Ticket;
import com.phengmengheak._3_pheng_mengheak_spring_homework001.model.request.TicketRequest;
import com.phengmengheak._3_pheng_mengheak_spring_homework001.model.entity.TicketStatus;
import com.phengmengheak._3_pheng_mengheak_spring_homework001.model.response.ResponseApi;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {
    private final AtomicLong ticketId = new AtomicLong(6L);
    LocalDate dateNow = LocalDate.now();
    String date = dateNow.format(DateTimeFormatter.ISO_LOCAL_DATE);
    private final List<Ticket> ticketList = new ArrayList<>(List.of(
            new Ticket(1L,"Pheng Mengheak",date,"Phnom Penh","Kep",24,true, TicketStatus.BOOKED,"A01"),
            new Ticket(2L,"Horn Tola",date,"Siem Reap","Phnomo Penh",4.4,false, TicketStatus.COMPLETED,"Z01"),
            new Ticket(3L,"Lyubov Chika",date,"Kompong thom","Kompong soam",7.25,true, TicketStatus.COMPLETED,"B02"),
            new Ticket(4L,"Sreyneath Soeun",date,"Kompang Chnang","Koh Kong",4.15,false, TicketStatus.CANCELLED,"K02"),
            new Ticket(5L,"Nika Sok",date,"Phnom Penh","Kompang thom",8.34,true, TicketStatus.BOOKED,"B01")
    ));

    @Operation(summary = "Get all tickets")
    @GetMapping
    public ResponseEntity<ResponseApi<List<Ticket>>> getAllTickets(){
        ResponseApi<List<Ticket>> responseApi = new ResponseApi<>(true,"Tickets retrieved successfully",HttpStatus.OK,ticketList, LocalDateTime.now());
        return ResponseEntity.ok(responseApi);
    }

    @Operation(summary = "Get a ticket by ID")
    @GetMapping("/{ticket-id}")
    public ResponseEntity<ResponseApi<Ticket>> getTicketById(@PathVariable("ticket-id") Long ticketId ){
        for(Ticket ticket: ticketList){
            if(ticket.getTicketId().equals(ticketId)){
                ResponseApi<Ticket> responseApi = new ResponseApi<>(true,"Tickets fetched successfully",HttpStatus.OK,ticket, LocalDateTime.now());
                return ResponseEntity.ok(responseApi);
            }
        }
        ResponseApi<Ticket> responseApi = new ResponseApi<>(false,"Ticket not found!",HttpStatus.NOT_FOUND,null, LocalDateTime.now());
        return ResponseEntity.ok(responseApi);
    }

    @Operation(summary = "Create a new Ticket")
    @PostMapping
    public ResponseEntity<?> createTicket(@RequestBody TicketRequest ticketRequest){
        Ticket newTicket = new Ticket(
                ticketId.getAndIncrement(),
                ticketRequest.getPassengerName(),
                ticketRequest.getTravelDate(),
                ticketRequest.getSourceStation(),
                ticketRequest.getDestinationStation(),
                ticketRequest.getPrice(),
                ticketRequest.getPaymentStatus(),
                ticketRequest.getTicketStatus(),
                ticketRequest.getSeatNumber()
        );
        ResponseApi<Ticket> responseApi = new ResponseApi<>(true,"Ticket created successfully",HttpStatus.CREATED,newTicket, LocalDateTime.now());
        ticketList.add(newTicket);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseApi);
    }

    @Operation(summary = "Create multiple new tickets")
    @PostMapping("/bulk")
    public ResponseEntity<ResponseApi<List<Ticket>>> createMultipleTickets(@RequestBody List<TicketRequest> newTicketRequestList){
        List<Ticket> createdTickets = new ArrayList<>();
        for(TicketRequest ticket: newTicketRequestList){
            Ticket newTicket = new Ticket(
                    ticketId.getAndIncrement(),
                    ticket.getPassengerName(),
                    ticket.getTravelDate(),
                    ticket.getSourceStation(),
                    ticket.getDestinationStation(),
                    ticket.getPrice(),
                    ticket.getPaymentStatus(),
                    ticket.getTicketStatus(),
                    ticket.getSeatNumber()
            );
            ticketList.add(newTicket);
            createdTickets.add(newTicket);
        }
        ResponseApi<List<Ticket>> responseApi = new ResponseApi<>(true,"Tickets created successfully",HttpStatus.CREATED,createdTickets, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseApi);
    }

    @Operation(summary = "Delete a ticket using ID")
    @DeleteMapping("/{ticket-id}")
    public ResponseEntity<?> deleteStudentById(@PathVariable("ticket-id") Long ticketId){
        boolean removed = ticketList.removeIf(t -> t.getTicketId().equals(ticketId));
        if(removed){
            ResponseApi<String> succcessResponseApi = new ResponseApi<>(true,"Tickets created successfully",HttpStatus.GONE,null, LocalDateTime.now());
            return ResponseEntity.ok(succcessResponseApi);
        } else{
            ResponseApi<String> notFoundResponseApi = new ResponseApi<>(false,"Ticket not found!",HttpStatus.NOT_FOUND,null,LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponseApi);
        }
    }

    @Operation(summary = "Update a ticket by ID")
    @PutMapping("/{ticket-id}")
    public ResponseEntity<?> updateTicketById(@PathVariable("ticket-id") Long ticketRequestId, @RequestBody TicketRequest ticketRequest){
        for(Ticket ticket: ticketList){
            if(ticket.getTicketId().equals(ticketRequestId)){
                ticket.setPassengerName(ticketRequest.getPassengerName());
                ticket.setTravelDate(ticketRequest.getTravelDate());
                ticket.setSourceStation(ticketRequest.getSourceStation());
                ticket.setDestinationStation(ticketRequest.getDestinationStation());
                ticket.setPrice(ticketRequest.getPrice());
                ticket.setPaymentStatus(ticketRequest.getPaymentStatus());
                ticket.setTicketStatus(ticketRequest.getTicketStatus());
                ticket.setSeatNumber(ticketRequest.getSeatNumber());
                ResponseApi<Ticket> successUpdateApi = new ResponseApi<>(true,"Tickets updated successfully",HttpStatus.ACCEPTED,ticket, LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(successUpdateApi);
            }
        }
        ResponseApi<Ticket> notFoundUpdateApi = new ResponseApi<>(true,"Tickets not found!",HttpStatus.NOT_FOUND,null, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(notFoundUpdateApi);
    }

    @Operation(summary = "Search a ticket by passenger name")
    @GetMapping("/search")
    public ResponseEntity<ResponseApi<List<Ticket>>> searchTickets(@RequestParam String passengerName){
        List<Ticket> ticketListSearch = new ArrayList<>();
        for(Ticket ticket : ticketList){
            if(ticket.getPassengerName().toLowerCase()
                    .contains(passengerName.toLowerCase())){
                ticketListSearch.add(ticket);
            }
        }
        if(ticketListSearch.isEmpty()){
            ResponseApi<List<Ticket>> responseApi = new ResponseApi<>(true,"Ticked not founded!",HttpStatus.NOT_FOUND,null, LocalDateTime.now());
            return ResponseEntity.ok(responseApi);
        }
        ResponseApi<List<Ticket>> responseApi = new ResponseApi<>(true,"Tickets fetched successfully",HttpStatus.OK,ticketListSearch, LocalDateTime.now());
        return ResponseEntity.ok(responseApi);
    }

    @Operation(summary = "Update payment status of multiple tickets")
    @PutMapping("/bulk")
    public ResponseEntity<ResponseApi<List<Ticket>>> bulkUpdateTickets(@RequestBody BulkTicketUpdateRequest ticketPaymentStatus ){
        List<Ticket> ticketListUpdateStatus = new ArrayList<>();
           for(Long id: ticketPaymentStatus.getTicketId()){
               for (Ticket ticket: ticketList){
                   if(ticket.getTicketId().equals(id)){
                       ticket.setPaymentStatus(ticketPaymentStatus.getPaymentStatus());
                       ticketListUpdateStatus.add(ticket);
                   }
               }
           }
        ResponseApi<List<Ticket>> responseApi = new ResponseApi<>(true,"Payment status updated successfully",HttpStatus.ACCEPTED,ticketListUpdateStatus, LocalDateTime.now());
        return ResponseEntity.ok(responseApi);
    }

    @Operation(summary = "Filter tickets by status and travel date")
    @GetMapping("/filter")
    public ResponseEntity<?> filter(@RequestParam TicketStatus ticketStatus, @RequestParam String date){
        List<Ticket> ticketFilters = new ArrayList<>();
        for(Ticket ticket: ticketList){
           if(ticket.getTicketStatus().equals(ticketStatus) && ticket.getTravelDate().equals(date)){
               ticketFilters.add(ticket);
           }
        }
        if(ticketFilters.isEmpty()){
            ResponseApi<String> responseApi = new ResponseApi<>(false,"Bad Request",HttpStatus.NOT_FOUND,null, LocalDateTime.now());
            return ResponseEntity.ok(responseApi);
        }
        ResponseApi<List<Ticket>> responseApi = new ResponseApi<>(true,"Tickets filtered successfully",HttpStatus.OK,ticketFilters, LocalDateTime.now());
        return ResponseEntity.ok(responseApi);
    }
}