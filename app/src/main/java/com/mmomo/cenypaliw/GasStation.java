package com.mmomo.cenypaliw;

public class GasStation {
    private int ID;
    private String name;
    private String street;
    private String city;
    private String postalCode;
    private String province;
    private String county;
    private double RON95;
    private double RON98;
    private double ON;
    private double LPG;
    private double CNG;

    public GasStation(int ID, String name, String street,
                      String city, String postalCode, String province,
                      String county, double RON95, double RON98, double ON, double LPG, double CNG) {
        this.ID = ID;
        this.name = name;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.province = province;
        this.county = county;
        this.RON95 = RON95;
        this.RON98 = RON98;
        this.ON = ON;
        this.LPG = LPG;
        this.CNG = CNG;
    }

    public GasStation() {

    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getProvince() {
        return province;
    }

    public String getCounty() {
        return county;
    }

    public double getRON95() {
        return RON95;
    }

    public double getRON98() {
        return RON98;
    }

    public double getON() {
        return ON;
    }

    public double getLPG() {
        return LPG;
    }

    public double getCNG() {
        return CNG;
    }

}
