package Model;


import Model.Paquete;
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

public class MiHilo extends Thread{
    
        private int idHilo;
        private Socket so;
        private final int puerto = 5000;
        private final String host= "localhost";
        private Paquete p;
        private ObjectOutputStream mensaje;
        private DataOutputStream mensajetexto;
        private DataInputStream entradatxt;
        Toolkit t = Toolkit.getDefaultToolkit();
        
        ClienteARC clientes;
        
        public MiHilo(int _idHilo, ClienteARC _clientes){
            idHilo = _idHilo;
            clientes = _clientes;
        }

        @Override
        public synchronized void run(){
            try{
                p = this.crearPaquete();
                this.iniciarCliente(p);
               
            }catch(Exception e){
                e.printStackTrace();
            }

        }
        
        public synchronized void iniciarCliente(Paquete p) throws InterruptedException{

            try{
                //Asignamos el identificador del hilo y un socket a escuchar el puerto de un host
                so = new Socket(host, puerto);

                //Mensaje que vamos a recibir
                entradatxt = new DataInputStream((so.getInputStream()));
                
                mensajetexto = new DataOutputStream(so.getOutputStream());
                mensajetexto.writeUTF("A la espera proceso "+getIdHilo());
                mensajetexto.flush();

                String ent = entradatxt.readUTF();
                System.out.println(ent);
                if(ent.equals("Conexión creada, mantente a la espera.")){
                    this.wait(1000);
                }
                
                ent = entradatxt.readUTF();
                System.out.println("ENT 2: " + ent);
                if(ent.equals("Comenzar")){
                    System.out.println("voy a enviar");
                    enviarPaquete(p);
                }
                

                //mensaje que vamos a enviar
                mensajetexto = new DataOutputStream(so.getOutputStream());
                
                //so.close();
            } catch (IOException ex) {
                System.out.println("ERROR INICIAR CLIENTE "+ getIdHilo());
            }
        }
        
        public Paquete crearPaquete (){
            
            double coordx, coordy;
            Random x = new Random();
            Random y = new Random();
            
            Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();

            coordx = (x.nextDouble() * screenSize.width + 0);
            coordy = (y.nextDouble() * screenSize.height + 0);
            
            Paquete p = new Paquete(this.idHilo, coordx, coordy);
            return p;
        }
        
        private void enviarPaquete(Paquete p) throws IOException, InterruptedException{
            System.out.println("Ha llegado el mensaje comenzar para el hilo "+ getIdHilo());
            System.out.println("Paquete: "+ p.getID() + " Con coordenadas: " + p.getX()+ " " + p.getY());
            mensaje = new ObjectOutputStream(so.getOutputStream());
            mensaje.writeObject(p);
            System.out.println("Enviado paquete: " + p.getID());
            this.wait(3000);
            mensaje.flush();
        }
        
        
        public int getIdHilo(){
            return idHilo;
        }

        public void setIdHilo(int idHilo){
            this.idHilo = idHilo;
        }

    }
