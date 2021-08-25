package test;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.ufes.junitods.MetodoIMC;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jopendocument.dom.spreadsheet.MutableCell;
import org.jopendocument.dom.spreadsheet.Sheet;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import org.junit.runners.Parameterized;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class IMCParametrizadoTestCsv {

    //atributos lidos do arquivo
    String casoTeste;
    char sexo;
    Double peso;
    Double altura;
    Double imc;
    String classificao;
    String falha;
    int linha;

    private static final String STR_FILE = "src\\test\\resources\\casosDeTeste.csv";
    private static final String STR_DESTINYFILE = "src\\test\\resources\\casosDeTesteCriado.csv";

    //classe a ser testada
    private MetodoIMC metodoIMC;

    //variaveis para ler do arquivo ods
    private static File file;

    
    private BufferedWriter bfWriterCsv(BufferedWriter bw) throws IOException{
        if (bw == null) {
            return new BufferedWriter(new FileWriter(STR_DESTINYFILE, true));
        }
        return bw;
    }
    
    
    //excecao esperada
    @Rule
    public ExpectedException excecaoEsperada = ExpectedException.none();

    /*watcher para verificar o teste falhou ou passou
     é necessário para setar a variavel failed como true or false
     pois essa váriavel é usada no TearDown para escrever no arquivo
     */
    
    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        public void failed(Throwable t, Description d) {
            String dado;
            BufferedWriter bw = null;

            if (falha.isEmpty()) {
            dado = casoTeste + "," 
                    + sexo + ","
                    + peso + ","
                    + altura + ","
                    + imc + ","
                    + classificao + "," + "" + ","
                    + metodoIMC.getImc() + ","
                    + metodoIMC.getCondicao() + "," 
                    + "F" + "," + t.getMessage();
            } else {
            dado = casoTeste + ","
                    + sexo + ","
                    + peso + ","
                    + altura + ","
                    + imc + ","
                    + classificao + ","
                    + falha + ","
                    + metodoIMC.getImc() + ","
                    + metodoIMC.getCondicao() + "," 
                    + "P";
            }

            try (BufferedWriter by = bfWriterCsv(bw)) {
                by.write(dado);
                by.newLine();
            } catch (Exception e) {
                e.getStackTrace();
            }

        }

        @Override
        public void succeeded(Description d) {
            String dado;
            BufferedWriter bw = null;
            
            if (falha.isEmpty()) {
            dado = casoTeste + "," 
                    + sexo + ","
                    + peso + ","
                    + altura + ","
                    + imc + ","
                    + classificao + "," + "" + ","
                    + metodoIMC.getImc() + ","
                    + metodoIMC.getCondicao();
        } else {
            dado = casoTeste + ","
                    + sexo + ","
                    + peso + ","
                    + altura + ","
                    + imc + ","
                    + classificao + ","
                    + falha + ","
                    + metodoIMC.getImc() + ","
                    + metodoIMC.getCondicao();
        }
            
            try (BufferedWriter bfw = bfWriterCsv(bw)) {
                bfw.write(dado);
                bfw.newLine();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

    };

    public IMCParametrizadoTestCsv(String casoTeste, char sexo, Double peso, Double altura,
            Double imc, String classificao, String falha, int linha) {
        this.casoTeste = casoTeste;
        this.sexo = sexo;
        this.peso = peso;
        this.altura = altura;
        this.imc = imc;
        this.classificao = classificao;
        this.falha = falha;
        this.linha = linha;
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void testIMC() {

        metodoIMC = new MetodoIMC();

        //verifica se não há uma falha esperada
        if (!falha.isEmpty()) {
            excecaoEsperada.expect(RuntimeException.class);
            excecaoEsperada.expectMessage(falha);
        }

        metodoIMC.fazerCalculo(peso, altura);
        metodoIMC.classificar(sexo);

        assertEquals(metodoIMC.getImc(), imc, 0.1);
        assertEquals(metodoIMC.getCondicao(), classificao);
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> getData() {
        //array de dados a ser retornado
        ArrayList<Object[]> dados = new ArrayList();
        List<List<String>> linhas = new ArrayList<List<String>>();
        String[] colunas = null;
        try {

            CSVReader csvReader = new CSVReader(new FileReader(new File(STR_FILE)));
            int lineNumber = 1;

            while ((colunas = csvReader.readNext()) != null) {
                linhas.add(Arrays.asList(colunas));
                if (lineNumber == 1) {
                    lineNumber++;
                } else {
                    String casoTeste = colunas[0];
                    char sexo = StringToOherConverter.getInstance().getChar(colunas[1]);

                    Double peso;
                    if ("".equals(colunas[2])) {
                        peso = null;
                    } else {
                        peso = StringToOherConverter.getInstance().getDouble(colunas[2]);
                    }

                    Double altura;
                    if ("".equals(colunas[3])) {
                        altura = null;
                    } else {
                        altura = StringToOherConverter.getInstance().getDouble(colunas[3]);
                    }

                    Double imc;
                    if ("".equals(colunas[4])) {
                        imc = null;
                    } else {
                        imc = StringToOherConverter.getInstance().getDouble(colunas[4]);
                    }

                    String classificao = colunas[5];
                    String falha = colunas[6];

                    dados.add(new Object[]{casoTeste, sexo, peso, altura, imc, classificao, falha, lineNumber});

                    lineNumber++;
                }
            }
            

        } catch (IOException ex) {
            Logger.getLogger(IMCParametrizadoTestCsv.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dados;
    }

}
