import java.util.function.BiConsumer;
public class HelloFunctional {
 public static void main(String[] args) { BiConsumer<String,Integer> sapa =
 (nama, nim) -> System.out.println("Halo Word I am, " + nama + "-" + nim);
 sapa.accept("HANDIKA DWI ARDIYANTO", 240202863);
 }
}

