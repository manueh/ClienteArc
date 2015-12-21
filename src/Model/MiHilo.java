package Model;


import Model.Paquete;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
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
        private ArrayList<String> tiempos;
        private File f , f1;
        private BufferedWriter bw, bw1;
        private FileWriter w, w1;
        private PrintWriter  pw, pw1;
        private Date fecha;
        private int numCiclos;
        ClienteARC clientes;
        
        public MiHilo(int _idHilo, ClienteARC _clientes){
            
            idHilo = String.valueOf(_idHilo);
            clientes = _clientes;
            fecha = new Date();
            f  = new File("tiempos" + fecha.getDay()+ fecha.getMonth()+ ".txt");
            f1 = new File("tiempos_medios" + fecha.getDay()+ fecha.getMonth()+ ".txt");
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
                
                //Le damos al paquete un id segun de la ip que proviene para
                //poderlos diferenciar.
                ip = so.getLocalAddress();
                String ipString = ip.toString();
                String id = this.getId() + ipString ; 
                this.setIdHilo(id);
                
                mensajetexto.writeUTF(getIdHilo());
                mensajetexto.flush();
                
                String ent = entradatxt.readUTF();
                while(!ent.equals("Finalizar")){
                    System.out.println(ent);
                    switch(ent){
                        
                        case "Conexión creada, mantente a la espera.":
                            System.out.println("Conexion creada");
                            w = new FileWriter(f, true);
                            bw = new BufferedWriter(w);
                            pw = new PrintWriter(bw);
                            w1 = new FileWriter(f1, true);
                            bw1 = new BufferedWriter(w1);
                            pw1 = new PrintWriter(bw1);
                            time_start = System.currentTimeMillis();
                            sleep(10);
                            break;
                        case "Numero Vecinos":
                            contVecinos = Integer.parseInt(entradatxt.readUTF());
                            paquetesVecinos = new ArrayList<Paquete>();
                            break;
                        case "Comenzar":
                            p = this.crearPaquete();
                            sleep(10);
                            enviarPaquete(p);
                            break;
                        case "Mantente a la espera.":
                            sleep(10);
                            break;
                        case "Te envio a tus vecinos.":                              
                            almacenarPaquetesServidor();
                            break;
                        case "Fin Ciclo":
                            time_end = System.currentTimeMillis();
                            System.out.println("Tiempo ciclo para proceso: " + this.getIdHilo() + " -> " + (time_end - time_start )/100 +" seconds");
                            if(f.exists())
                                pw.println("Tiempo ciclo para proceso:\t" + this.getIdHilo()+"\t" + "-> " + (time_end - time_start )/100 +" seconds \n");
                            else
                                pw.write("Tiempo ciclo para proceso:\t" + this.getIdHilo() + "\t" + "-> " + (time_end - time_start )/100 +" seconds");
                            pw.close();
                            bw.close();
                            paquetesVecinos.clear();
                            numCiclos++;
                            break;
                        default:
                            System.out.println("No se ha obtenido respuesta del servidor");
                            break;
                    }
                    ent = entradatxt.readUTF();
                }
                
                tiempo_total = System.currentTimeMillis();
                System.out.println("Tiempo total ejecución: " + (tiempo_total - time_start)/1000 + "segundos.");
                System.out.println("Tiempo medio ejecución: " + ((tiempo_total - time_start)/1000)/numCiclos + "segundos.");
                
                
                if(f1.exists())
                    pw1.println("Hilo " + this.getIdHilo() + "\tTiempo medio ejecución: " + ((tiempo_total - time_start)/1000)/numCiclos + "seconds.");
                else
                    pw1.write("Hilo " + this.getIdHilo() + "\tTiempo medio ejecución: " + ((tiempo_total - time_start)/1000)/numCiclos + "seconds.");
                
                pw1.close();
                bw1.close();
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
                    mensajeTodos.flush();
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

        public ArrayList<String> getTiempos() {
            return tiempos;
        }

        public void setTiempos(ArrayList<String> tiempos) {
            this.tiempos = tiempos;
        }
        
    }
