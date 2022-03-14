import java.util.*;
import Procesos.*;

public class SO {
    /*Memoria de procesos*/
    static String MEMORIA[]=new String[2048];
    static Scanner Teclado=new Scanner(System.in);
    /*Cola de procesos*/
    static Queue<PROCESOS> Cola_Procesos=new LinkedList<>();
    static Queue<PROCESOS> Auxiliar_Cola_Procesos=new LinkedList<>();
    static List<PROCESOS> Procesos_Concluidos=new LinkedList<>();
    static List<PROCESOS> Procesos_Matados=new LinkedList<>();
    static Queue<PROCESOS> Procesos_espera_memoria=new LinkedList<>();
    static int contadorSuma=0, contadorResta=0, contadorFactorial=0, contadorContar=0, contadorImprimir=0, k, j;

    public static void main(String[] args){
        /*Llenado de la memoria*/
        INICIO_MEMORIA();
        /*Arranque de procesos iniciales como un Sistema Operativo*/
        INICIO_PROCESOS();
        /*Se le da control al usuario para poder crear, ejecutar, eliminar, imprimir procesos entre otras cosas a
        * dietra y siniestra*/
        MENU_SO();
    }

    /*Inicialización de la Memoria de procesos con cadenas vacías*/
    public static void INICIO_MEMORIA(){
        for(int i=0; i<2048; i++){
            MEMORIA[i]="";
        }
    }

    /*El usuario indicará el nombre del nuevo proceso, a este se le asignará de forma automática un ID, N° de instrucciones
    * N° de espacios en memoria, además de que se agregará a la cola de procesos*/
    public static void MENU_PROCESOS(){
        int id_process, instructions, Dir_base, size_memory, Dir_limite, opcion, id_search;
        String nombre, Estado="Innactivo", name_search;
        PROCESOS proceso=new PROCESOS(), head=new PROCESOS(), new_process=new PROCESOS();
        char Eleccion='a';

        System.out.println("Indique qu\u00E9 har\u00E1 el nuevo proceso:\n\t1.- Suma\n\t2.- Resta\n\t3.- Factorial");
        System.out.print("\t4.- Contar\n\t5.- Imprimir Saludos\nElecci\u00F3n: ");
        opcion=Teclado.nextInt();
        Teclado.nextLine();
        id_process=(int)(Math.random()*99999+1);
        size_memory=SIZE_MEMORY();
        instructions=NUMERO_INSTRUCCIONES();
        System.out.println("Ingrese el nombre del proceso a ejecutar(sin \".exe\"): ");
        nombre=Teclado.nextLine()+".exe";
        System.out.println("Tama\u00F1o del proceso: "+size_memory);
        new_process=new PROCESOS(nombre, id_process, instructions, size_memory, opcion, 0, 0, Estado);
        while(!Espacio_Suficiente(size_memory)&&Eleccion!='C'&&Eleccion!='c'){
            System.out.println("No hay  memoria suficiente, tiene tres opciones:\n\ta) Eliminar Procesos\n\tb) Ejecutar procesos");
            System.out.println("\tc) Agregar a una Cola de espera para pasar al men\u00FA del SO\n\tElecci\u00F3n: ");
            Eleccion=Teclado.next().charAt(0);
            switch (Eleccion){
                case 'a':
                case 'A':
                    IMPRIMIR_COLA();
                    System.out.print("\nIngrese el ID del proceso a eliminar: ");
                    id_search=Teclado.nextInt();
                    Teclado.nextLine();
                    System.out.print("\nIngrese el nombre del proceso a eliminar: ");
                    name_search=Teclado.nextLine();
                    for (int contador=0; contador<Cola_Procesos.size(); contador++) {
                        proceso=Cola_Procesos.element();
                        if(proceso.Estado.compareTo("Activo")==0)
                            head=proceso;
                        if(proceso.process.getName().compareTo(name_search)==0&&proceso.id_process==id_search) {
                            ELIMINAR_PROCESOS(proceso);
                            break;
                        }
                        else {
                            Cola_Procesos.add(Cola_Procesos.poll());
                        }
                    }
                    for (int contador=0; contador<Cola_Procesos.size(); contador++){
                        proceso=Cola_Procesos.element();
                        if(proceso.Estado.compareTo("Activo")==0&&proceso.process.getName().compareTo(head.process.getName())!=0&&proceso.id_process!=head.id_process)
                            proceso.Estado="Inactivo";
                        if(proceso.Estado.compareTo("Activo")!=0)
                            Cola_Procesos.add(Cola_Procesos.poll());
                        else
                            break;
                    }
                    break;
                case 'b':
                case 'B':
                    EJECUCION_PROCESOS(proceso);
                    break;
                case 'c':
                case 'C':
                    Procesos_espera_memoria.add(proceso);
                    break;
            }
        }
        if(Eleccion!='c'&&Eleccion!='C') {
            System.out.println("El proceso se pudo crear con \u00E9xito, se ha agregado a la cola y a la memoria.");
            new_process.Dir_base = j;
            new_process.Dir_limite = j+size_memory-1;
            Cola_Procesos.add(new_process);
            Asignacion_Memoria(new_process.process.getName(), new_process.size_process, new_process.Dir_base);
        }
    }

