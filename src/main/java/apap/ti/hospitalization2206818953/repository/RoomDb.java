package apap.ti.hospitalization2206818953.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import apap.ti.hospitalization2206818953.model.Room;

@Repository
public interface RoomDb extends JpaRepository<Room, String> {
    @Query("SELECT COUNT(r) FROM Room r WHERE r.isDeleted = false")
    int countByIsDeletedFalse();
}