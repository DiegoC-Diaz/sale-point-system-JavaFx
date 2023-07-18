/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import com.mycompany.sale_point.buscadorProducto;
import com.mycompany.sale_point.editorAvanzadoController;
import javafx.application.Platform;

/**
 *
 * @author Diego Carcamo
 */
public class Wait implements Runnable, producto {

    private long mili;
    private int num;
    private Object controller;

    public Wait() {
        controller = null;
        num = 0;
        mili = 0;

    }

    public Wait(float sec, Object controller) {

        this.controller = controller;
        this.mili = (long) (sec * 100);
        num = 10;
    }

    public void increment() {
        num += 1;
    }

    public void reset() {
        num = 10;
    }

    @Override
    public void run() {

        try {
            //an event with a button maybe
            System.out.println("button is clicked");
            for (int i = 0; i < num; i++) {
                System.out.println(i);
                Thread.sleep(mili);
            }

            System.out.println("End of thread");
        

                String className = controller.getClass().getSimpleName();

                if (className.equals ( 
                    "buscadorProducto")) {

                        ((buscadorProducto) controller).busquedaAutomatica();

                }

                else if (className.equals ( 
                    "editorAvanzadoController")) {
                        ((editorAvanzadoController) controller).busquedaAutomatica();

                }

                System.out.println (controller.getClass());
            
                

       

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        }
    }
