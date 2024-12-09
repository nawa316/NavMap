import java.util.ArrayList;
import java.util.List;

public class CityNode {
    private String nama;
    private String province;
    private int luas;
    private List<String> tetangga;

    public CityNode(String nama){
        this.nama = nama;
        this.tetangga = new ArrayList<>();
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getLuas() {
        return luas;
    }

    public void setLuas(int luas) {
        this.luas = luas;
    }

    public List<String> getTetangga() {
        return tetangga;
    }

    public void setTetangga(String tetangga) {
        this.tetangga.add(tetangga);
    }
}
