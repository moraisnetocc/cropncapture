package com.example.V3;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//package pdi;

//import V3.testePonto.Ponto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.*;


/**
 *
 * @author Salatiel
 */
public class Kmeans2 {
  //  private int k;
  //  private Pixel [][] pixelsRGB;          //Pixels da imagem a ser tratada
    private byte [][] groups;
    private double [][] prototype0 , prototype1;
    private Ponto prototypeA, prototypeB;
    private Ponto [][] groupsP;
    private Ponto [][] imagemP;
    private Bitmap imagem;
    private int dimension;
    private double [][]pixLum;
   // private Group [] groups;            //grupos da imagem
    Ponto p;


    public Kmeans2(Bitmap imagem, int dimension){
    	this.imagem = imagem;
        this.dimension = dimension;
        prototypeA = new Ponto(dimension);
        prototypeB = new Ponto(dimension);

        //Dividindo as dimensões da imagem por dimension, sabe-se quantos pontos uma imagem tem
        groupsP = new Ponto[imagem.getWidth()/dimension][imagem.getHeight()/dimension];
        imagemP = toPonto();
       
        //initGroupsP();
        //gerarProtosP();
        //gerarProtosMediana();
    }
    public Ponto[][] toPonto(){

        int width = Math.round(imagem.getWidth()/dimension);
        int height = Math.round(imagem.getHeight()/dimension);
        Ponto [][] pontos;
        pontos = new Ponto[width][height];
        int viz = dimension/2;
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height;j++){
               int a = Math.abs(i*dimension+viz);
                int b = Math.abs(j*dimension+viz);
                Ponto temp = new Ponto(dimension);
                //.out.println("a,b " + a + "," + b);
                for(int k = a, q = 0; k < (a+dimension); k++, q++){
                    for(int w = b, r=0; w < (b+dimension); w++, r++){
                        temp.coords[q][r] = Color.green(imagem.getPixel(k-viz, w-viz));
                    }
                }
                pontos[i][j] = temp;
            }
        }
        return pontos;
    }
    public void runKmeans(){
        boolean found;

        do{
            found = false;

            for(int i = 1; i < groups.length-1; i++){
                for(int j = 1; j < groups[0].length-1; j++){
                    double d0 = euclidianDistance(i, j, 0);
                    double d1 = euclidianDistance(i, j, 1);
                    
                   

                    if(d0 <= d1 && groups[i][j] != 0){
                    //     System.out.println("d0 = "+ d0 + " d1= "+d1);
                        groups[i][j] = 0;
                        gerarProtos();
                      //  printGroups();
                        found = true;
                    }
                    if(d1 < d0 && groups[i][j] != 1){
                        groups[i][j] = 1;
                        gerarProtos();
                     //   printGroups();
                        found = true;
                    }
                }
            }
        }while(found);
        for(int i = 0; i < groups.length; i++){
            for(int j = 0; j < groups[0].length; j++){
                imagem.setPixel(i,j,Color.rgb(255*groups[i][j], 255*groups[i][j], 255*groups[i][j]));
            }
        }
    }

    public void runKmeansEuclidian(){
        boolean found;
        do{
            found = false;
            for(int i = 0; i < imagemP.length; i++){
                for(int j = 0; j < imagemP[0].length; j++){
                    double d0 = euclidianDistanceP(i,j,0);
                    double d1 = euclidianDistanceP(i,j,1);
                    //System.out.println(d0 + " " + d1);
                    if(d0 <= d1 && groupsP[i][j].coords[0][0] != 0){
                        //Alterar grupo
                        for(int k = 0; k < dimension;k++){
                            for(int w = 0; w < dimension; w++){
                                groupsP[i][j].coords[k][w] = 0;
                            }
                        }
                        
                        gerarProtosMedia();
                        found = true;
                    }
                    else if(d1 < d0 && groupsP[i][j].coords[0][0]!= 1){
                        //Alterar grupo
                        for(int k = 0; k < dimension;k++){
                            for(int w = 0; w < dimension; w++){
                                groupsP[i][j].coords[k][w] = 1;
                            }
                        }
                        
                        gerarProtosMedia();
                        found = true;
                    }
                }
            }
        }while(found);
        
        //Transformando matriz de Pontos em matriz de Pixels
        int viz = dimension/2;
        for(int i = 0; i < imagemP.length; i++){
            for(int j = 0; j < imagemP[0].length;j++){
                int a = Math.abs(i*dimension+viz);
                int b = Math.abs(j*dimension+viz);
                //.out.println("a,b " + a + "," + b);
                for(int k = a, q = 0; k < (a+dimension); k++, q++){
                    for(int w = b, r=0; w < (b+dimension); w++, r++){
                        imagem.setPixel((k-viz),(w-viz), Color.rgb(255*groupsP[i][j].coords[q][r], 255*groupsP[i][j].coords[q][r], 255*groupsP[i][j].coords[q][r]));
                    }
                    //System.out.println(" ab " + a + " " + b);
                }
            }
        }
    }
    
    public void runKmeansChebyshev(){
        boolean found;
        do{
            found = false;
            for(int i = 0; i < imagemP.length; i++){
                for(int j = 0; j < imagemP[0].length; j++){
                    double d0 = chebyshevDistanceP(i,j,0);
                    double d1 = chebyshevDistanceP(i,j,1);
                    //System.out.println(d0 + " " + d1);
                    if(d0 <= d1 && groupsP[i][j].coords[0][0] != 0){
                        //Alterar grupo
                        for(int k = 0; k < dimension;k++){
                            for(int w = 0; w < dimension; w++){
                                groupsP[i][j].coords[k][w] = 0;
                            }
                        }
                        
                        gerarProtosMediana();
                        found = true;
                    }
                    else if(d1 < d0 && groupsP[i][j].coords[0][0]!= 1){
                        //Alterar grupo
                        for(int k = 0; k < dimension;k++){
                            for(int w = 0; w < dimension; w++){
                                groupsP[i][j].coords[k][w] = 1;
                            }
                        }
                        
                        gerarProtosMediana();
                        found = true;
                    }
                }
            }
        }while(found);
        
        //Transformando matriz de Pontos em matriz de Pixels
        int viz = dimension/2;
        for(int i = 0; i < imagemP.length; i++){
            for(int j = 0; j < imagemP[0].length;j++){
                int a = Math.abs(i*dimension+viz);
                int b = Math.abs(j*dimension+viz);
                //.out.println("a,b " + a + "," + b);
                for(int k = a, q = 0; k < (a+dimension); k++, q++){
                    for(int w = b, r=0; w < (b+dimension); w++, r++){
                        imagem.setPixel((k-viz),(w-viz), Color.rgb(255*groupsP[i][j].coords[q][r], 255*groupsP[i][j].coords[q][r], 255*groupsP[i][j].coords[q][r]));
                    }
                    //System.out.println(" ab " + a + " " + b);
                }
            }
        }
    }
    public void gerarProtos(){
        int c0[][] = {{0,0,0},{0,0,0},{0,0,0}};
        int c1[][] = {{0,0,0},{0,0,0},{0,0,0}};
      
       for(int k = -1; k <= 1; k++){
        for(int l = -1; l <= 1; l++){
             prototype0[k+1][l+1] = 0;
              prototype1[k+1][l+1] = 0;
        }
       }
        for(int i = 1; i < groups.length - 1; i++){
            for(int j = 1 ; j < groups[0].length - 1; j++){
                for(int k = -1; k <= 1; k++){
                    for(int l = -1; l <= 1; l++){
                        if(groups[i][j] == 0){
                            prototype0[k+1][l+1] += pixLum[i+k][j+l];// utilizar cruminancia e luminancia
                            c0[k+1][l+1]++;
                        }
                        else{
                            prototype1[k+1][l+1] += pixLum[i+k][j+l]; // utilizar cruminancia e luminancia
                            c1[k+1][l+1]++;
                        }
                    }
                }
            }
        }
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                prototype0[i][j] /= c0[i][j];
                prototype1[i][j] /= c1[i][j];   
            }
        }
    }
    
    
    public void gerarProtosMedia(){
        Ponto c0 = new Ponto(dimension);
        Ponto c1 = new Ponto(dimension);
        
        //Zerar prototipos a cada chamada do método
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                prototypeA.coords[i][j] = 0;
                prototypeB.coords[i][j] = 0;
            }
        }
        
        for(int i = 0; i < groupsP.length; i++){
            for(int j = 0; j < groupsP[0].length; j++){
                //Caso o primeiro pixel do ponto seja do grupo zero, atualizar o prototipoA senão atualizar prototipoB
                if(groupsP[i][j].coords[0][0] == 0){
                    for(int k = 0; k < dimension; k++){
                        for(int w = 0; w < dimension; w++){
                            prototypeA.coords[k][w] += imagemP[i][j].coords[k][w];
                            c0.coords[k][w]++;
                        }
                    }
                }else{
                    for(int k = 0; k < dimension; k++){
                        for(int w = 0; w < dimension; w++){
                            prototypeB.coords[k][w] += imagemP[i][j].coords[k][w];
                            c1.coords[k][w]++;
                        }
                    }
                }
            }
        }
        
        for(int i = 0; i < dimension; i++){
            for(int j = 0 ; j < dimension; j++){
                prototypeA.coords[i][j] /= c0.coords[i][j];
                prototypeB.coords[i][j] /= c1.coords[i][j];
            }
        }
    }
    
    public void gerarProtosMediana(){
        List<Integer> c0 = new ArrayList<Integer>();
        List<Integer> c1 = new ArrayList<Integer>();
        
        //Zerar prototipos a cada chamada do método
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                prototypeA.coords[i][j] = 0;
                prototypeB.coords[i][j] = 0;
            }
        }
        
        for(int i = 0; i < groupsP.length; i++){
            for(int j = 0; j < groupsP[0].length; j++){
                //Caso o primeiro pixel do ponto seja do grupo zero, atualizar o prototipoA senão atualizar prototipoB
                if(groupsP[i][j].coords[0][0] == 0){
                    for(int k = 0; k < dimension; k++){
                        for(int w = 0; w < dimension; w++){
                            c0.add(imagemP[i][j].coords[k][w]);
                        }
                    }
                }else{
                    for(int k = 0; k < dimension; k++){
                        for(int w = 0; w < dimension; w++){
                            c1.add(imagemP[i][j].coords[k][w]);
                        }
                    }
                }
            }
        }
        
        Collections.sort(c0);
        Collections.sort(c1);
        
        int a; 
        try{
            a = c0.get(c0.size()/2);
        }catch(Exception e){
            a = 0;
        }
        int b;
        try{
            b = c1.get(c1.size()/2);
        }catch(Exception e){
            b = 0;
        }
        
        for(int k = 0; k < dimension; k++){
            for(int w = 0; w < dimension; w++){
                prototypeA.coords[k][w] = a;
                prototypeB.coords[k][w] = b;
            }
        }
    }
    
    void printGroups(){
          for(int i = 1; i < groups.length-1; i++){
            for(int j = 1; j < groups[0].length-1; j++){
                System.out.print(groups[i][j] + " ");   
            }
        }
          System.out.println();
    }
     
    public void initGroups(){
        for(int i = 0; i < groups.length; i++){
            for(int j = 0; j < groups[0].length; j++){
                groups[i][j] = (byte)Math.round(Math.random());
            }
        }
    }
    
    public void initGroupsP(){
        for(int i = 0; i < groupsP.length; i++){
            for(int j = 0; j < groupsP[0].length; j++){
                Ponto p = new Ponto(dimension);
                int group = (byte)Math.round(Math.random());    //grupo 0 ou 1
                for(int w = 0; w < dimension; w++){
                    for(int k = 0; k < dimension; k++){
                        p.coords[w][k] = group;
                    }
                }
                groupsP[i][j] = p;
            }
        }
    }
    
    public double Distance(int x1, int y1, int group){
        /*
        Implementação da distancia euclidiana d = raiz((x1-x2)² + (y1-y2)²)
        */
        double sum = 0;
        if(group == 0){
            for(int i = -1; i <= 1; i++){
                for(int j = -1; j <= 1; j++){
                    sum+=Math.abs((pixLum[x1+i][y1+j] - prototype0[i+1][j+1]));
                }
            }   
        }
        else{
            for(int i = -1; i <= 1; i++){
                for(int j = -1; j <= 1; j++){
                    sum+=Math.abs((pixLum[x1+i][y1+j] - prototype1[i+1][j+1]));
                }
            }   
        }        
        return (sum);
    }
    
    public double euclidianDistanceP(int x, int y, int group){
      double sum = 0;
      if(group == 0 ){
          for(int i = 0; i < dimension; i++){
              for(int j = 0; j < dimension; j++){
                  sum+=Math.abs((imagemP[x][y].coords[i][j] - prototypeA.coords[i][j]));
              }
          }
      }
      else{
          for(int i = 0; i < dimension; i++){
              for(int j = 0; j < dimension; j++){
                  sum+=Math.abs((imagemP[x][y].coords[i][j] - prototypeB.coords[i][j]));
              }
          }
      }
      return sum;
    }
    
    public double euclidianDistance(int x1, int y1, int group){
        /*
        Implementação da distancia euclidiana d = raiz((x1-x2)² + (y1-y2)²)
        */
        double sum = 0;
        if(group == 0){
            for(int i = -1; i <= 1; i++){
                for(int j = -1; j <= 1; j++){
                    sum+=Math.abs((pixLum[x1+i][y1+j] - prototype0[i+1][j+1]));
                }
            }   
        }
        else{
            for(int i = -1; i <= 1; i++){
                for(int j = -1; j <= 1; j++){
                    sum+=Math.abs((pixLum[x1+i][y1+j] - prototype1[i+1][j+1]));
                }
            }   
        }        
        return (sum);
    }
   
    public double chebyshevDistanceP(int x, int y, int group){
        double result = 0;
        Ponto p = new Ponto(dimension);
        if(group == 0){
            for(int i = 0; i < dimension; i++){
                for(int j = 0; j < dimension; j++){
                    p.coords[i][j] = Math.abs((imagemP[x][y].coords[i][j] - prototypeA.coords[i][j]));
                }
            }
            double max = p.coords[0][0];
            for(int i = 0; i < dimension; i++){
                for(int j = 0; j < dimension; j++){
                    if(p.coords[i][j] > max){
                        max = p.coords[i][j];
                    }
                }
            }
            result = max;
        }else{
            for(int i = 0; i < dimension; i++){
                for(int j = 0; j < dimension; j++){
                    p.coords[i][j] = Math.abs((imagemP[x][y].coords[i][j] - prototypeB.coords[i][j]));
                }
            }
            double max = p.coords[0][0];
            for(int i = 0; i < dimension; i++){
                for(int j = 0; j < dimension; j++){
                    if(p.coords[i][j] > max){
                        max = p.coords[i][j];
                    }
                }
            }
            result = max;
        }
        return result;
    }
    public Bitmap getImagem() {
        return imagem;
    }
   /* public void setLum(){
        Pixel [][]pixels = imagem.getPixels();
        for(int k = 0; k < imagem.getWidth(); k++)
            for(int j = 0; j < imagem.getHeight(); j++)
            {
                pixLum[k][j] = 0.299*pixels[k][j].getR() + 0.587*pixels[k][j].getG() +  0.114*pixels[k][j].getB();
                pixLum[k][j] /= 255.0;
            }
        
        
        
    }*/
    
  /*  public void setK(int k){
        this.k = k;
    }
    
    public int getK(){
        return this.k;
    }*/

    /*public Pixel[][] getPixels() {
        return pixelsRGB;
    }

    public void setPixels(Pixel[][] pixels) {
        this.pixelsRGB = pixels;
    }*/

    public byte[][] getGroups() {
        return groups;
    }

    public void setGroups(byte[][] groups) {
        this.groups = groups;
    }
    /*
    public static void main(String args[]) throws Exception{
       Kmeans2 k = new Kmeans2("fig1.png", 5);
       k.initGroupsP();
       k.gerarProtosMediana();
       k.runKmeansChebyshev();
       //k.gerarProtosMedia();
       //k.runKmeansEuclidian();
        //k.printGroups();

    }*/
}
