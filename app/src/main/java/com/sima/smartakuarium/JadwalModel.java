package com.sima.smartakuarium;

public class JadwalModel {
    public String time;
    public String jenis;
    public String repeat;

    public JadwalModel(String time, String jenis, String repeat) {
        this.time = time;
        this.jenis = jenis;
        this.repeat = repeat;
    }

    public JadwalModel() {}

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }

    public String getRepeat() { return repeat; }
    public void setRepeat(String repeat) { this.repeat = repeat; }
}