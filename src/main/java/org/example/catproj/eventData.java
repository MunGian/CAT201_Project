package org.example.catproj;

public class eventData {
    private int id;
    private String name;
    private String date;
    private String time;
    private String desc;
    private String imag;

    public eventData(int id, String name, String date, String time, String desc, String imag){
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.desc = desc;
        this.imag = imag;
    }

    public int getid() {return id;}

    public String getName(){
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDesc() {
        return desc;
    }

    public String getImag() {
        return imag;
    }
}
