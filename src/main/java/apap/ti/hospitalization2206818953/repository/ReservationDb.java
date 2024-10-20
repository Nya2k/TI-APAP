package apap.ti.hospitalization2206818953.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import apap.ti.hospitalization2206818953.model.Reservation;

public interface ReservationDb extends JpaRepository<Reservation, String> {
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.isDeleted = false")
    int countByIsDeletedFalse();

    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId AND r.dateIn <= :dateOut AND r.dateOut >= :dateIn")
    List<Reservation> findByRoomIdAndDateInBetween(String roomId, Date dateIn, Date dateOut);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.room.id = :roomId AND " + "(:startDate <= r.dateOut AND :endDate >= r.dateIn)")
    int countReservationsInRange(@Param("roomId") String roomId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT r FROM Reservation r WHERE YEAR(r.dateIn) = :year")
    List<Reservation> findByYear(@Param("year") int year);
}