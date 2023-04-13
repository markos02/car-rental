package com.kodilla.carrental.scheduler;

import com.kodilla.carrental.domain.Mail;
import com.kodilla.carrental.domain.Rental;
import com.kodilla.carrental.service.RentalDbService;
import com.kodilla.carrental.service.SimpleEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailScheduler {

    private final SimpleEmailService simpleEmailService;
    private static final String SUBJECT = "Overdue Rentals";
    private static final String EMAIL = "test@email.com";

    @Autowired
    RentalDbService rentalDbService;

    @Scheduled(cron = "0 0 1 * * *")
    public void sendOverdueRentalsEmail() {
        String message;
        List<Rental> overdueRentals = rentalDbService.getOverdueRentals();

        if (overdueRentals.size() == 0) {
            message = "There are no rentals that are overdue";
        } else {
            message = "There are currently: " + overdueRentals.size() + "rentals that are overdue";
        }

        simpleEmailService.send(new Mail(EMAIL, SUBJECT, message)
        );
    }
}