    /*En esta parte lo que se hace es llenar la memoria con procesos los cuáles se generarán y cada uno hará suma, resta
    * factorial, contar o imprimir en pantalla dependiendo del número aleatorio que salga para la creación de este; además
    * se verificará que haya espacio en memoria para poder agregarlo a la cola*/
    public static void INICIO_PROCESOS(){
        PROCESOS proceso;
        int opcion;
        int id_process, instructions, size_memory, inicio=0, Dir_base, Dir_limite;
        boolean espacio=true;
        String nombre, Estado;

        do{
            id_process=(int)(Math.random()*9999+1);
            size_memory=SIZE_MEMORY();
            instructions=NUMERO_INSTRUCCIONES();
            opcion=(int)(Math.random()*5+1);
            Dir_base=inicio;
            Dir_limite=inicio+size_memory;
            if(Cola_Procesos.size()==0)
                Estado="Activo";
            else
                Estado="Inactivo";
            switch(opcion){
                case 1:
                    nombre="Suma"+contadorSuma+".exe";
                    contadorSuma++;
                    proceso=new PROCESOS(nombre, id_process, instructions, size_memory, opcion, Dir_base, Dir_limite, Estado);
                    if(Espacio_Suficiente(size_memory)) {
                        Asignacion_Memoria(nombre, size_memory, inicio);
                        inicio += size_memory;
                        Cola_Procesos.add(proceso);
                    }
                    else espacio=false;
                    break;
                case 2:
                    nombre="Resta"+contadorResta+".exe";
                    contadorResta++;
                    proceso=new PROCESOS(nombre, id_process, instructions, size_memory, opcion, Dir_base, Dir_limite, Estado);
                    if(Espacio_Suficiente(size_memory)) {
                        Asignacion_Memoria(nombre, size_memory, inicio);
                        inicio += size_memory;
                        Cola_Procesos.add(proceso);
                    }
                    else espacio=false;
                    break;
                case 3:
                    nombre="Factorial"+contadorFactorial+".exe";
                    contadorFactorial++;
                    proceso=new PROCESOS(nombre, id_process, instructions, size_memory, opcion, Dir_base, Dir_limite, Estado);
                    if(Espacio_Suficiente(size_memory)) {
                        Asignacion_Memoria(nombre, size_memory, inicio);
                        inicio += size_memory;
                        Cola_Procesos.add(proceso);
                    }
                    else espacio=false;
                    break;
                case 4:
                    nombre="Contar"+contadorContar+".exe";
                    contadorContar++;
                    proceso=new PROCESOS(nombre, id_process, instructions, size_memory, opcion, Dir_base, Dir_limite, Estado);
                    if(Espacio_Suficiente(size_memory)) {
                        Asignacion_Memoria(nombre, size_memory, inicio);
                        inicio += size_memory;
                        Cola_Procesos.add(proceso);
                    }
                    else espacio=false;
                    break;
                case 5:
                    nombre="Imprimir"+contadorImprimir+".exe";
                    contadorImprimir++;
                    proceso=new PROCESOS(nombre, id_process, instructions, size_memory, opcion, Dir_base, Dir_limite, Estado);
                    if(Espacio_Suficiente(size_memory)) {
                        Asignacion_Memoria(nombre, size_memory, inicio);
                        inicio += size_memory;
                        Cola_Procesos.add(proceso);
                    }
                    else espacio=false;
                    break;
            }
        }while(espacio);
    }

    /*Esta función lo que hace es llenar las localidades de memoria respecto a los procesos que se vayan generando para
    * tener control de estos*/
    public static void Asignacion_Memoria(String nombre, int size_memory, int inicio){
        for(int i=inicio; i<inicio+size_memory; i++){
            MEMORIA[i]=nombre;
        }
    }

