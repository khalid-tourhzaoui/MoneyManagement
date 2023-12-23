package com.mediatech.MoneyManagement.Models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class DaretOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String designation;

    @Column(nullable = false)
    private int nombreParticipant;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;
    @Column(nullable = false,length = 30)
    private String typePeriode; // Mensuelle, hebdomadaire, etc.

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User adminOffre;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(nullable = false)
    private double montantParPeriode;

	public DaretOperation(String designation, int nombreParticipant, LocalDate dateDebut, LocalDate dateFin,String typePeriode, User adminOffre, String status,
			double montantParPeriode) {
		
		this.designation = designation;
		this.nombreParticipant = nombreParticipant;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.typePeriode = typePeriode;
		this.adminOffre = adminOffre;
		this.status = status;
		this.montantParPeriode = montantParPeriode;
	}

	public DaretOperation() {
		super();
		//TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public int getNombreParticipant() {
		return nombreParticipant;
	}

	public void setNombreParticipant(int nombreParticipant) {
		this.nombreParticipant = nombreParticipant;
	}

	public LocalDate getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(LocalDate dateDebut) {
		this.dateDebut = dateDebut;
	}

	public LocalDate getDateFin() {
		return dateFin;
	}

	public void setDateFin(LocalDate dateFin) {
		this.dateFin = dateFin;
	}

	public String getTypePeriode() {
		return typePeriode;
	}

	public void setTypePeriode(String typePeriode) {
		this.typePeriode = typePeriode;
	}

	public User getAdminOffre() {
		return adminOffre;
	}

	public void setAdminOffre(User adminOffre) {
		this.adminOffre = adminOffre;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getMontantParPeriode() {
		return montantParPeriode;
	}

	public void setMontantParPeriode(double montantParPeriode) {
		this.montantParPeriode = montantParPeriode;
	}
	
    
}
