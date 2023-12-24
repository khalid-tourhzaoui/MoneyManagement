package com.mediatech.MoneyManagement.DTO;

import java.time.LocalDate;

public class DaretOperationDto {

    private String designation;
    private int nombreParticipant;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String typePeriode;
    private double montantParPeriode;
    
    

    public DaretOperationDto(String designation, int nombreParticipant, LocalDate dateDebut, LocalDate dateFin,
			String typePeriode, double montantParPeriode) {
		super();
		this.designation = designation;
		this.nombreParticipant = nombreParticipant;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.typePeriode = typePeriode;
		this.montantParPeriode = montantParPeriode;
	}

	// Ajoutez des getters et setters pour chaque champ

    public DaretOperationDto() {
		super();
		//TODO Auto-generated constructor stub
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

    public double getMontantParPeriode() {
        return montantParPeriode;
    }

    public void setMontantParPeriode(double montantParPeriode) {
        this.montantParPeriode = montantParPeriode;
    }
}

