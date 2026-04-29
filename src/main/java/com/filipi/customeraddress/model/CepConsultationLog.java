package com.filipi.customeraddress.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cep_consultation_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CepConsultationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 8)
    private String zipCode;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String responsePayload;

    @Column(nullable = false)
    private Integer httpStatus;

    @Column(nullable = false)
    private Boolean success;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime consultedAt;
}
