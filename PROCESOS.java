package Procesos;
import Opciones_procesos.*;

public class PROCESOS{
    /*Variables referentes a los atributos de un proceso*/
    public int id_process, num_instructions, size_process, opcion, num_instructions_executed;
    public int Dir_base, Dir_limite;
    public Thread process;
    public String Estado;

    /*Constructor con la creación de un Proceso con sus atributos*/
    public PROCESOS(String name_process, int id_process, int num_instructions, int size_process, int opcion, int Dir_base, int Dir_limite,
    String Estado){
        process=new Thread(name_process);
        this.id_process=id_process;
        this.num_instructions=num_instructions;
        this.size_process=size_process;
        this.opcion=opcion;
        num_instructions_executed=0;
        this.Dir_base=Dir_base;
        this.Dir_limite=Dir_limite;
        this.Estado=Estado;
    }

    /*Constructor vacío*/
    public PROCESOS(){}

    /*Constructor de procesador auxiliar*/
    public PROCESOS(String name){process=new Thread("");}

    /*Menú pera la ejecuciónde un proceso dependiendo de su opción de creación*/
    public static PROCESOS Selection_Process(PROCESOS thread){
        PROCESOS proceso=new PROCESOS();
        int aux=0;

        switch (thread.opcion){
            case 1:
                proceso=SUMA.suma(thread);
                break;
            case 2:
                proceso=RESTA.resta(thread);
                break;
            case 3:
                proceso=FACTORIAL.factorial(thread);
                break;
            case 4:
                proceso=CONTAR.contar(thread);
                break;
            case 5:
                proceso=IMPRESION.impresion(thread);
                break;
        }
        if(proceso.num_instructions_executed+5<= proceso.num_instructions)
            proceso.num_instructions_executed+=5;
        else {
            aux = proceso.num_instructions - proceso.num_instructions_executed;
            proceso.num_instructions_executed+=aux;
        }
        return proceso;
    }
}
