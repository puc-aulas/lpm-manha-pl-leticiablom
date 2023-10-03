import java.util.List;

public class App {
    public static void main(String[] args) {
        Grafo grafo = new Grafo();

        // Arquivo para o grafo
        grafo.carregarGrafoDeArquivo("grafo.txt");

        List<String> resultadoBusca = grafo.buscaLargura();

        // Imprimir
        System.out.println("Resultado da busca em largura:");
        for (String aresta : resultadoBusca) {
            System.out.println(aresta);
        }
    }
}
