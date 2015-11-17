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
        for(int i = 0; i < 60; i++){
            MiHilo h = new MiHilo(i, this);
            h.start();
            /**
             * Usamos el método join para realentizar el proceso y ver que todo se conecta de forma correcta
             */
            try {
                h.join(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClienteARC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void obtenerCoordenadas (){
        Random x = new Random();
        Random y = new Random();
        
        Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
        
        this.coordx = (x.nextDouble() * screenSize.width + 0);
        this.coordy = (y.nextDouble() * screenSize.height + 0);
        
    }
    
    public void iniciarCliente(MiHilo h){
        
        try {
            //Asignamos el identificador del hilo y un socket a escuchar el puerto de un host
            so = new Socket(host, puerto);
            
            //Mensaje que vamos a recibir
            entradatxt = new DataInputStream(new DataInputStream(so.getInputStream()));
            
            mensajetexto = new DataOutputStream(so.getOutputStream());
            mensajetexto.writeUTF("A la espera proceso "+h.getIdHilo());
            mensajetexto.flush();
            
            System.out.println("ENTRADATXT.READ = "+ entradatxt.readUTF());
            
            switch (entradatxt.readUTF()) {
                case "Conexión creada, mantente a la espera.":
                    try {
                        System.out.println("Entro en espera");
                        this.wait(2000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ClienteARC.class.getName()).log(Level.SEVERE, null, ex);
                    }   break;
                case "Comenzar envio de coordenadas":
                    System.out.println("Ha llegado el mensaje comenzar para el hilo "+ h.getIdHilo());
                    mensaje = new ObjectOutputStream(so.getOutputStream());
                    mensaje.writeObject(h);
                    mensaje.flush();
                    break;
            }

            //mensaje que vamos a enviar
            mensajetexto = new DataOutputStream(so.getOutputStream());
            
            
            //so.close();
        } catch (IOException ex) {
            System.out.println("ERROR INICIAR CLIENTE "+ h.getIdHilo());
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
