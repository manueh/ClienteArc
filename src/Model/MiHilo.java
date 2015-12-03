package Model;


import Model.Paquete;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MiHilo extends Thread{
    
        private String idHilo;
        private Socket so;
        private final int puerto = 5000;
        private final String host= "192.168.1.107";
        private Paquete p;
        private ObjectOutputStream mensaje;
        private ObjectInputStream vecinos;
        private DataOutputStream mensajetexto;
        private DataInputStream entradatxt;
        Toolkit t = Toolkit.getDefaultToolkit();
        private Paquete[] paquetesVecinos;
        private  int contVecinos ;
        
        ClienteARC clientes;
        
        public MiHilo(int _idHilo, ClienteARC _clientes){
            idHilo = String.valueOf(_idHilo);
            System.out.println(idHilo);
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
            InetAddress ip;
            try{
                //Asignamos el identificador del hilo y un socket a escuchar el puerto de un host
                so = new Socket(host, puerto);

                //Mensaje que vamos a recibir
                entradatxt = new DataInputStream((so.getInputStream()));
                mensajetexto = new DataOutputStream(so.getOutputStream());
                ip = so.getLocalAddress();
                String ipString = ip.toString();
                String id = getId() + ipString ; 
                System.out.println(id);
                this.setIdHilo(id);
                mensajetexto.writeUTF("A la espera proceso "+getIdHilo());
                mensajetexto.flush();
                
                String ent = entradatxt.readUTF();
                
                while(!ent.equals("Finalizar")){
                    System.out.println("Valor entrada: " + ent);
                    switch(ent){
                        case "Conexión creada, mantente a la espera.":
                            System.out.println("Conexión Creada");
                            wait(100);
                            break;
                        case "Numero Vecinos":
                            int aux = 0;
                            aux = Integer.parseInt(entradatxt.readUTF());
                            paquetesVecinos = new Paquete[aux];
                            System.out.println("Mi tamaño de vector es: " + (aux));
                            break;
                        case "Comenzar":
                            System.out.println("voy a enviar");
                            enviarPaquete(p);
                            break;
                        case "Mantente a la espera.":
                            System.out.println("Esperando...");
                            wait(500);
                            break;
                        case "Te envio a tus vecinos.":                              
                            almacenarPaquetesServidor();
                            System.out.println("Almacenando...");
                               
                            
                            
                            /*for(contVecinos = 0; contVecinos < paquetesVecinos.length; contVecinos++)
                                System.out.println(paquetesVecinos[contVecinos].getID());
                            */
                            break;
                        default:
                            System.out.println("No se ha obtenido respuesta del servidor");
                            break;
                    }
                    ent = entradatxt.readUTF();
                }
                //mensaje que vamos a enviar
                mensajetexto = new DataOutputStream(so.getOutputStream());
                
                //so.close();
            } catch (IOException ex) {
                System.out.println("ERROR INICIAR CLIENTE "+ getIdHilo());
                ex.printStackTrace();
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
            
            mensaje = new ObjectOutputStream(so.getOutputStream());
            mensaje.writeObject(p);
            System.out.println("Enviado paquete: " + p.getID());
            mensaje.flush();
        }
        
        public void almacenarPaquetesServidor(){
            
            Paquete p;
            for(int i = 0 ; i < paquetesVecinos.length;  i++ ){
                
                try {
                    vecinos = new ObjectInputStream(so.getInputStream());
                    p = (Paquete) vecinos.readObject();
                    System.out.println("Paquete recibido: " + p.getID() + " " + p.getX());
                    paquetesVecinos[i] = p;
                    System.out.println("Soy el proceso " + this.getIdHilo() + " Y almaceno al Vecino " + p.getID() + " almacenado en pos " + i);
                } catch (IOException ex) {
                    Logger.getLogger(MiHilo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MiHilo.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
        public String getIdHilo(){
            return idHilo;
        }

        public void setIdHilo(String idHilo){
            this.idHilo = idHilo;
        }

    }
