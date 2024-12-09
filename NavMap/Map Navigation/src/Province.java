import java.sql.SQLOutput;
import java.util.Arrays;

public class Province {
    //Fields
    private String nama;
    private int luas;
    private int totalCity;
    private String[] tetangga;

    //Constructor
    public Province(String nama, int luas, int totalCity, String[] tetangga) {
        this.nama = nama;
        this.luas = luas;
        this.totalCity = totalCity;
        this.tetangga = tetangga;
    }

    //Getter and Setter for Nama
    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }

    //Getter and Setter untuk luas
    public int getLuas() {
        return luas;
    }
    public void setLuas(int luas) {
        this.luas = luas;
    }

    //Getter dan setter untuk totalCity
    public int getTotalCity() {
        return totalCity;
    }
    public void setTotalCity(int totalCity) {
        this.totalCity = totalCity;
    }

    //Getter dan Setter untuk tetangga
    public String[] getTetangga() {
        return tetangga;
    }
    public void setTetangga(String[] tetangga) {
        this.tetangga = tetangga;
    }

    //Method void display province secara detail
    public void displayProvinceDetails() {
        System.out.println("Nama Provinsi: " + nama);
        System.out.println("Luas: " + luas + " km²");
        System.out.println("Provinsi Tetangga: " + Arrays.toString(tetangga));
    }
}