package Model;


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
public class Paquete implements Serializable{
    private double coordx, coordy; 
    private String id;
    
    public Paquete(String id, double coordx,double coordy){
        this.id = id;
        this.coordx = coordx;
        this.coordy = coordy;
    }
    public void setX(double coordx){
        this.coordx = coordx;
    }
    
    public double getX(){
        return this.coordx;
    }
    
    public void setY(double coordy){
        this.coordy = coordy;
    }
    
    public double getY(){
        return this.coordy;
    }
    
    public void setID(String id){
        this.id = id;
    }
    
     public String getID(){
        return this.id;
    }
    
}
