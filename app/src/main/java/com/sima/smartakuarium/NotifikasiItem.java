package com.sima.smartakuarium;

public class NotifikasiItem {
    private String pesan;
    private String waktu;

    public NotifikasiItem(String pesan, String waktu) {
        this.pesan = pesan;
        this.waktu = waktu;
    }

    public String getPesan() {
        return pesan;
    }

    public String getWaktu() {
        return waktu;
    }
}
