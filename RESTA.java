package Opciones_procesos;

import Procesos.PROCESOS;

public class RESTA{

    /*Método para poder iniciar la ejecución de nuestro hilo*/
    public static PROCESOS resta(PROCESOS thread){
        run();
        return thread;
    }

    /*Método encargado de la ejecución de nuestro hilo*/
    public static void run(){
        int numero_max=(int)(Math.random()*9999+1), resultado=numero_max, i;
        for(i=0; i<numero_max; i++){
            resultado-=i;
            System.out.println("Resta: "+resultado);
            if(i%5!=0) {
                break;
            }
        }
    }
}
