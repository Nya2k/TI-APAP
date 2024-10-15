    package apap.ti.hospitalization2206818953.model;

    import java.util.Date;
    import java.util.List;

    import org.hibernate.annotations.CreationTimestamp;
    import org.hibernate.annotations.UpdateTimestamp;

    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.FetchType;
    import jakarta.persistence.Id;
    import jakarta.persistence.JoinColumn;
    import jakarta.persistence.JoinTable;
    import jakarta.persistence.ManyToMany;
    import jakarta.persistence.ManyToOne;
    import jakarta.persistence.Table;
    import jakarta.persistence.Temporal;
    import jakarta.persistence.TemporalType;
    import jakarta.validation.constraints.NotNull;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "reservation")
    public class Reservation {
        @Id
        private String id;

        @NotNull
        @Column(name = "date_in", nullable = false)
        private Date dateIn;

        @NotNull
        @Column(name = "date_out", nullable = false)
        private Date dateOut;

        @NotNull
        @Column(name = "total_fee", nullable = false)
        private double totalFee;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "id_patient", referencedColumnName = "id")
        private Patient patient;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "id_assigned_nurse", referencedColumnName = "id")
        private Nurse assignedNurse;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "id_room", referencedColumnName = "id")
        private Room room;

        @ManyToMany
        @JoinTable(name = "facilities_reservation", joinColumns = @JoinColumn(name = "id_reservation"), inverseJoinColumns = @JoinColumn(name = "id_facility"))
        private List<Facility> listFacility;

        @CreationTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "created_at", updatable = false, nullable = false)
        private Date createdAt;

        @UpdateTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "updated_at", nullable = false)
        private Date updatedAt;

        @Column(name = "is_deleted", nullable = false)
        private boolean isDeleted;
    }
