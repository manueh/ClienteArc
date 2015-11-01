/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Manu
 */
public class MiHilo extends Thread{
    
        private int idHilo;
        Thread h;
        ClienteARC clientes;
        public MiHilo(int idHilo, ClienteARC clientes){
            this.idHilo = idHilo;
            this.clientes = clientes;
        }

        @Override
        public void run(){
            try{
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
    }