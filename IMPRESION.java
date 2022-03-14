package Opciones_procesos;

import Procesos.PROCESOS;

public class IMPRESION{

    /*Método para poder iniciar la ejecución de nuestro hilo*/
    public static PROCESOS impresion(PROCESOS thread){
        run();
        return thread;
    }

    /*Método encargado de la ejecución de nuestro hilo*/
    public static void run() {
        int numero_max = (int) (Math.random() * 9999 + 1), i, contar;

        for (i = 0; i < numero_max; i++) {
            System.out.println("Hola Mundo!!!");
            if(i%5!=0) {
                break;
            }
        }
    }
}
