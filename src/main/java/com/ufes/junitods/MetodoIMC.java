package com.ufes.junitods;

import static java.lang.Math.pow;
import java.text.DecimalFormat;

public class MetodoIMC {

    private double imc = 0;
    private String condicao = null;

    public void fazerCalculo(Double peso, Double altura) {

        try {
            //verificando o peso
            if (peso <= 0 || Double.isNaN(peso) || peso == null) {
                throw new RuntimeException("Falha: O peso deve ser um número maior que zero");
            }
        } catch (Exception e) {
            throw new RuntimeException("Falha: O peso deve ser um número maior que zero");
        }

        try {
            //verificando a altura
            if (altura <= 0 || altura > 3 || Double.isNaN(altura) || peso == null) {
                throw new RuntimeException("Falha: A altura deve ser um número maior que zero e menor que 3m");
            }
        } catch (Exception e) {
            throw new RuntimeException("Falha: A altura deve ser um número maior que zero e menor que 3m");
        }

        //realizando o calculo
        this.imc = peso / (pow(altura, 2));
        DecimalFormat formatter = new DecimalFormat("#0.000");
        imc = Double.parseDouble(formatter.format(imc).replace(',', '.'));
    }

    public void classificar(char sexo) throws RuntimeException {
        double indice = getImc();
        switch (sexo) {
            case 'F':
                if (indice < 19.1) {
                    this.condicao = "Abaixo do peso";
                }
                if (indice >= 19.1 && indice < 25.8) {
                    this.condicao = "No peso normal";
                }
                if (25.8 <= indice && indice < 27.3) {
                    this.condicao = "Marginalmente acima do peso";
                }
                if (27.3 <= indice && indice < 32.3) {
                    this.condicao = "Acima do peso ideal";
                }
                if (indice > 32.3) {
                    this.condicao = "Obeso";
                }
                break;
            case 'M':
                if (indice < 20.7) {
                    this.condicao = "Abaixo do peso";
                }
                if (20.7 <= indice && indice < 26.4) {
                    this.condicao = "No peso normal";
                }
                if (26.4 <= indice && indice < 27.8) {
                    this.condicao = "Marginalmente acima do peso";
                }
                if (27.8 <= indice && indice < 31.1) {
                    this.condicao = "Acima do peso ideal";
                }
                if (indice > 31.1) {
                    this.condicao = "Obeso";
                }
                break;
            default:
                throw new RuntimeException("Falha: É necessário informar o sexo (M, F)");
        }

    }

    public double getImc() {
        return this.imc;
    }

    public String getCondicao() {
        return this.condicao;
    }

}
