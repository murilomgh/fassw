/*
 * Copyright (C) 2013 Murilo Honorio
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fassw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.wsmo.common.exception.InvalidModelException;

/**
 *
 * @author Murilo Honorio
 */
public class GroundingCoreografiaTest {

    Scanner sc;

    public GroundingCoreografiaTest() {
    }

    /**
     * Cria o objeto GroundingCoreografia e invoca o metodo gerar, que gera um documento WSML a
     * partir do documento WSDL fornecido como entrada.
     *
     * @throws FileNotFoundException
     */
    @BeforeClass
    public static void setUpClass() throws FileNotFoundException, UnsupportedEncodingException, IOException, InvalidModelException {
        GroundingCoreografia gc = new GroundingCoreografia();
        gc.gerar();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Inicializa o Scanner para cada um dos testes.
     * 
     * @throws FileNotFoundException 
     */
    @Before
    public void setUp() throws FileNotFoundException {
        sc = new Scanner(new File("servico.wsml"));
    }

    @After
    public void tearDown() {
    }

    @Test
    public void deveDefinirVarianteRule() {
        String esperado = "wsmlVariant _\"http://www.wsmo.org/wsml/wsml-syntax/wsml-rule\"";
        String obtido = obterLinha(1);
        assertEquals(esperado, obtido);
    }

    @Test
    public void deveDefinirNamespaceDefaultIgualAoWSDLeServiceName() { 
        String esperado = "namespace { _\"http://example.org/myLastFmSoapServer/myLastFmSoapServer#\"";
        String obtido = obterLinha(2);
        assertEquals(esperado, obtido);
    }
    
    @Test
    public void deveReferenciarOntologiasDeDadosCriadas() {
        String esperado = "     onto1 _\"http://example.org/myLastFmSoapServer#\" }";
        String obtido = obterLinha(6);
        assertEquals(esperado, obtido);
    }
    
    @Test
    public void deveCriarWebservice() {
        String esperado = "webService _\"http://example.org/myLastFmSoapServer/myLastFmSoapServer\"";
        String obtido = obterLinha(9);
        assertEquals(esperado, obtido);
    }
    
    @Test
    public void deveCriarNFPcontendoOutputOperations() {
        String esperado = "          _\"http://response\" hasValue {onto1#findEventsResponse, onto1#findEventsByVenueResponse}";
        String obtido = obterLinha(11);
        assertEquals(esperado, obtido);
    }
    
    @Test
    public void deveDeclararCapacidade() {
        String esperado = "capability myLastFmSoapServerCapability";
        String obtido = obterLinha(16);
        assertEquals(esperado, obtido);
    }
    
    @Test
    public void capacidadeDeveImportarOntologia() {
        String esperado1 = "     importsOntology";
        String obtido1 = obterLinha(18);
        String esperado2 = "            _\"http://example.org/myLastFmSoapServer\"";
        String obtido2 = obterLinha(1); //TODO corrigir comportamento do obterLinha()
        assertEquals(esperado1, obtido1);
        assertEquals(esperado2, obtido2);
    }
    
    @Test
    public void deveDeclararSharedVariables() {
        String esperado = "sharedVariables {?request1, ?request2}";
        String obtido = obterLinha(21);
        assertEquals(esperado, obtido);
    }
    
    @Test
    public void deveDeclararPrecondicao() {
        String esperado = "sharedVariables {?request1, ?request2}";
        String obtido = obterLinha(23);
        assertEquals(esperado, obtido);
    }
    
    
    /**
     * Retorna a linha da posicao indicada.
     * 
     * @param numeroDaLinha
     * @return linha na posicao indicada
     */
    private String obterLinha(int numeroDaLinha) {
        int contar = 1;
        String linhaObtida = null;

        while (sc.hasNext()) {
            String linha = sc.nextLine();
            if (contar == numeroDaLinha) {
                linhaObtida = linha;
                break;
            }
            contar++;
        }
        return linhaObtida;
    }
}
