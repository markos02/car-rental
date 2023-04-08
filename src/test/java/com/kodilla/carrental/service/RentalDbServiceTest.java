package com.kodilla.carrental.service;

import com.kodilla.carrental.domain.*;
import com.kodilla.carrental.domain.enums.FuelType;
import com.kodilla.carrental.domain.enums.OrderStatus;
import com.kodilla.carrental.domain.enums.RentalStatus;
import com.kodilla.carrental.domain.enums.Transmission;
import com.kodilla.carrental.exception.RentalNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RentalDbServiceTest {

    @Autowired
    RentalDbService rentalDbService;

    @Autowired
    CarDbService carDbService;

    @Autowired
    ClientDbService clientDbService;

    @Autowired
    DamageDbService damageDbService;

    @MockBean
    private ExtraFeesService extraFeesService;

    @Test
    void testGetAllRentals() throws RentalNotFoundException {
        //Given
        Rental rental1 = new Rental();
        Rental rental2 = new Rental();
        Rental rental3 = new Rental();

        //When
        rentalDbService.saveRental(rental1);
        rentalDbService.saveRental(rental2);
        rentalDbService.saveRental(rental3);
        Integer rentalId1 = rental1.getRentalId();
        Integer rentalId2 = rental2.getRentalId();
        Integer rentalId3 = rental3.getRentalId();

        List<Rental> retrievedRentals = rentalDbService.getAllRentals();

        //Then
        assertEquals(3, retrievedRentals.size());

        //CleanUp
        rentalDbService.deleteRental(rentalId1);
        rentalDbService.deleteRental(rentalId2);
        rentalDbService.deleteRental(rentalId3);
    }

    @Test
    void testGetAllRentalsNoRentals() throws RentalNotFoundException {
        //Given
        //When
        List<Rental> retrievedRentals = rentalDbService.getAllRentals();

        //Then
        assertNotNull(retrievedRentals);
        assertEquals(0, retrievedRentals.size());
    }

    @Test
    void testGetRental() throws RentalNotFoundException {
        //Given
        Rental rental1 = new Rental();
        Rental rental2 = new Rental();

        //When
        rentalDbService.saveRental(rental1);
        rentalDbService.saveRental(rental2);
        Integer rentalId1 = rental1.getRentalId();
        Integer rentalId2 = rental2.getRentalId();
        rentalDbService.deleteRental(rentalId2);

        Rental retrievedRental = rentalDbService.getRental(rentalId1);

        //Then
        assertNotNull(retrievedRental);
        assertThrows(RentalNotFoundException.class, () -> rentalDbService.getRental(rentalId2));

        //CleanUp
        rentalDbService.deleteRental(rentalId1);
    }

    @Test
    void testGetActiveRentals() throws RentalNotFoundException {
        //Given
        Rental rental1 = new Rental();
        rental1.setStatus(RentalStatus.RENTED);
        Rental rental2 = new Rental();
        rental2.setStatus(RentalStatus.RENTED);
        Rental rental3 = new Rental();
        rental3.setStatus(RentalStatus.ORDERED);
        Rental rental4 = new Rental();
        rental4.setStatus(RentalStatus.RETURNED);
        Rental rental5 = new Rental();

        //When
        rentalDbService.saveRental(rental1);
        rentalDbService.saveRental(rental2);
        rentalDbService.saveRental(rental3);
        rentalDbService.saveRental(rental4);
        rentalDbService.saveRental(rental5);

        Integer rentalId1 = rental1.getRentalId();
        Integer rentalId2 = rental2.getRentalId();
        Integer rentalId3 = rental3.getRentalId();
        Integer rentalId4 = rental4.getRentalId();
        Integer rentalId5 = rental5.getRentalId();

        List<Rental> retrievedRentals = rentalDbService.getActiveRentals();

        //Then
        assertEquals(2, retrievedRentals.size());
        assertEquals(rentalId1, retrievedRentals.get(0).getRentalId());
        assertEquals(rentalId2, retrievedRentals.get(1).getRentalId());

        //CleanUp
        rentalDbService.deleteRental(rentalId1);
        rentalDbService.deleteRental(rentalId2);
        rentalDbService.deleteRental(rentalId3);
        rentalDbService.deleteRental(rentalId4);
        rentalDbService.deleteRental(rentalId5);
    }

    @Test
    @Transactional
    void testGetOverdueRentals() throws RentalNotFoundException {
        //Given
        LocalDate rental1DateTo = LocalDate.now().plusDays(1);
        LocalDate rental2DateTo = LocalDate.now();
        LocalDate rental3DateTo = LocalDate.now().minusDays(1);
        LocalDate rental4DateTo = LocalDate.now().minusDays(1);
        LocalDate rental5DateTo = LocalDate.now().minusDays(1);
        Order order1 = new Order();
        order1.setDateTo(rental1DateTo);
        Order order2 = new Order();
        order2.setDateTo(rental2DateTo);
        Order order3 = new Order();
        order3.setDateTo(rental3DateTo);
        Order order4 = new Order();
        order4.setDateTo(rental4DateTo);
        Order order5 = new Order();
        order5.setDateTo(rental5DateTo);

        Rental rental1 = new Rental();
        rental1.setStatus(RentalStatus.RENTED);
        rental1.setOrder(order1);
        Rental rental2 = new Rental();
        rental2.setStatus(RentalStatus.RENTED);
        rental2.setOrder(order2);
        Rental rental3 = new Rental();
        rental3.setStatus(RentalStatus.RENTED);
        rental3.setOrder(order3);
        Rental rental4 = new Rental();
        rental4.setStatus(RentalStatus.RETURNED);
        rental4.setOrder(order4);
        Rental rental5 = new Rental();
        rental5.setOrder(order5);

        //When
        rentalDbService.saveRental(rental1);
        rentalDbService.saveRental(rental2);
        rentalDbService.saveRental(rental3);
        rentalDbService.saveRental(rental4);
        rentalDbService.saveRental(rental5);

        Integer rentalId1 = rental1.getRentalId();
        Integer rentalId2 = rental2.getRentalId();
        Integer rentalId3 = rental3.getRentalId();
        Integer rentalId4 = rental4.getRentalId();
        Integer rentalId5 = rental5.getRentalId();

        List<Rental> retrievedRentals = rentalDbService.getOverdueRentals();

        //Then
        assertEquals(1, retrievedRentals.size());
        assertEquals(rentalId3, retrievedRentals.get(0).getRentalId());

        //CleanUp
        rentalDbService.deleteRental(rentalId1);
        rentalDbService.deleteRental(rentalId2);
        rentalDbService.deleteRental(rentalId3);
        rentalDbService.deleteRental(rentalId4);
        rentalDbService.deleteRental(rentalId5);
    }

    @Test
    void testDeleteRental() throws RentalNotFoundException {
        //Given
        Rental rental1 = new Rental();
        Rental rental2 = new Rental();

        //When
        rentalDbService.saveRental(rental1);
        rentalDbService.saveRental(rental2);

        Integer rentalId1 = rental1.getRentalId();
        Integer rentalId2 = rental2.getRentalId();

        rentalDbService.deleteRental(rentalId1);

        //Then
        assertThrows(RentalNotFoundException.class, () -> rentalDbService.deleteRental(rentalId1));
        assertDoesNotThrow(() -> rentalDbService.deleteRental(rentalId2));
    }

    @Test
    @Transactional
    void testReturnCarNoFees() throws RentalNotFoundException {
        //Given
        LocalDate order1DateFrom = LocalDate.now().minusDays(10);
        LocalDate order1DateTo = LocalDate.now();

        Client client1 = new Client();
        client1.setFirstname("John");
        client1.setLastname("Smith");

        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");

        Order order1 = new Order();
        order1.setClient(client1);
        order1.setCar(car1);
        order1.setDateFrom(order1DateFrom);
        order1.setDateTo(order1DateTo);
        order1.setStatus(OrderStatus.COMPLETED);
        order1.setChildSeat(true);
        order1.setGps(true);
        order1.setExtraDriver(false);
        order1.setFuelLevel(0.25);

        Rental rental = new Rental();
        rental.setOrder(order1);
        rental.setStatus(RentalStatus.RENTED);

        order1.setRental(rental);

        //When
        carDbService.saveCar(car1);;
        clientDbService.saveClient(client1);

        rentalDbService.saveRental(rental);
        Integer rentalId = rental.getRentalId();
        Double fuelLevel = 0.25;

        rentalDbService.returnCar(rentalId, LocalDate.now(), fuelLevel);

        Rental retrievedRental = rentalDbService.getRental(rentalId);

        //Then
        assertNotNull(retrievedRental);
        assertEquals(rentalId, retrievedRental.getRentalId());
        assertEquals(RentalStatus.RETURNED, retrievedRental.getStatus());
        verify(extraFeesService, never()).fuelFee(any());
        verify(extraFeesService, never()).overdueFee(any());
        verify(extraFeesService, never()).newDamagesFee(any());
    }

    @Test
    @Transactional
    void testReturnCarFuelFee() throws RentalNotFoundException {
        //Given
        LocalDate order1DateFrom = LocalDate.now().minusDays(10);
        LocalDate order1DateTo = LocalDate.now();

        Client client1 = new Client();
        client1.setFirstname("John");
        client1.setLastname("Smith");

        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");

        Order order1 = new Order();
        order1.setClient(client1);
        order1.setCar(car1);
        order1.setDateFrom(order1DateFrom);
        order1.setDateTo(order1DateTo);
        order1.setStatus(OrderStatus.COMPLETED);
        order1.setChildSeat(true);
        order1.setGps(true);
        order1.setExtraDriver(false);
        order1.setFuelLevel(0.25);

        Rental rental = new Rental();
        rental.setOrder(order1);
        rental.setStatus(RentalStatus.RENTED);

        order1.setRental(rental);

        //When
        carDbService.saveCar(car1);
        clientDbService.saveClient(client1);

        rentalDbService.saveRental(rental);
        Integer rentalId = rental.getRentalId();
        Double fuelLevel = 0.10;

        rentalDbService.returnCar(rentalId, LocalDate.now(), fuelLevel);

        Rental retrievedRental = rentalDbService.getRental(rentalId);

        //Then
        assertNotNull(retrievedRental);
        assertEquals(rentalId, retrievedRental.getRentalId());
        assertEquals(RentalStatus.RETURNED, retrievedRental.getStatus());
        verify(extraFeesService, times(1)).fuelFee(any());
        verify(extraFeesService, never()).overdueFee(any());
        verify(extraFeesService, never()).newDamagesFee(any());
    }

    @Test
    @Transactional
    void testReturnCarOverdueFee() throws RentalNotFoundException {
        //Given
        LocalDate order1DateFrom = LocalDate.now().minusDays(10);
        LocalDate order1DateTo = LocalDate.now().minusDays(1);

        Client client1 = new Client();
        client1.setFirstname("John");
        client1.setLastname("Smith");

        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");

        Order order1 = new Order();
        order1.setClient(client1);
        order1.setCar(car1);
        order1.setDateFrom(order1DateFrom);
        order1.setDateTo(order1DateTo);
        order1.setStatus(OrderStatus.COMPLETED);
        order1.setChildSeat(true);
        order1.setGps(true);
        order1.setExtraDriver(false);
        order1.setFuelLevel(0.25);

        Rental rental = new Rental();
        rental.setOrder(order1);
        rental.setStatus(RentalStatus.RENTED);

        order1.setRental(rental);

        //When
        carDbService.saveCar(car1);
        clientDbService.saveClient(client1);

        rentalDbService.saveRental(rental);
        Integer rentalId = rental.getRentalId();
        Double fuelLevel = 0.25;

        rentalDbService.returnCar(rentalId, LocalDate.now(), fuelLevel);

        Rental retrievedRental = rentalDbService.getRental(rentalId);

        //Then
        assertNotNull(retrievedRental);
        assertEquals(rentalId, retrievedRental.getRentalId());
        assertEquals(RentalStatus.RETURNED, retrievedRental.getStatus());
        verify(extraFeesService, never()).fuelFee(any());
        verify(extraFeesService, times(1)).overdueFee(any());
        verify(extraFeesService, never()).newDamagesFee(any());
    }

    @Test
    @Transactional
    void testReturnCarNewDamageFee() throws RentalNotFoundException {
        //Given
        LocalDate order1DateFrom = LocalDate.now().minusDays(10);
        LocalDate order1DateTo = LocalDate.now();

        Client client1 = new Client();
        client1.setFirstname("John");
        client1.setLastname("Smith");

        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");

        Order order1 = new Order();
        order1.setClient(client1);
        order1.setCar(car1);
        order1.setDateFrom(order1DateFrom);
        order1.setDateTo(order1DateTo);
        order1.setStatus(OrderStatus.COMPLETED);
        order1.setChildSeat(true);
        order1.setGps(true);
        order1.setExtraDriver(false);
        order1.setFuelLevel(0.25);

        Rental rental = new Rental();
        rental.setOrder(order1);
        rental.setStatus(RentalStatus.RENTED);

        order1.setRental(rental);

        //When
        carDbService.saveCar(car1);
        clientDbService.saveClient(client1);

        rentalDbService.saveRental(rental);

        Damage damage = new Damage();
        damage.setRental(rental);
        rental.getDamages().add(damage);
        damageDbService.saveDamage(damage);

        Integer rentalId = rental.getRentalId();
        Double fuelLevel = 0.25;

        rentalDbService.returnCar(rentalId, LocalDate.now(), fuelLevel);

        Rental retrievedRental = rentalDbService.getRental(rentalId);

        //Then
        assertNotNull(retrievedRental);
        assertEquals(rentalId, retrievedRental.getRentalId());
        assertEquals(RentalStatus.RETURNED, retrievedRental.getStatus());
        verify(extraFeesService, never()).fuelFee(any());
        verify(extraFeesService, never()).overdueFee(any());
        verify(extraFeesService, times(1)).newDamagesFee(any());
    }

    @Test
    @Transactional
    void testReturnCarFuelAndOverdueAndNewDamageFee() throws RentalNotFoundException {
        //Given
        LocalDate order1DateFrom = LocalDate.now().minusDays(10);
        LocalDate order1DateTo = LocalDate.now().minusDays(1);

        Client client1 = new Client();
        client1.setFirstname("John");
        client1.setLastname("Smith");

        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");

        Order order1 = new Order();
        order1.setClient(client1);
        order1.setCar(car1);
        order1.setDateFrom(order1DateFrom);
        order1.setDateTo(order1DateTo);
        order1.setStatus(OrderStatus.COMPLETED);
        order1.setChildSeat(true);
        order1.setGps(true);
        order1.setExtraDriver(false);
        order1.setFuelLevel(0.25);

        Rental rental = new Rental();
        rental.setOrder(order1);
        rental.setStatus(RentalStatus.RENTED);

        order1.setRental(rental);

        //When
        carDbService.saveCar(car1);
        clientDbService.saveClient(client1);

        rentalDbService.saveRental(rental);

        Damage damage = new Damage();
        damage.setRental(rental);
        rental.getDamages().add(damage);
        damageDbService.saveDamage(damage);

        Integer rentalId = rental.getRentalId();
        Double fuelLevel = 0.24;

        rentalDbService.returnCar(rentalId, LocalDate.now(), fuelLevel);

        Rental retrievedRental = rentalDbService.getRental(rentalId);

        //Then
        assertNotNull(retrievedRental);
        assertEquals(rentalId, retrievedRental.getRentalId());
        assertEquals(RentalStatus.RETURNED, retrievedRental.getStatus());
        verify(extraFeesService, times(1)).fuelFee(any());
        verify(extraFeesService, times(1)).overdueFee(any());
        verify(extraFeesService, times(1)).newDamagesFee(any());
    }
}