package com.mmomo.cenypaliw;

public class YourGasStation extends GasStation {

    private int ID_your_station;

    public YourGasStation(int ID, int ID_your_station, String name, String street, String city, String postalCode, String province, String county, int RON95, int RON98, int ON, int LPG, int CNG) {
        super(ID, name, street, city, postalCode, province, county, RON95, RON98, ON, LPG, CNG);
        this.ID_your_station = ID_your_station;
    }


    public YourGasStation(String city, String name) {
        super();
        //Probably this will need updating: there will be more String in constructor in order to distinguish stations

        /*1.Wyłuskanie stacji z wybranego miasta
        2. Przejscie po liscie kilku tych stacji
        3. Porówywanie nazw czy znajduje się w nich wpisana nazwa
        4. Zebranie danych stacji spelniajacej kryteria
            4b. Stwierdzenie ze nie da sie takich naleźć
        5. Podstawienie danych do pól stacji.

        */
    }

    public int getID_your_station() {
        return ID_your_station;
    }


}
