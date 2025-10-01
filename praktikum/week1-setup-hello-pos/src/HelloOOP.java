// HelloOOP.java
class Mahasiswa {
 String nama; int nim;
 Mahasiswa(String n, int u){ nama=n; nim=u; } void sapa(){ System.out.println("Halo word i am, " + nama +"-"+ nim); }}
public class HelloOOP {
 public static void main(String[] args) {
 Mahasiswa m = new Mahasiswa("HANDIKA DWI ARDIYANTO", 240202863);
 m.sapa();
 }
}