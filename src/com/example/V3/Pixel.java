package com.example.V3;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Salatiel
 */
public class Pixel {
    private int r;
    private int g;
    private int b;
    
    public Pixel(int r, int g, int b){
        setR(r);
        setG(g);
        setB(b);
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }
    
    public int [] getRGB(){
        int rgb[] = new int[3];
        rgb[0] = getR();
        rgb[1] = getG();
        rgb[2] = getB();
        return rgb;
    }
    
    public int getRGB2(){
        int col = (getR() << 16) | (getG() << 8) | (getB());
        return col;
    }
    
    public float getRf(){
        return this.r/255.f;
    }
    
    public float getGf(){
        return this.g/255.f;
    }
    
    public float getBf(){
        
        return this.b/255.f;
    }
}

