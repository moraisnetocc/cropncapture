/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.V3;

/**
 *
 * @author ele
 */
public class Ponto {
    public int[][] coords;
    private int dimension;

    public Ponto(int dimension){
        this.coords = new int[dimension][dimension];
    }
    
    
    public int getCoord(int i) {
        return coords[i][i];
    }

    public void setCoords(int[][] coord) {
        this.coords = coord;
    }

    public int getDimension() {
        return dimension;
    }

    public void setQtdDimensoes(int dimensoes) {
        this.dimension = dimensoes;
    }
    
    
}
