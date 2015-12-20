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
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MiHilo extends Thread{
    
        private String idHilo;
        private Socket so;
        private final int puerto = 5000;
        private final String host= "localhost";
        private Paquete p;
        private ObjectOutputStream mensaje;
        private ObjectInputStream vecinos;
        private DataOutputStream mensajetexto;
        private DataInputStream entradatxt;
        Toolkit t = Toolkit.getDefaultToolkit();
        private ArrayList<Paquete> paquetesVecinos;
        private  int contVecinos ;
        
        ClienteARC clientes;
        
        public MiHilo(int _idHilo, ClienteARC _clientes){
            idHilo = String.valueOf(_idHilo);
            clientes = _clientes;
            
        }

        @Override
        public synchronized void run(){
            try{
                this.iniciarCliente();

            }catch(Exception e){
                e.printStackTrace();
            }

        }
        
        public synchronized void iniciarCliente() throws InterruptedException{
            InetAddress ip;
            long time_start = 0, time_end;
            long tiempo_total = 0;
            
            try{
                //Asignamos el identificador del hilo y un socket a escuchar el puerto de un host
                so = new Socket(host, puerto);

                //Mensaje que vamos a recibir
                entradatxt = new DataInputStream((so.getInputStream()));
                mensajetexto = new DataOutputStream(so.getOutputStream());
                
                ip = so.getLocalAddress();
                String ipString = ip.toString();
                String id = this.getId() + ipString ; 
                this.setIdHilo(id);
                
                mensajetexto.writeUTF(getIdHilo());
                mensajetexto.flush();
                
                String ent = entradatxt.readUTF();

                while(!ent.equals("Finalizar")){
                    switch(ent){
                        case "Conexión creada, mantente a la espera.":
                            System.out.println("Conexion creada");
                            time_start = System.currentTimeMillis();
                            sleep(50);
                            break;
                        case "Numero Vecinos":
                            contVecinos = Integer.parseInt(entradatxt.readUTF());
                            paquetesVecinos = new ArrayList<Paquete>();
                            break;
                        case "Comenzar":
                            System.out.println("Creando paquete...");
                            p = this.crearPaquete();
                            sleep(50);
                            System.out.println("Enviando paquete " + p.getID());
                            enviarPaquete(p);
                            break;
                        case "Mantente a la espera.":
                            sleep(500);
                            break;
                        case "Te envio a tus vecinos.":                              
                            almacenarPaquetesServidor();
                            break;
                        case "Fin Ciclo":
                            time_end = System.currentTimeMillis();
                            System.out.println("Tiempo ciclo para proceso: " + this.getIdHilo() + " -> " + (time_end - time_start ) +" milliseconds");
                            break;
                        default:
                            System.out.println("No se ha obtenido respuesta del servidor");
                            break;
                    }
                    ent = entradatxt.readUTF();
                }
                
                tiempo_total = System.currentTimeMillis();
                System.out.println("Tiempo total ejecución: " + (tiempo_total - time_start));
                
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
            
            String idPaquete = "p_" + this.getIdHilo();
            Paquete p = new Paquete(idPaquete, coordx, coordy);
            return p;
        }
        
        private void enviarPaquete(Paquete p) throws IOException, InterruptedException{
            
            mensaje = new ObjectOutputStream(so.getOutputStream());
            mensaje.writeObject(p);
            mensaje.flush();
        }
        
        public void almacenarPaquetesServidor(){
            
            Paquete p_aux;                
            try {
                vecinos = new ObjectInputStream(so.getInputStream());
                p_aux = (Paquete) vecinos.readObject();
                paquetesVecinos.add(p_aux);
                
                if(paquetesVecinos.size() == contVecinos )
                {                    
                    DataOutputStream mensajeTodos = new DataOutputStream(so.getOutputStream());
                    mensajeTodos.writeUTF("Todos Recibidos");
                }    
                        
            } catch (IOException ex) {
                Logger.getLogger(MiHilo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MiHilo.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            }
        
        
        public String getIdHilo(){
            return idHilo;
        }

        public void setIdHilo(String idHilo){
            this.idHilo = idHilo;
        }

    }
