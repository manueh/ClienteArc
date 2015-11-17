
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

public class MiHilo extends Thread implements Serializable{
    
        private int idHilo;
        private Socket so;
        private final int puerto = 5000;
        private final String host= "147.156.88.143";

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
        public synchronized void run(){
            try{
                this.obtenerCoordenadas();
                this.iniciarCliente();
               
            }catch(Exception e){
                e.printStackTrace();
            }

        }
        
        public synchronized void iniciarCliente() throws InterruptedException{

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
                    enviarHilo();
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
        
        private void enviarHilo() throws IOException, InterruptedException{
            System.out.println("Ha llegado el mensaje comenzar para el hilo "+ getIdHilo());
            mensaje = new ObjectOutputStream(so.getOutputStream());
            mensaje.writeObject(this);
            this.wait();
            mensaje.flush();
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
