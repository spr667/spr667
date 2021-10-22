package com.sharebooks.models;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;

@Entity
@Table(name = "usuari")
public class UsuarisModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String nom;
    private String contrasenya;
    private boolean esadmin;
    private UUID token;

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public boolean isEsadmin() {
        return esadmin;
    }

    public void setEsadmin(boolean esadmin) {
        this.esadmin = esadmin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }
    
}