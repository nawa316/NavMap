import java.util.*;

public class MapGraph {
    private String nama;
    Map<String, List<CityNode>> nodeMap;
    private int totalCity;
    private int totalProvince;

    public MapGraph(){
        nodeMap = new HashMap<>();
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getTotalCity() {
        return totalCity;
    }

    public void setTotalCity(int totalCity) {
        this.totalCity = totalCity;
    }

    public int getTotalProvince() {
        return totalProvince;
    }

    public void setTotalProvince(int totalProvince) {
        this.totalProvince = totalProvince;
    }

    public void addCity(String kota){
        CityNode node = new CityNode(kota);
        nodeMap.putIfAbsent(kota, new ArrayList<>());
    }

    public void addPath(String kota1, String kota2, int weight){
        CityNode city2 = new CityNode(kota2);
        nodeMap.get(kota1).add(city2);
        city2.setTetangga(kota1);

        CityNode city1 = new CityNode(kota1);
        nodeMap.get(kota2).add(city1);
        city1.setTetangga(kota2);

        Navigation nav = new Navigation();
        nav.addNav(kota1, kota2, weight);
    }

    public Set<String> getAllCity(){
        return nodeMap.keySet();
    }

    public void searchCity(String Nama){
        if (nodeMap.containsKey(Nama)){
            System.out.println("Kota " + Nama + " terdaftar di map kami");
        } else {
            System.out.println("Kota " + Nama + " belum terdaftar di map kami");
            System.out.print("Apakah kamu mau mendaftarkan Kota " + Nama + "?(Ya atau Tidak): ");
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            if (input.equals("Ya")){
                System.out.println();
                System.out.print("Nama city: ");
                input = sc.nextLine();
                addCity(input);
                System.out.print("Terimakasih telah melakukan input kota baru");
            } else {
                System.out.println("Kembali ke menu utama");
            }
        }
    }
}
