/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


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
fdsfdsfdsfds


public class ClienteARC {
    
    private Socket so;
    private final int puerto = 5000;
    private final String host= "localhost";
    private DataOutputStream mensaje;
    private DataInputStream entrada;
    
    public void lanzarHilos(){
        for(int i=0; i< 60; i++){
            MiHilo h = new MiHilo(i, this);
            h.start();
            
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
    
    public void iniciarCliente(MiHilo h){
        int id = -1;
        String nombre;
        
        try {
            //Asignamos el identificador del hilo
            id = h.getIdHilo();
            nombre = Integer.toString(id);
            //Asigna un socket a escuchar el puerto de un host 
            so = new Socket(host, puerto);
            //Mensaje que vamos a recibir
            entrada = new DataInputStream(new DataInputStream(so.getInputStream()));
            //mensaje que vamos a enviar
            mensaje = new DataOutputStream(so.getOutputStream());
            
            mensaje.writeUTF(nombre);
            System.out.println(entrada.readUTF() +"Hilo "+id);
            
            //so.close();
        } catch (IOException ex) {
            System.out.println("ERROR INICIAR CLIENTE "+ id);
        }
    }
    
 
    public static void main(String[] args) {
        ClienteARC c = new ClienteARC();
        c.lanzarHilos();
        
    }
    
}
