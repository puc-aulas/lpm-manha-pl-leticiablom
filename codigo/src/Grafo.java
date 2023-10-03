import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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

   
    public void carregarGrafoDeArquivo(String nomeArquivo) {
    try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
        String linha;

        while ((linha = br.readLine()) != null) {
            processarLinha(linha);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    private void processarLinha(String linha) {
        String[] partes = linha.split(":");
        if (partes.length >= 2) {
            String nomeCidadeOrigem = partes[0].trim();
            String[] conexoes = partes[1].trim().split(",");
            
            // Crie a cidade de origem e adicione ao grafo
            Cidade cidadeOrigem = new Cidade(vertices.size() + 1, nomeCidadeOrigem);
            adicionarCidade(cidadeOrigem);
    
            for (String conexao : conexoes) {
                String[] dadosConexao = conexao.trim().split("\\s*\\(\\s*|\\s*\\)\\s*");
    
                if (dadosConexao.length == 2) {
                    String nomeCidadeDestino = dadosConexao[0].trim();
                    int distancia = Integer.parseInt(dadosConexao[1]);
    
                    // Crie a cidade de destino e a estrada correspondente
                    Cidade cidadeDestino = new Cidade(vertices.size() + 1, nomeCidadeDestino);
                    Estrada estrada = new Estrada(cidadeOrigem, cidadeDestino, distancia);
    
                    adicionarCidade(cidadeDestino);
                    adicionarEstrada(estrada);
                }
            }
        }
    }
    

    public List<String> buscaLargura() {
        Queue<Cidade> fila = new LinkedList<>();
        int t = 0; // Variável para atribuir valores L (tempo de visita)
        Map<Cidade, Integer> nivel = new HashMap<>(); // Mapeia cada cidade para seu nível
        Map<Cidade, Cidade> pai = new HashMap<>(); // Mapeia cada cidade para seu pai
        Map<Cidade, Integer> l = new HashMap<>(); // Mapeia cada cidade para seu tempo de visita
        Set<Cidade> visitados = new HashSet<>(); // Ver cidades já visitadas
        Set<String> cidadesDestino = new HashSet<>(); // Ver se já está em resultado
        List<String> resultado = new ArrayList<>(); // Consertar resultado
    
        
        for (Cidade cidade : vertices) {
            if (!visitados.contains(cidade)) { // Ver se a cidade já foi visitada
                fila.add(cidade);
                nivel.put(cidade, 0);
                pai.put(cidade, null);
    
                while (!fila.isEmpty()) {
                    Cidade v = fila.poll();
                    int currentLevel = nivel.get(v);
                    l.put(v, t);
                    t++;
                    visitados.add(v); // Marca a cidade como visitada
    
                    for (Estrada estrada : arestas) {
                        if (estrada.getOrigem().equals(v)) {
                            Cidade w = estrada.getDestino();
                            if (!visitados.contains(w) && !fila.contains(w)) {
                                // Visitar aresta pai {v, w}
                                pai.put(w, v);
                                nivel.put(w, currentLevel + 1);
                                fila.add(w);
                                String aresta = v.getNome() + " -> " + w.getNome();
                                if (!cidadesDestino.contains(w.getNome())) {
                                    resultado.add(aresta); // Adicione o resultado à lista se o nome da cidade destino 
                                    cidadesDestino.add(w.getNome()); // Adicione o nome da cidade destino ao resultado
                                }
                            }
                        }
                    }
                }
            }
        }
    
        return resultado;
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
