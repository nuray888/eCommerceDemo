package com.example.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 2, message = "State name must be at least 5 characters")
    private String state;

    @NotBlank
    @Size(min = 5, message = "Building name must be at least 5 characters")
    private String buildingName;

    @NotBlank
    @Size(min = 5, message = "Country name must be at least 5 characters")
    private String country;

    @NotBlank
    @Size(min = 4, message = "City name must be at least 5 characters")
    private String city;

    @NotBlank
    @Size(min = 5, message = "Street name must be at least 5 characters")
    private String street;

    @NotBlank
    @Size(min = 6, message = "Pin code name must be at least 5 characters")
    private String pinCode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users;

    public Address(String state, String buildingName, String country, String city, String street, String pinCode) {
        this.state = state;
        this.buildingName = buildingName;
        this.country = country;
        this.city = city;
        this.street = street;
        this.pinCode = pinCode;
    }
}
