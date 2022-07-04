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


    public static Direcciones transfor(Address address){
        Direcciones direccion = new Direcciones();
        direccion.setDireccion(address.getAddressLine(0));
        direccion.setCountryCode(address.getCountryCode());
        direccion.setLatitude(address.getLatitude());
        direccion.setLongitude(address.getLongitude());

        return direccion;
    }

}
