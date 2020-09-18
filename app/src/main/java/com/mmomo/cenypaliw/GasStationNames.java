package com.mmomo.cenypaliw;

public enum GasStationNames {
    //Help to distinguish popular franchise, used to show their images in Markers and Your Station List
    ORLEN(0),LOTOS(1),GROSAR(2),BP(3),NONE(4);

    private  int i;
    private GasStationNames(int i){
        this.i=i;
    }
    public static int getPosition(GasStationNames name) {
        return name.i;
    }

    public static String bigStationNameShortcut(String name){
        if(name.contains("ORLEN")){
            return "ORLEN";
        }else if(name.contains("LOTOS")){
            return "LOTOS";
        } else if (name.contains("GROSAR")) {
            return "GROSAR";
        } else if (name.contains("BP")) {
            return "BP";
        }else return name;
    }
}
