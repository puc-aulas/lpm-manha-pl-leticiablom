import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1 - Listar visitas ");
        System.out.println("2 - Recomendar visitas por todas as cidades ");
        int numero = scanner.nextInt();
        
         Grafo grafo = Grafo.lerGrafo("grafo.txt");
        
        switch (numero) {
            case 1:
            grafo.recomendarVisitaTodasCidades();
                break;
            case 2:
             grafo.encontrarRotaQuePassePorTodasCidades();
                break;

}
scanner.close();
}
}