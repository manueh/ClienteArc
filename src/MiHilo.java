
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Manu
 */
public class MiHilo extends Thread implements Serializable{
    
        private int idHilo;
        //Thread h;
        private Socket so;
        private final int puerto = 5000;
        private final String host= "localhost";

        private ObjectOutputStream mensaje;
        private DataOutputStream mensajetexto;
        private DataInputStream entradatxt;
        private double coordx, coordy; 
        Toolkit t = Toolkit.getDefaultToolkit();
        
        ClienteARC clientes;
        
        public MiHilo(int _idHilo, ClienteARC _clientes){
            idHilo = _idHilo;
            clientes = _clientes;
        }

        @Override
        public void run(){
            try{
                this.obtenerCoordenadas();
                this.coordx = clientes.getCoordenadaX();
                this.coordy = clientes.getCoordenadaY();
                this.iniciarCliente();
               
            }catch(Exception e){
                e.printStackTrace();
            }

        }
        
        public void iniciarCliente(){

            try {
                //Asignamos el identificador del hilo y un socket a escuchar el puerto de un host
                so = new Socket(host, puerto);

                //Mensaje que vamos a recibir
                entradatxt = new DataInputStream(new DataInputStream(so.getInputStream()));

                mensajetexto = new DataOutputStream(so.getOutputStream());
                mensajetexto.writeUTF("A la espera proceso "+getIdHilo());
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
                        System.out.println("Ha llegado el mensaje comenzar para el hilo "+ getIdHilo());
                        mensaje = new ObjectOutputStream(so.getOutputStream());
                        mensaje.writeObject(this);
                        mensaje.flush();
                        break;
                }

                //mensaje que vamos a enviar
                mensajetexto = new DataOutputStream(so.getOutputStream());


                //so.close();
            } catch (IOException ex) {
                System.out.println("ERROR INICIAR CLIENTE "+ getIdHilo());
            }
        }

        public void obtenerCoordenadas (){
            Random x = new Random();
            Random y = new Random();

            Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();

            this.coordx = (x.nextDouble() * screenSize.width + 0);
            this.coordy = (y.nextDouble() * screenSize.height + 0);

        }
            
        public int getIdHilo(){
            return idHilo;
        }

        public void setIdHilo(int idHilo){
            this.idHilo = idHilo;
        }
        
        public Double getX(){
            return this.coordx;
        }
        
        public Double getY(){
            return this.coordy;
        }
    }