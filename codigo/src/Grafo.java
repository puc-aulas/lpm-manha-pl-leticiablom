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

    public boolean todasCidadesConectadas() {
        Set<Cidade> visitados = new HashSet<>();
        dfs(vertices.get(0), visitados);
        return visitados.size() == vertices.size();
    }

    private void dfs(Cidade cidadeAtual, Set<Cidade> visitados) {
        visitados.add(cidadeAtual);
        for (Estrada estrada : arestas) {
            if (estrada.getOrigem().equals(cidadeAtual) && !visitados.contains(estrada.getDestino())) {
                dfs(estrada.getDestino(), visitados);
            } else if (estrada.getDestino().equals(cidadeAtual) && !visitados.contains(estrada.getOrigem())) {
                dfs(estrada.getOrigem(), visitados);
            }
        }
    }






 public List<String> identificarCidadesIsoladas() {
        List<String> cidadesOrigem = new ArrayList<>();
        List<String> cidadesDestino = new ArrayList<>();
        List<String> cidadesIsoladas = new ArrayList<>();

        // Preencha a lista de cidadesOrigem e cidadesDestino com base nas estradas
        for (Estrada estrada : arestas) {
            cidadesOrigem.add(estrada.getOrigem().getNome());
            cidadesDestino.add(estrada.getDestino().getNome());
        }

        // Verifique se alguma cidadeOrigem não está em cidadeDestino
        for (String cidade : cidadesOrigem) {
            if (!cidadesDestino.contains(cidade) && !cidadesIsoladas.contains(cidade)) {
                cidadesIsoladas.add(cidade);
            }
        }

        // Imprima as cidades isoladas ou uma mensagem se não houver nenhuma
        if (!cidadesIsoladas.isEmpty()) {
            System.out.println("Cidades Isoladas:");
            for (String cidadeIsolada : cidadesIsoladas) {
                System.out.println(cidadeIsolada);
            }
        } else {
            System.out.println("Não existem cidades isoladas.");
        }

        return cidadesIsoladas;
    }





public List<Cidade> encontrarMenorRota() {
    List<Cidade> melhorRota = null;
    List<Cidade> todasCidades = new ArrayList<>(vertices);
    todasCidades.remove(0); 

    int menorDistancia = Integer.MAX_VALUE;

    // Gere todas as permutações das cidades
    List<List<Cidade>> permutacoes = permutacoes(todasCidades);

    for (List<Cidade> rota : permutacoes) {
        rota.add(0, vertices.get(0)); // Adicione a rodoviária no início e final da rota
        rota.add(vertices.get(0));

        int distanciaTotal = calcularDistanciaTotal(rota);

        if (distanciaTotal < menorDistancia) {
            menorDistancia = distanciaTotal;
            melhorRota = rota;
        }
    }

    return melhorRota;
}

private int calcularDistanciaTotal(List<Cidade> rota) {
    int distanciaTotal = 0;
    for (int i = 0; i < rota.size() - 1; i++) {
        Cidade cidadeOrigem = rota.get(i);
        Cidade cidadeDestino = rota.get(i + 1);

        for (Estrada estrada : arestas) {
            if ((estrada.getOrigem().equals(cidadeOrigem) && estrada.getDestino().equals(cidadeDestino))
                    || (estrada.getOrigem().equals(cidadeDestino) && estrada.getDestino().equals(cidadeOrigem))) {
                distanciaTotal += estrada.getPeso();
                break;
            }
        }
    }
    return distanciaTotal;
}

private List<List<Cidade>> permutacoes(List<Cidade> cidades) {
    List<List<Cidade>> permutacoes = new ArrayList<>();
    permutacoesRecursivo(cidades, 0, permutacoes);
    return permutacoes;
}

private void permutacoesRecursivo(List<Cidade> cidades, int inicio, List<List<Cidade>> permutacoes) {
    if (inicio == cidades.size()) {
        permutacoes.add(new ArrayList<>(cidades));
    } else {
        for (int i = inicio; i < cidades.size(); i++) {
            Collections.swap(cidades, inicio, i);
            permutacoesRecursivo(cidades, inicio + 1, permutacoes);
            Collections.swap(cidades, inicio, i); // Restaura a ordem original
        }
    }
}
}
