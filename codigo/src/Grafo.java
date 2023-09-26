import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Grafo {
    private List<Cidade> vertices;
    private List<Estrada> arestas;

    // Construtor
    public Grafo() {
        this.vertices = new ArrayList<>();
        this.arestas = new ArrayList<>();
    }

    // Getters
    public List<Cidade> getVertices() {
        return vertices;
    }

    public List<Estrada> getArestas() {
        return arestas;
    }

    // Adicionar
    public void adicionarCidade(Cidade cidade) {
        vertices.add(cidade);
    }
    public void adicionarEstrada(Estrada estrada) {
        arestas.add(estrada);
    }

    // Ler do arquivo
      public static Grafo lerGrafo(String nomeArquivo) {
        Grafo grafo = new Grafo();

        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(":");
                String nomeCidade = partes[0].trim();
                String[] vizinhas = partes[1].trim().split(",");

                Cidade cidade = new Cidade(grafo.getVertices().size() + 1, nomeCidade);
                grafo.adicionarCidade(cidade);

                for (String vizinhaInfo : vizinhas) {
                    String[] vizinhaDados = vizinhaInfo.trim().split("\\(");
                    String nomeVizinha = vizinhaDados[0].trim();
                    int distancia = Integer.parseInt(vizinhaDados[1].replaceAll("\\D+", ""));

                    Cidade vizinha = new Cidade(grafo.getVertices().size() + 1, nomeVizinha);
                    grafo.adicionarCidade(vizinha);

                    Estrada estrada = new Estrada(cidade, vizinha, distancia);
                    grafo.adicionarEstrada(estrada);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return grafo;
    }


     // Método que eu achava que era pra fazer

    public void recomendarVisitaTodasCidades() {
        Set<Cidade> visitadas = new HashSet<>();
        
        for (Cidade cidade : vertices) {
            if (!visitadas.contains(cidade)) {
                System.out.println("Recomendação de visita a partir de: " + cidade.getNome());
                buscaProfundidade(cidade, visitadas, cidade.getNome());
                System.out.println();
            }
        }
    }
    
    private void buscaProfundidade(Cidade cidade, Set<Cidade> visitadas, String pontoPartida) {
        visitadas.add(cidade);
        System.out.println("Visite: " + cidade.getNome());
    
        for (Estrada estrada : arestas) {
            if (estrada.getOrigem().equals(cidade) && !visitadas.contains(estrada.getDestino())) {
                System.out.println("Pegue a estrada para: " + estrada.getDestino().getNome() + " (Distância: " + estrada.getPeso() + ")");
                buscaProfundidade(estrada.getDestino(), visitadas, pontoPartida);
            }
        }
    }
    
    // Método da rota que passa por todas

     public static List<Cidade> encontrarRota(Grafo grafo) {
        List<Cidade> cidades = grafo.getVertices();
        List<Cidade> melhorRota = null;
        double menorDistancia = Double.POSITIVE_INFINITY;

        List<List<Cidade>> permutacoes = permutacoes(cidades);

        for (List<Cidade> rota : permutacoes) {
            double distanciaTotal = calcularDistanciaTotal(grafo, rota);
            if (distanciaTotal < menorDistancia) {
                menorDistancia = distanciaTotal;
                melhorRota = rota;
            }
        }

        return melhorRota;
    }

    private static double calcularDistanciaTotal(Grafo grafo, List<Cidade> rota) {
        double distanciaTotal = 0;
        for (int i = 0; i < rota.size() - 1; i++) {
            Cidade cidadeAtual = rota.get(i);
            Cidade proximaCidade = rota.get(i + 1);
            for (Estrada estrada : grafo.getArestas()) {
                if ((estrada.getOrigem() == cidadeAtual && estrada.getDestino() == proximaCidade)
                        || (estrada.getOrigem() == proximaCidade && estrada.getDestino() == cidadeAtual)) {
                    distanciaTotal += estrada.getPeso();
                    break; // Encontrou a estrada, pare a busca
                }
            }
        }
        return distanciaTotal;
    }
    


    public List<Cidade> encontrarRotaQuePassePorTodasCidades() {
        List<Cidade> cidades = getVertices();
        List<Cidade> melhorRota = null;
        double menorDistancia = Double.POSITIVE_INFINITY;


        List<List<Cidade>> permutacoes = permutacoes(cidades);

        for (List<Cidade> rota : permutacoes) {
            double distanciaTotal = calcularDistanciaTotal(rota);
            if (distanciaTotal < menorDistancia) {
                menorDistancia = distanciaTotal;
                melhorRota = rota;
            }
        }

        return melhorRota;
    }

    private double calcularDistanciaTotal(List<Cidade> rota) {
        double distanciaTotal = 0;

        for (int i = 0; i < rota.size() - 1; i++) {
            Cidade cidadeAtual = rota.get(i);
            Cidade proximaCidade = rota.get(i + 1);

            for (Estrada estrada : getArestas()) {
                if ((estrada.getOrigem() == cidadeAtual && estrada.getDestino() == proximaCidade)
                        || (estrada.getOrigem() == proximaCidade && estrada.getDestino() == cidadeAtual)) {
                    distanciaTotal += estrada.getPeso();
                    break; // Encontrou a estrada, pare a busca
                }
            }
        }

        return distanciaTotal;
    }

    public static List<List<Cidade>> permutacoes(List<Cidade> cidades) {
        List<List<Cidade>> permutacoes = new ArrayList<>();
        permutacoesRecursivas(cidades, 0, permutacoes);
        return permutacoes;
    }

    public static void permutacoesRecursivas(List<Cidade> cidades, int inicio, List<List<Cidade>> permutacoes) {
        if (inicio == cidades.size()) {
            permutacoes.add(new ArrayList<>(cidades));
        } else {
            for (int i = inicio; i < cidades.size(); i++) {
                Collections.swap(cidades, inicio, i);
                permutacoesRecursivas(cidades, inicio + 1, permutacoes);
                Collections.swap(cidades, inicio, i);
            }
        }
    }
    
}
