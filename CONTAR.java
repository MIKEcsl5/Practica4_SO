package Opciones_procesos;
/*Importamos esta clase para poder hacer uso de los "hilos"*/
import Procesos.PROCESOS;

public class CONTAR{

    /*Método para poder iniciar la ejecución de nuestro hilo*/
    public static PROCESOS contar(PROCESOS thread) {
        run();
        return thread;
    }

    /*Método encargado de la ejecución de nuestro hilo*/
    public static void run() {
        int numero_max = (int) (Math.random() * 9999 + 1), i, contar;

        for (i = 0; i < numero_max; i++) {
            contar=i;
            System.out.println("Contando: "+contar);
            if(i%5!=0) {
                break;
            }
        }
    }
}
