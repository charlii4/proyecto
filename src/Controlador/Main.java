/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;
import Servicios.CentrosServices;
import Vistas.Ppal;
/**
 *
 * @author charliVB
 */
public class Main {
   
   public static void main(String[] args) {
       //ejecuta el controlador y este la vista
       Ppal p =new Ppal();
       new CentrosServices(p ).iniciar() ;
   }

}
    
