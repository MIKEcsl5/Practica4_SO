package Opciones_procesos;

import Procesos.PROCESOS;

public class FACTORIAL{

    /*Método para poder iniciar la ejecución de nuestro hilo*/
    public static PROCESOS factorial(PROCESOS thread){
        run();
        return thread;
    }

    /*Método encargado de la ejecución de nuestro hilo*/
    public static void run(){
        int numero_max=(int)(Math.random()*9999+1), resultado=0, i;

        for(i=0; i<numero_max; i++){
            if(i==0||i==1)
                resultado=1;
            else
                resultado*=i;
            System.out.println("Multiplicaci\u00F3n: "+resultado);
            if(i%5!=0) {
                break;
            }
        }
    }
}
