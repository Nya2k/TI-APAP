package apap.ti.hospitalization2206818953;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.github.javafaker.Faker;

import apap.ti.hospitalization2206818953.model.Facility;
import apap.ti.hospitalization2206818953.model.Nurse;
import apap.ti.hospitalization2206818953.model.Patient;
import apap.ti.hospitalization2206818953.model.Reservation;
import apap.ti.hospitalization2206818953.model.Room;
import apap.ti.hospitalization2206818953.service.FacilityService;
import apap.ti.hospitalization2206818953.service.NurseService;
import apap.ti.hospitalization2206818953.service.PatientService;
import apap.ti.hospitalization2206818953.service.ReservationService;
import apap.ti.hospitalization2206818953.service.RoomService;

@SpringBootApplication
public class Hospitalization2206818953Application {

	public static void main(String[] args) {
		SpringApplication.run(Hospitalization2206818953Application.class, args);
	}

    @Bean
	@Transactional
	CommandLineRunner run(RoomService roomService, PatientService patientService, NurseService nurseService, FacilityService facilityService, ReservationService reservationService) {
		return args -> {
			var faker = new Faker(new Locale("in-ID"));

            // Data Room
            var room = new Room();
            room.setId(faker.idNumber().valid());
            String roomName = faker.name().title();
            room.setName(roomName.substring(0, Math.min(30, roomName.length())));
            room.setDescription(faker.lorem().sentence());
            room.setMaxCapacity(faker.number().numberBetween(1, 10));
            room.setPricePerDay(faker.number().randomDouble(2, 100000, 1000000));
            room.setDeleted(false);
            roomService.addRoom(room);


            // Data Patient
            var patient = new Patient();
            patient.setId(UUID.randomUUID());
            patient.setNIK(faker.idNumber().valid());
            patient.setName(faker.name().fullName());
            patient.setEmail(faker.internet().emailAddress());
            patient.setBirthDate(faker.date().birthday());
            patient.setGender(faker.bool().bool());
            patient.setDeleted(false);
            patientService.addPatient(patient);

            // Data Nurse
            var nurse = new Nurse();
            nurse.setId(UUID.randomUUID());
            nurse.setName(faker.name().fullName());
            nurse.setEmail(faker.internet().emailAddress());
            nurse.setGender(faker.bool().bool());
            nurse.setDeleted(false);
            nurseService.addNurse(nurse);

            // Data Facility
            var facility = new Facility();
            facility.setId(UUID.randomUUID());
            facility.setName(faker.commerce().productName());
            facility.setFee(faker.number().randomDouble(2, 50000, 500000));
            facility.setDeleted(false);
            facilityService.addFacility(facility);

            // Data Reservation
            var reservation = new Reservation();
            reservation.setId(faker.idNumber().valid());
            reservation.setDateIn(faker.date().past(10, TimeUnit.DAYS));
            reservation.setDateOut(faker.date().future(5, TimeUnit.DAYS));
            reservation.setTotalFee(faker.number().randomDouble(2, 1000000, 5000000));
            reservation.setPatient(patient);
            reservation.setAssignedNurse(nurse);
            reservation.setRoom(room);
            reservation.setListFacility(new ArrayList<>(List.of(facility))); // Add a list of facilities
            reservation.setDeleted(false);
            reservationService.addReservation(reservation);
		};
	}

}
