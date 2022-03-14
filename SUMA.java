package Opciones_procesos;
import Procesos.*;

public class SUMA {

    /*Método para poder iniciar la ejecución de nuestro hilo*/
    public static PROCESOS suma(PROCESOS thread){
        run();
        return thread;
    }

    /*Método encargado de la ejecución de nuestro hilo*/
    public static  void run(){
        int numero_max=(int)(Math.random()*9999+1), resultado=0, i;

        for(i=0; i<numero_max; i++){
            resultado+=i;
            System.out.println("Suma: "+resultado);
            if(i%5!=0) {
                break;
            }
        }
    }
}
