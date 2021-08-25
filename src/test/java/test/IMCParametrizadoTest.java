package test;

import com.ufes.junitods.MetodoIMC;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jopendocument.dom.spreadsheet.MutableCell;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
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
public class IMCParametrizadoTest {

    //atributos lidos do arquivo
    String casoTeste;
    char sexo;
    Double peso;
    Double altura;
    Double imc;
    String classificao;
    String falha;
    int linha;

    //classe a ser testada
    private MetodoIMC metodoIMC;

    //variaveis para ler do arquivo ods
    private static File file;
    private static Sheet sheet;

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
            if (falha.isEmpty()) {
                sheet.getCellAt("H" + linha).setValue(metodoIMC.getImc());
                sheet.getCellAt("I" + linha).setValue(metodoIMC.getCondicao());
            }

            MutableCell cell = sheet.getCellAt("J" + linha);
            cell.setBackgroundColor(Color.red);
            cell.setValue("F");

            MutableCell mensagemFalha = sheet.getCellAt("K" + linha);
            mensagemFalha.setValue(t.getMessage());

        }

        @Override
        public void succeeded(Description d) {
            if (falha.isEmpty()) {
                sheet.getCellAt("H" + linha).setValue(metodoIMC.getImc());
                sheet.getCellAt("I" + linha).setValue(metodoIMC.getCondicao());
            }

            MutableCell cell = sheet.getCellAt("J" + linha);
            cell.setBackgroundColor(Color.green);
            cell.setValue("P");

            MutableCell mensagemFalha = sheet.getCellAt("K" + linha);
            mensagemFalha.clearValue();

        }
    };

    public IMCParametrizadoTest(String casoTeste, char sexo, Double peso, Double altura,
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
        //salvando as alterações na planilha
        try {
            sheet.getSpreadSheet().saveAs(file);
        } catch (IOException ex) {
            Logger.getLogger(IMCParametrizadoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {

            //abrindo o arquivo
            file = new File("src/test/resources/casosDeTeste.ods");
            sheet = SpreadSheet.createFromFile(file).getSheet(0);

            int notEmptyRows = getRows(sheet);

            // percorrendo as linhas
            for (int i = 2; i < notEmptyRows; i++) {
                String casoTeste = sheet.getCellAt("A" + i).getValue().toString();
                char sexo = StringToOherConverter.getInstance().getChar(sheet.getCellAt("B" + i).getValue().toString());
                Double peso;

                if ("null".equals(sheet.getCellAt("C" + i).getValue().toString())) {
                    peso = null;
                } else {
                    peso = StringToOherConverter.getInstance().getDouble(sheet.getCellAt("C" + i).getValue().toString());
                }

                Double altura = StringToOherConverter.getInstance().getDouble(sheet.getCellAt("D" + i).getValue().toString());
                Double imc = StringToOherConverter.getInstance().getDouble(sheet.getCellAt("E" + i).getValue().toString());
                String classificao = sheet.getCellAt("F" + i).getValue().toString();
                String falha = sheet.getCellAt("G" + i).getValue().toString();

                dados.add(new Object[]{casoTeste, sexo, peso, altura, imc, classificao, falha, i});
                //System.out.println(casoTeste + " - " + sexo + " - " + peso + " - " + altura + " - " + imc + " - " + classificao + " - " + falha);
            }
        } catch (IOException ex) {
            Logger.getLogger(IMCParametrizadoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dados;
    }

    // obtendo a quantidade de linhas
    private static int getRows(Sheet sheet) {

        int maxRows = sheet.getRowCount() + 1;
        int notEmptyRows = 0;
        for (int i = 2; i < maxRows; i++) {
            String casoTeste = sheet.getCellAt("A" + i).getValue().toString();
            if (casoTeste.isBlank()) {
                notEmptyRows = i;
                break;
            }
        }
        return notEmptyRows;
    }

}
