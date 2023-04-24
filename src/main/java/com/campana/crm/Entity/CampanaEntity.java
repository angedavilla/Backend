package com.campana.crm.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "campana_crm")
public class CampanaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "fecha", updatable = false)
    private LocalDate fecha = LocalDate.now();
    
}
