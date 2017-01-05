/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.binarizao;

/**
 *
 * @author ele
 */
public class Ponto {
    public int[][] elements;
    private int dimension;
    public int min;
    public int max;

    public Ponto(int dimension){
        this.elements = new int[dimension][dimension];
    }
    
    
    public int getCoord(int i) {
        return elements[i][i];
    }

    public void setCoords(int[][] coord) {
        this.elements = coord;
    }

    public int getDimension() {
        return dimension;
    }

    public void setQtdDimensoes(int dimensoes) {
        this.dimension = dimensoes;
    }
    
    
}
