package com.mmomo.cenypaliw;

public enum GasStationNames {
    ORLEN(0),LOTOS(1),GROSAR(2),BP(3),NONE(4);

    private  int i;
    private GasStationNames(int i){
        this.i=i;
    }
    public static int getPosition(GasStationNames name) {
        return name.i;
    }
}
