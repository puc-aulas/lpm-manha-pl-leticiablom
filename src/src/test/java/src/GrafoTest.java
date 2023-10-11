package src;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class GrafoTest {

    // Teste unitário do requisito (b)

    @Test
    public void testTodasCidadesConectadas() {
        Grafo grafo = new Grafo();

        // Cidades e estradas do grafo

        Cidade cidadeA = new Cidade(1, "A");
        Cidade cidadeB = new Cidade(2, "B");
        Cidade cidadeC = new Cidade(3, "C");

        grafo.adicionarCidade(cidadeA);
        grafo.adicionarCidade(cidadeB);
        grafo.adicionarCidade(cidadeC);

        grafo.adicionarEstrada(new Estrada(cidadeA, cidadeB, 100));
        grafo.adicionarEstrada(new Estrada(cidadeB, cidadeC, 150));

        assertTrue(grafo.todasCidadesConectadas());
    }

    // Teste unitário do requisito (b)

    @Test
    public void testCidadesNaoConectadas() {
        Grafo grafo = new Grafo();

        // Cidades e estradas do grafo

        Cidade cidadeX = new Cidade(4, "X");
        Cidade cidadeY = new Cidade(5, "Y");
        Cidade cidadeZ = new Cidade(6, "Z");

        grafo.adicionarCidade(cidadeX);
        grafo.adicionarCidade(cidadeY);
        grafo.adicionarCidade(cidadeZ);
        
        assertFalse(grafo.todasCidadesConectadas());
    }

        // Teste unitário do requisito (c)
        
    @Test
    public void testIdentificarCidadesIsoladas() {
        Grafo grafo = new Grafo();

        // Cidades e estradas do grafo
        
        Cidade cidadeX = new Cidade(4, "X");
        Cidade cidadeY = new Cidade(5, "Y");
        Cidade cidadeZ = new Cidade(6, "Z");

        grafo.adicionarCidade(cidadeX);
        grafo.adicionarCidade(cidadeY);
        grafo.adicionarCidade(cidadeZ);

        
        grafo.adicionarEstrada(new Estrada(cidadeX, cidadeZ, 130));
        grafo.adicionarEstrada(new Estrada(cidadeZ, cidadeX, 130));

        // Cidade isolada -> Y
        grafo.adicionarEstrada(new Estrada(cidadeY, cidadeZ, 100));
        grafo.adicionarEstrada(new Estrada(cidadeY, cidadeX, 120));

        List<String> cidadesIsoladas = grafo.identificarCidadesIsoladas();
        
        assertTrue(cidadesIsoladas.contains("Y"));

        assertFalse(cidadesIsoladas.contains("X"));
        assertFalse(cidadesIsoladas.contains("Z"));
    }
}