    /*Esta función lo que se hace es corroborar que haya espacio suficiente en medio o al final de la memoria para poder
    * ingresar un nuevo proceso*/
    public static boolean Espacio_Suficiente(int size_memory){
        int i;
        j=k=0;
        boolean tentativo=true, regreso=false;

        for(i=0; i<2048; i++){
            if(MEMORIA[i].compareTo("")==0){
                if(tentativo) {
                    j = i;
                    tentativo=false;
                }
                else{
                    k=i;
                }
            }
            else{
                if(((k-j)+1)==size_memory){
                    regreso=true;
                    break;
                }
                else
                    tentativo=true;
            }
        }
        if((k-j)>=size_memory)
            regreso=true;
        if(regreso&&Procesos_espera_memoria.size()!=0){
            PROCESOS proceso=Procesos_espera_memoria.poll();
            proceso.Dir_base=j;
            proceso.Dir_limite=k;
            Asignacion_Memoria(proceso.process.getName(), proceso.size_process, proceso.Dir_base);
            Cola_Procesos.add(proceso);
        }
        return regreso;
    }

    /*Menú del SO*/
    public static void MENU_SO(){
        char opcion;
        PROCESOS proceso=new PROCESOS(), ListadoFinalizado, ListadoEliminado;
        int i=0, SizeEliminados, SizeConcluidos;

        do{
            System.out.println("\n\ta) Crear Proceso\n\tb) Ver Estado Actual del Sistema");
            System.out.println("\tc) Imprimir Cola de Procesos\n\td) Ver Proceso Actual");
            System.out.println("\te) Ejecutar Proceso Actual\n\tf) Pasar al Proceso Siguiente");
            System.out.println("\tg) Matar Proceso Actual\n\th) Cerrar Programa\n\tElecci\u00F3n: ");
            opcion=Teclado.next().charAt(0);
            switch (opcion){
                case 'a':
                case 'A':
                    MENU_PROCESOS();
                    break;
                case 'b':
                case 'B':
                    i=0;
                    SizeEliminados=Procesos_Matados.size();
                    SizeConcluidos=Procesos_Concluidos.size();
                    System.out.println("N\u00FAmeros de Procesos en la Cola: "+Cola_Procesos.size());
                    System.out.println("\nProcesos Eliminados\tProcesos Finalizados");
                    do{
                        if(i<SizeConcluidos)
                            ListadoFinalizado=Procesos_Concluidos.get(i);
                        else
                            ListadoFinalizado=new PROCESOS("");
                        if(i<SizeEliminados)
                            ListadoEliminado=Procesos_Matados.get(i);
                        else
                            ListadoEliminado=new PROCESOS("");
                        /*Imprime las dos columnas*/
                        if(ListadoEliminado.process.getName().compareTo("")!=0&&ListadoFinalizado.process.getName().compareTo("")!=0)
                            System.out.println(ListadoEliminado.process.getName()+"\t\t"+ListadoFinalizado.process.getName());
                        else{
                            /*Imprime la primera columna*/
                            if(ListadoEliminado.process.getName().compareTo("")!=0&&ListadoFinalizado.process.getName().compareTo("")==0)
                                System.out.println(ListadoEliminado.process.getName());
                            else
                                /*Imprime la segunda columna*/
                                System.out.println("\t\t\t\t\t"+ListadoFinalizado.process.getName());
                        }
                        i++;
                    }while(ListadoFinalizado.process.getName().compareTo("")!=0||ListadoEliminado.process.getName().compareTo("")!=0);
                    Impresion_Memoria();
                    break;
                case 'c':
                case 'C':
                    IMPRIMIR_COLA();
                    break;
                case 'd':
                case 'D':
                    proceso=Cola_Procesos.element();
                    System.out.println("PROCESO ACTUAL");
                    System.out.println("Nombre: "+proceso.process.getName()+" ID Process: "+proceso.id_process+" Instrucciones Totales: "+proceso.num_instructions);
                    System.out.print("Instrucciones ejecutadas: "+proceso.num_instructions_executed+" Direcci\u00F3n Base: "+proceso.Dir_base);
                    System.out.println(" Direcci\u00F3n L\u00EDmite: "+proceso.Dir_limite);
                    break;
                case 'e':
                case 'E':
                    EJECUCION_PROCESOS(proceso);
                     break;
                case 'f':
                case 'F':
                    Cola_Procesos.element().Estado="Inactivo";
                    Cola_Procesos.add(Cola_Procesos.poll());
                    Cola_Procesos.element().Estado="Activo";
                    break;
                case 'g':
                case 'G':
                    ELIMINAR_PROCESOS(proceso);
                    break;
                case 'h':
                case 'H':
                    INICIO_MEMORIA();
                    Cola_Procesos.clear();
                    break;
                default:
                    System.out.println("Por favor escoja una de las opciones establecidas");
            }
        }while(opcion!='h'&&opcion!='H');
    }

