/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example.binarizao;

import android.graphics.Color;
import java.util.Hashtable;
import android.graphics.Bitmap;

/**
 *
 * @author Salatiel
 */
public class Binarization {
    private Bitmap image;
    private Hashtable <Integer, Integer> map;
    private double prototypeA;
    private double prototypeB;
    private int [] histogram;

    public Binarization(Bitmap img){
        this.image = img;                               //Lendo imagem
        channelY();       							    //Passando imagem para escala de Cinza
        map = new Hashtable<Integer, Integer>();                    //Hashtable
        histogram = getHist();                                //Histograma
    }
    
    public Bitmap getImage(){
    	return image;
    }
    
    public int[] getHist(){    
        int [] rCount = new int[256];              // Contagem dos elementos r                                    
         for (int i = 0; i < image.getWidth(); i++){
             for (int j = 0; j < image.getHeight(); j++){ 
                 rCount[Color.red(image.getPixel(i, j))]++;
             }  
         }
         return rCount;
    }
    
    public void channelY(){
        double y;
        for(int k = 0; k < image.getWidth(); k++){
            for(int j = 0; j < image.getHeight(); j++){
                y = 0.299*Color.red(image.getPixel(k, j)) + 0.587*Color.green(image.getPixel(k, j)) +  0.114*Color.blue(image.getPixel(k, j));
                image.setPixel(k,j,Color.rgb((int)y, (int)y,(int)y));
            }
        }
    }
    public void initGroups(){
        for(int i = 0; i < 256; i++){
            int value = (byte)Math.round(Math.random());    //valor aleatorio
            map.put(i, value);                              //inserindo na hashtable
        }
    }

    public void gerarProtos(){
        double sumA = 0;
        double divA = 0;
        double sumB = 0;
        double divB = 0;

        for(int i = 0; i < 256; i++){
            if(map.get(i) == 0){
                sumA+= i*histogram[i];                      //valor*qtdDeElementos
                divA+=histogram[i];                         //pesos
            }
            else{
                sumB+= i*histogram[i];
                divB+=histogram[i];
            }
        }

        prototypeA = sumA/divA;
        prototypeB = sumB/divB;
    }

    public double L1Distance(int x, int y, int group){
        double distance;
        if(group == 0){
            distance = Math.abs(Color.green(image.getPixel(x, y)) - prototypeA);
        }
        else{
            distance = Math.abs(Color.green(image.getPixel(x, y)) - prototypeB);
        }
        return distance;
    }

    public double L1Distance2(int rgb, int group){
        double distance;
        if(group == 0){
            distance = Math.abs(rgb - prototypeA);
        }
        else{
            distance = Math.abs(rgb - prototypeB);
        }
        return distance;
    }

    public void binarization(){ 
        for(int i = 0; i < image.getWidth(); i++){
            for(int j = 0; j < image.getHeight(); j++){
                int r = (int)map.get(Color.red(image.getPixel(i, j)));        
                image.setPixel(i, j, Color.rgb(r*255, r*255, r*255));
            }
        }
    }

    public void dinamicCluster(){
        initGroups();

        boolean found;
        do{
            gerarProtos();
            found = false;

            for(int i = 0; i < image.getWidth(); i++){
                for(int j = 0; j < image.getHeight(); j++){
                    //TODO calcular diferenÃ§a dos pixels aos grupos e se necessÃ¡rio trocar grupo
                    double d0 = L1Distance(i,j,0);
                    double d1 = L1Distance(i,j,1);

                    if(d0 <= d1 && map.get(image.getPixel(i, j)) != 0){
                        map.put(image.getPixel(i, j), 0);
                        found = true;
                    }
                    else if(d1 < d0 && map.get(image.getPixel(i, j)) != 1){
                        map.put(image.getPixel(i, j), 1);
                        found = true;
                    }
                }
            }
        }while(found);
    }

    public void dinamicCluster2(){
        initGroups();

        boolean found;
        do{
            gerarProtos();
            found = false;

            for(int i = 0; i < 256; i++){

                //TODO calcular diferenÃ§a dos pixels aos grupos e se necessÃ¡rio trocar grupo
                double d0 = L1Distance2(i,0);
                double d1 = L1Distance2(i,1);

                if(d0 <= d1 && map.get(i) != 0){
                    map.put(i, 0);
                    found = true;
                }
                else if(d1 < d0 && map.get(i) != 1){
                    map.put(i, 1);
                    found = true;
                }
            }
            System.out.println("vezes");
        }while(found);
        binarization();
    }
    public void Gradient(){
        
        int [][] mask1 = {{0,0,0},{0,-1,0},{0,0,1}};  //Peso definido do problema
        int [][] mask2 = {{0,0,0},{0,0,-1},{0,1,0}};
        int middle = mask1.length/2;
        for(int i = 0; i < image.getWidth(); i++){
            for(int j = 0; j < image.getHeight(); j++){
                float media1 = 0;
                float media2 = 0;

                for(int k = 0; k < mask1.length; k++){
                    for(int w = 0; w < mask1.length; w++){
                        try{                          
                           //Operação para primeira máscara
                           media1 += image.getPixel(i + k - middle,j + w - middle) * mask1[k][w];                        
                           //Operação para segunda máscara 
                           media2 += image.getPixel(i + k - middle,j + w - middle) * mask2[k][w];
                           
                        }catch(Exception e){}
                    }  
                }  
                media1 = Math.abs(media1) + Math.abs(media2);

                if(media1 >= 255)
                    media1 = 255;
                else
                	media1 = 0;
                              
                //Color c = new Color((int)mediaR1, (int)mediaG1, (int)mediaB1);
                ;
                image.setPixel(i, j, Color.rgb((int)media1, (int)media1, (int)media1));
            }
        }
        
    }
  /*  public static void main(String args []) throws IOException{
        /*String name = "lena";
        String type = ".png";
        Binarization b = new Binarization(name+type);
        b.dinamicCluster2();
        File f = new File(name + "b" + type);
        //b.image.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //b.image.showImage();
        b.image.saveImage(f, b.image.getImage2());*/

        /*Line l = new Line(0,0,50,50);
        Line [] lines = new Line[2];
        lines[0] = l;
        l = new Line(50,50,100,100);
        lines[1] = l;
        Camera c = new Camera(2, lines);

        Thread tc = new Thread(c);
        tc.start();


        Scanner s = new Scanner(System.in);
        System.out.println("press any button to close...");
        s.hasNextLine();
        BufferedImage img = c.getSnapshot();
        Image image = new Image(img);
        image.showImageCanvas();
        c.setCountLines(0);
        //c.closeCamera();

        Slides slide = new Slides("Negocios.pptx");
        Scanner s = new Scanner(System.in);
        System.out.println(slide.getSizeSlide());
        for(int i = 0 ; i < slide.getSizeSlide(); i++){
            BufferedImage img = (BufferedImage)slide.getSlide(i);
            Image.showImageCanvas(img);
            s.next();
        }
    }*/
}
