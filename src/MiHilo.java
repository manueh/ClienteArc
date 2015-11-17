
import java.io.Serializable;

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
        private double coordx, coordy;
        ClienteARC clientes;
        
        public MiHilo(int _idHilo, ClienteARC _clientes){
            idHilo = _idHilo;
            clientes = _clientes;
        }

        @Override
        public void run(){
            try{
                clientes.obtenerCoordenadas();
                this.coordx = clientes.getCoordenadaX();
                this.coordy = clientes.getCoordenadaY();
                clientes.iniciarCliente(this);
               
            }catch(Exception e){
                e.printStackTrace();
            }

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