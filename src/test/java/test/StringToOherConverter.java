package test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author clayton
 */
public final class StringToOherConverter {

    private static StringToOherConverter instance;

    private StringToOherConverter() {

    }

    public static StringToOherConverter getInstance() {
        if (instance == null) {
            instance = new StringToOherConverter();
        }
        return instance;
    }

    /**
     * Convert String to Char
     *
     * @param str
     * @return
     */
    public char getChar(String str) {
        if (str.isEmpty()) {
            return '\0';
        }
        return str.charAt(0);
    }

    public Float getFloat(String str) {
        Float numero;
        try {
            numero = Float.parseFloat(str);
        } catch (NumberFormatException ex) {
            numero = Float.NaN;
        }
        return numero;
    }

    /**
     *
     * @param str
     * @param delimiter Enter with a character delimiter
     * @return
     */
    public Collection<Double> getDoubleCollection(String str, char delimiter) {
        str = str.trim();
        ArrayList<Double> colecao;
        try {
            colecao = new ArrayList(Arrays.asList(stringArrayToDouble(str, delimiter)));
        } catch (NumberFormatException ex) {
            throw new NumberFormatException(ex.getMessage());
        }
        return colecao;
    }

    public Collection<Float> getFloatCollection(String str, char delimiter) {
        str = str.trim();
        ArrayList<Float> colecao;
        try {
            colecao = new ArrayList(Arrays.asList(stringArrayToFloat(str, delimiter)));
        } catch (NumberFormatException ex) {
            throw new NumberFormatException(ex.getMessage());
        }
        return colecao;
    }

    public Collection<Integer> getIntegerCollection(String str, char delimiter) {
        str = str.trim();
        ArrayList<Integer> colecao;
        try {
            colecao = new ArrayList(Arrays.asList(stringArrayToInteger(str, delimiter)));
        } catch (NumberFormatException ex) {
            throw new NumberFormatException(ex.getMessage());
        }
        return colecao;
    }

    public Collection<String> getStringCollection(String str, char delimiter) {
        ArrayList<String> colecao;
        try {
            str = str.trim();
            if (str.isEmpty()) {
                throw new Exception("Vetor vazio");
            } else if (str.endsWith(Character.toString(delimiter))) {
                throw new Exception("Vetor mal definido");
            }
            colecao = new ArrayList(Arrays.asList(stringArrayToString(str, delimiter)));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return colecao;
    }

    private Double[] stringArrayToDouble(String str, char delimiter) {
        String[] terms = str.split(String.valueOf(delimiter));
        Double[] sequence = new Double[terms.length];
        for (int i = 0; i < terms.length; i++) {
            sequence[i] = Double.parseDouble(terms[i].replaceAll("^\\s+", ""));
        }
        return sequence;
    }

    private Float[] stringArrayToFloat(String str, char delimiter) {
        String[] terms = str.split(String.valueOf(delimiter));
        Float[] sequence = new Float[terms.length];
        for (int i = 0; i < terms.length; i++) {
            sequence[i] = Float.parseFloat(terms[i].replaceAll("^\\s+", ""));
        }
        return sequence;
    }

    private String[] stringArrayToString(String str, char delimiter) {
        String[] terms = str.split(String.valueOf(delimiter));
        String[] sequence = new String[terms.length];
        for (int i = 0; i < terms.length; i++) {
            sequence[i] = terms[i].replaceAll("^\\s+", "");
        }
        return sequence;
    }

    private Integer[] stringArrayToInteger(String str, char delimiter) {
        String[] terms = str.split(String.valueOf(delimiter));
        Integer[] sequence = new Integer[terms.length];
        for (int i = 0; i < terms.length; i++) {
            sequence[i] = Integer.parseInt(terms[i].replaceAll("^\\s+", ""));
        }
        return sequence;
    }

    public Byte getByte(String str) {
        Byte numero;
        try {
            numero = Byte.valueOf(str);
        } catch (NumberFormatException ex) {
            throw new NumberFormatException(ex.getMessage());
        }
        return numero;
    }

    public Boolean getBoolean(String str) {
        Boolean booleano;
        try {
            if (str.toLowerCase().equals("true") || str.toLowerCase().equals("false")) {
                booleano = Boolean.parseBoolean(str.toLowerCase());
            } else {
                throw new Exception();
            }
        } catch (Exception ex) {
            throw new RuntimeException("Falha ao converter " + str + " para Boolean (utilize true ou false)\n" + ex.getMessage());
        }
        return booleano;
    }

    public Double getDouble(String str) {
        try {
            Double numero = Double.parseDouble(str);
            return numero;
        } catch (NumberFormatException ex) {
            return Double.NaN;
        }
    }

    public int getInt(String str) {
        try {
            int numero = Integer.parseInt(str);
            return numero;
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Falha ao converter " + str + " para int\n" + ex.getMessage());
        }
    }

}
