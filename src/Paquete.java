/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Manu
 */
public class Paquete {
    private double coordx, coordy; 
    private int id;
    
    public Paquete(int id, double coordx,double coordy){
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
    
    public void setY(double coord){
        this.coordy = coordy;
    }
    
    public double getY(){
        return this.coordy;
    }
    
    public void setID(int id){
        this.id = id;
    }
    
     public int getID(){
        return this.id;
    }
    
}
