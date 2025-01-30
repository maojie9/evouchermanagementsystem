package com.maojie.evouchersystem.evouchermanagementsystem.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maojie.evouchersystem.evouchermanagementsystem.domain.DBStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Customer implements Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Column(unique=true)
    private String mobileNoString;

    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private DBStatus status;
    
    @OneToMany(mappedBy = "customer")
    private List<Sorder> sorders;

    

}