    /*Impresion del estado de la memoria*/
    public static void Impresion_Memoria(){
        System.out.println("\nIMPRESION MEMORIA\nLocalidad\t\tProceso");
        for(int i=0; i< MEMORIA.length;i++)
            System.out.println(i+"\t\t"+ MEMORIA[i]);
    }

    /*Asignación tamaño memoria*/
    public static int SIZE_MEMORY(){
        int opcion=(int) (Math.random()*5);
        int size_memory=64;

        switch (opcion){
            case 1:
                size_memory=64;
                break;
            case 2:
                size_memory=128;
                break;
            case 3:
                size_memory=256;
                break;
            case 4:
                size_memory=512;
                break;
        }
        return size_memory;
    }

    /*Asignación N° instrucciones*/
    public static int NUMERO_INSTRUCCIONES(){
        int opcion=(int)(Math.random()*20+10);
        return opcion;
    }

    /*Impresión de la cola de procesos*/
    public static void IMPRIMIR_COLA(){
        System.out.println("PROCESOS EN COLA");
        for (PROCESOS impresion:Cola_Procesos) {
            if (impresion.process.getName().length() != 14&&impresion.process.getName().length()!=13) {
                if(impresion.id_process<1000) {
                    System.out.print("Nombre: " + impresion.process.getName() + "\t\tID Process: " + impresion.id_process + "\t\tInstrucciones restantes: ");
                    System.out.println((impresion.num_instructions - impresion.num_instructions_executed) + "\t\tEstado: " + impresion.Estado);
                    System.out.println("Direcci\u00F3n base: "+impresion.Dir_base+"\tDirecci\u00F3n l\u00EDmite: "+(impresion.Dir_limite-1));
                }
                else{
                    System.out.print("Nombre: " + impresion.process.getName() + "\t\tID Process: " + impresion.id_process + "\tInstrucciones restantes: ");
                    System.out.println((impresion.num_instructions - impresion.num_instructions_executed) + "\t\tEstado: " + impresion.Estado);
                    System.out.println("Direcci\u00F3n base: "+impresion.Dir_base+"\tDirecci\u00F3n l\u00EDmite: "+(impresion.Dir_limite-1));
                }
            }
            else {
                if(impresion.id_process<1000) {
                    System.out.print("Nombre: " + impresion.process.getName() + "\tID Process: " + impresion.id_process + "\t\tInstrucciones restantes: ");
                    System.out.println((impresion.num_instructions - impresion.num_instructions_executed) + "\t\tEstado: " + impresion.Estado);
                    System.out.println("Direcci\u00F3n base: "+impresion.Dir_base+"\tDirecci\u00F3n l\u00EDmite: "+(impresion.Dir_limite-1));
                }
                else{
                    System.out.print("Nombre: " + impresion.process.getName() + "\tID Process: " + impresion.id_process + "\tInstrucciones restantes: ");
                    System.out.println((impresion.num_instructions - impresion.num_instructions_executed) + "\t\tEstado: " + impresion.Estado);
                    System.out.println("Direcci\u00F3n base: "+impresion.Dir_base+"\tDirecci\u00F3n l\u00EDmite: "+(impresion.Dir_limite-1));
                }
            }
            System.out.println();
        }
    }

    /*Eliminación de Procesos de la Cola*/
    public static void ELIMINAR_PROCESOS(PROCESOS proceso){
        proceso=Cola_Procesos.poll();
        proceso.Estado="Eliminado";
        Procesos_Matados.add(proceso);
        for(int j=proceso.Dir_base; j< proceso.Dir_limite; j++)
            MEMORIA[j]="";
        System.out.print("\nEl proceso eliminado le faltaban: "+(proceso.num_instructions-proceso.num_instructions_executed));
        System.out.println(" instrucciones para terminar su ejecuci\u00F3n");
        Cola_Procesos.element().Estado="Activo";
    }

    /*Ejecución de procesos*/
    public static void EJECUCION_PROCESOS(PROCESOS proceso){
        proceso=PROCESOS.Selection_Process(Cola_Procesos.poll());
        if(proceso.num_instructions_executed==proceso.num_instructions) {
            Cola_Procesos.element().Estado="Activo";
            proceso.Estado="Terminado";
            Procesos_Concluidos.add(proceso);
            for(int j=proceso.Dir_base; j< proceso.Dir_limite; j++)
                MEMORIA[j]="";
        }
        else {
            proceso.Estado="Inactivo";
            Cola_Procesos.add(proceso);
            Cola_Procesos.element().Estado="Activo";
        }
    }
}
