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
    private final String host= "192.168.1.5";
    private ObjectOutputStream mensaje;
    private DataOutputStream mensajetexto;
    private DataInputStream entrada;
    private double coordx, coordy; 
    Toolkit t = Toolkit.getDefaultToolkit();
    
    public void lanzarHilos(){
        for(int i=0; i< 60; i++){
            MiHilo h = new MiHilo(i, this);
            h.start();
            System.out.println("YEEEEEEEEEEEEEEEEEEEEE");
            /**
             * Usamos el método join para realentizar el proceso y ver que todo se conecta de forma correcta
             */
            try {
                h.join(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClienteARC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//jijiji//
    
    public void obtenerCoordenadas (){
        Random x = new Random();
        Random y = new Random();
        
        Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
        
        this.coordx = (x.nextDouble() * screenSize.width + 0);
        this.coordy = (y.nextDouble() * screenSize.height + 0);
        
    }
    
    public void iniciarCliente(MiHilo h){
        int id = -1;
        String nombre;
        
        try {
            //Asignamos el identificador del hilo
            //Asigna un socket a escuchar el puerto de un host 
            so = new Socket(host, puerto);
            System.out.println("SOCKET");
            //Mensaje que vamos a recibir
            mensajetexto = new DataOutputStream(so.getOutputStream());
            entrada = new DataInputStream(new DataInputStream(so.getInputStream()));
            //mensaje que vamos a enviar
            mensaje = new ObjectOutputStream(so.getOutputStream());
            System.out.println("Debo mandar mensaje ahora");
            mensajetexto.writeUTF("hola que tal");
            
            System.out.println(entrada.readUTF());
            
            if(entrada!=null){
                mensaje.writeObject(h);
            }
            
            
            //so.close();
        } catch (IOException ex) {
            System.out.println("ERROR INICIAR CLIENTE "+ id);
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
