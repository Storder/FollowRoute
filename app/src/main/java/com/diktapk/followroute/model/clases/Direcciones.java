package com.diktapk.followroute.model.clases;

import android.location.Address;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Direcciones {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Long date;
    private String direccion;
    private String countryCode;
    private double latitude;
    private double longitude;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public static Direcciones transfor(Address address){
        Direcciones direccion = new Direcciones();
        direccion.setDireccion(address.getAddressLine(0));
        direccion.setCountryCode(address.getCountryCode());
        direccion.setLatitude(address.getLatitude());
        direccion.setLongitude(address.getLongitude());

        return direccion;
    }

}
