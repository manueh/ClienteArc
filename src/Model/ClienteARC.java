package Model;

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
import static java.lang.Thread.sleep;
/**
 *
 * @author Manu
 */


public class ClienteARC {
    
    public void lanzarHilos() {
        for(int i = 0; i < 100; i++){
            MiHilo h = new MiHilo(i, this);
            
            h.start();
            
        }
    }
 
    public static void main(String[] args) {
        ClienteARC c = new ClienteARC();
        c.lanzarHilos();
        
    }
}
