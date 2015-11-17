/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.*;
import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.Thread;
/**
 *
 * @author Manu
 */


public class ClienteARC {
    
    private Socket so;
    private final int puerto = 5000;
    private final String host= "localhost";
    
    private ObjectOutputStream mensaje;
    private DataOutputStream mensajetexto;
    private DataInputStream entradatxt;
    private double coordx, coordy; 
    Toolkit t = Toolkit.getDefaultToolkit();
    
    public void lanzarHilos(){
        for(int i = 0; i < 10; i++){
            MiHilo h = new MiHilo(i, this);
            h.start();
            
        }
    }
 
    public static void main(String[] args) {
        ClienteARC c = new ClienteARC();
        c.lanzarHilos();
        
    }
    
    public Double getCoordenadaX(){
        return this.coordx;
    }
    
    public Double getCoordenadaY(){
        return this.coordy;
    }
}
