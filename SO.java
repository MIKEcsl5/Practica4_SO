import java.util.*;
import Procesos.*;

public class SO {
    /*Memoria de procesos*/
    static String MEMORIA[]=new String[1024];
    static Scanner Teclado=new Scanner(System.in);
    /*Cola de procesos*/
    static Queue<PROCESOS> Cola_Procesos=new LinkedList<>();
    static List<PROCESOS> Procesos_Concluidos=new LinkedList<>();
    static List<PROCESOS> Procesos_Matados=new LinkedList<>();
    static List<ArrayList<String>> LocalidadesMemoria=new LinkedList<>();
    static Queue<PROCESOS> Procesos_espera_memoria=new LinkedList<>();
    static int contadorSuma=0, contadorResta=0, contadorFactorial=0, contadorContar=0, contadorImprimir=0, k, j;
    static ArrayList<Integer> LocalidadesDeMemoria=new ArrayList<>();

    public static void main(String[] args){
        /*Llenado de la memoria*/
        INICIO_MEMORIA();
        /*Arranque de procesos iniciales como un Sistema Operativo*/
        INICIO_PROCESOS();
        LLENADO_LOCALIDADES_MEMORIA();
        /*Se le da control al usuario para poder crear, ejecutar, eliminar, imprimir procesos entre otras cosas a
        * dietra y siniestra*/
        MENU_SO();
    }

    /*Inicialización de la Memoria de procesos con cadenas vacías*/
    public static void INICIO_MEMORIA(){
        for(int i=0; i<1024; i++){
            MEMORIA[i]="";
        }
    }/*ESCRITO FINALIZADO*/

    /*El usuario indicará el nombre del nuevo proceso, a este se le asignará de forma automática un ID, N° de instrucciones
    * N° de espacios en memoria, además de que se agregará a la cola de procesos*/
    public static void MENU_PROCESOS(){
        int id_process, instructions, size_memory, opcion, id_search;
        String nombre, Estado="Innactivo", name_search;
        PROCESOS proceso=new PROCESOS(), head=new PROCESOS(), new_process=new PROCESOS();
        char Eleccion='a';
        LinkedList<ArrayList<String>> Paginas=new LinkedList<>();

        System.out.println("Indique qu\u00E9 har\u00E1 el nuevo proceso:\n\t1.- Suma\n\t2.- Resta\n\t3.- Factorial");
        System.out.print("\t4.- Contar\n\t5.- Imprimir Saludos\nElecci\u00F3n: ");
        opcion=Teclado.nextInt();
        Teclado.nextLine();
        id_process=(int)(Math.random()*99999+1);
        size_memory=SIZE_MEMORY();
        instructions=NUMERO_INSTRUCCIONES();
        Paginas=PAGINACION(size_memory);
        size_memory=size_memory/16;
        System.out.println("Ingrese el nombre del proceso a ejecutar(sin \".exe\"): ");
        nombre=Teclado.nextLine()+".exe";
        System.out.println("Tama\u00F1o del proceso: "+size_memory);
        new_process=new PROCESOS(nombre, id_process, instructions, Paginas, opcion, 0, 0, Estado);
        while(!Espacio_Suficiente_Huecos(size_memory)&&Eleccion!='C'&&Eleccion!='c'){
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
                        if(proceso.Estado.compareTo("Activo")==0&&proceso.process.getName().compareTo(name_search)!=0)
                            head=proceso;
                        else
                            head=new PROCESOS("");
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
                        if(head.process.getName().compareTo("")!=0) {
                            if (proceso.Estado.compareTo("Activo") == 0 && proceso.process.getName().compareTo(head.process.getName()) != 0 && proceso.id_process != head.id_process)
                                proceso.Estado = "Inactivo";
                            if (proceso.Estado.compareTo("Activo") != 0)
                                Cola_Procesos.add(Cola_Procesos.poll());
                            else
                                break;
                        }
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
                    Procesos_espera_memoria.add(new_process);
                    break;
            }
        }
        if(Eleccion!='c'&&Eleccion!='C') {
            System.out.println("El proceso se pudo crear con \u00E9xito, se ha agregado a la cola y a la memoria.");
            //new_process.Dir_base = j;
            //new_process.Dir_limite = j+size_memory-1;
            Cola_Procesos.add(new_process);
            Asignacion_Memoria_Huecos(new_process.process.getName(), new_process.Paginas.size(), Paginas);
        }
    }/*ESCRITO FINALIZADO*/

    /*En esta parte lo que se hace es llenar la memoria con procesos los cuáles se generarán y cada uno hará suma, resta
    * factorial, contar o imprimir en pantalla dependiendo del número aleatorio que salga para la creación de este; además
    * se verificará que haya espacio en memoria para poder agregarlo a la cola*/
    public static void INICIO_PROCESOS(){
        PROCESOS proceso;
        LinkedList<ArrayList<String>> Paginas=new LinkedList<>();
        int opcion;
        int id_process, instructions, size_memory, inicio=0, Dir_base, Dir_limite;
        boolean espacio=true;
        String nombre, Estado;

        do{
            id_process=(int)(Math.random()*9999+1);
            size_memory=SIZE_MEMORY();
            instructions=NUMERO_INSTRUCCIONES();
            Paginas=PAGINACION(size_memory);
            opcion=(int)(Math.random()*5+1);
            Dir_base=inicio;
            size_memory=size_memory/16;
            Dir_limite=inicio+(size_memory/16)-1;
            if(Cola_Procesos.size()==0)
                Estado="Activo";
            else
                Estado="Inactivo";
            switch(opcion){
                case 1:
                    nombre="Suma"+contadorSuma+".exe";
                    contadorSuma++;
                    proceso=new PROCESOS(nombre, id_process, instructions, Paginas, opcion, Dir_base, Dir_limite, Estado);
                    if(Espacio_Suficiente(size_memory)) {
                        Asignacion_Memoria(nombre, size_memory, inicio, Paginas);
                        inicio += size_memory;
                        Cola_Procesos.add(proceso);
                    }
                    else espacio=false;
                    break;
                case 2:
                    nombre="Resta"+contadorResta+".exe";
                    contadorResta++;
                    proceso=new PROCESOS(nombre, id_process, instructions, Paginas, opcion, Dir_base, Dir_limite, Estado);
                    if(Espacio_Suficiente(size_memory)) {
                        Asignacion_Memoria(nombre, size_memory, inicio, Paginas);
                        inicio += size_memory;
                        Cola_Procesos.add(proceso);
                    }
                    else espacio=false;
                    break;
                case 3:
                    nombre="Factorial"+contadorFactorial+".exe";
                    contadorFactorial++;
                    proceso=new PROCESOS(nombre, id_process, instructions, Paginas, opcion, Dir_base, Dir_limite, Estado);
                    if(Espacio_Suficiente(size_memory)) {
                        Asignacion_Memoria(nombre, size_memory, inicio, Paginas);
                        inicio += size_memory;
                        Cola_Procesos.add(proceso);
                    }
                    else espacio=false;
                    break;
                case 4:
                    nombre="Contar"+contadorContar+".exe";
                    contadorContar++;
                    proceso=new PROCESOS(nombre, id_process, instructions, Paginas, opcion, Dir_base, Dir_limite, Estado);
                    if(Espacio_Suficiente(size_memory)) {
                        Asignacion_Memoria(nombre, size_memory, inicio, Paginas);
                        inicio += size_memory;
                        Cola_Procesos.add(proceso);
                    }
                    else espacio=false;
                    break;
                case 5:
                    nombre="Imprimir"+contadorImprimir+".exe";
                    contadorImprimir++;
                    proceso=new PROCESOS(nombre, id_process, instructions, Paginas, opcion, Dir_base, Dir_limite, Estado);
                    if(Espacio_Suficiente(size_memory)) {
                        Asignacion_Memoria(nombre, size_memory, inicio, Paginas);
                        inicio += size_memory;
                        Cola_Procesos.add(proceso);
                    }
                    else espacio=false;
                    break;
            }
        }while(espacio);
    }/*ESCRITO FINALIZADO*/

    /*Esta función lo que hace es llenar las localidades de memoria respecto a los procesos que se vayan generando para
    * tener control de estos*/
    public static void Asignacion_Memoria(String nombre, int size_memory, int inicio, LinkedList<ArrayList<String>> Paginas){
        int j=0, k=0;
        if(Espacio_Suficiente(size_memory)) {
            for (int i = inicio; i < inicio+size_memory; i++) {
                Paginas.get(j).set(1, Integer.toString(i));
                MEMORIA[i] = nombre;
                j++;
                if(j==Paginas.size())
                    break;
            }
        }
    }/*ESCRITO FINALIZADO*/

    /*Esta función lo que se hace es corroborar que haya espacio suficiente en medio o al final de la memoria para poder
    * ingresar un nuevo proceso*/
    public static boolean Espacio_Suficiente(int size_memory){
        int i;
        j=k=0;
        boolean tentativo=true, regreso=false;

        for(i=0; i<1024; i++){
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
                if(((k-j)+1)>=size_memory){
                    regreso=true;
                    LocalidadesDeMemoria.add(j);
                    LocalidadesDeMemoria.add(k);
                    return regreso;
                }
                else
                    tentativo=true;
            }
        }
        if((k-j)+1>=size_memory) {
            regreso = true;
            LocalidadesDeMemoria.add(j);
            LocalidadesDeMemoria.add(k);
        }
        return regreso;
    }/*ESCRITO FINALIZADO*/

    /*Menú del SO*/
    public static void MENU_SO(){
        char opcion;
        PROCESOS proceso=new PROCESOS(), ListadoFinalizado, ListadoEliminado;
        int i=0, SizeEliminados, SizeConcluidos;

        do{
            System.out.println("\n\ta) Crear Proceso\n\tb) Ver Estado Actual del Sistema");
            System.out.println("\tc) Imprimir Cola de Procesos\n\td) Ver Proceso Actual");
            System.out.println("\te) Ejecutar Proceso Actual\n\tf) Pasar al Proceso Siguiente");
            System.out.println("\tg) Matar Proceso Actual\n\th) Ver estad Actual de la memoria");
            System.out.println("\ti) Desfragmentar la memoria\n\tj) Cerrar Programa\n\tElecci\u00F3n: ");
            opcion=Teclado.next().charAt(0);
            switch (opcion) {
                case 'a':
                case 'A':
                    MENU_PROCESOS();
                    break;
                case 'b':
                case 'B':
                    i = 0;
                    SizeEliminados = Procesos_Matados.size();
                    SizeConcluidos = Procesos_Concluidos.size();
                    System.out.println("N\u00FAmeros de Procesos en la Cola: " + Cola_Procesos.size());
                    System.out.println("\nProcesos Eliminados\tProcesos Finalizados");
                    do {
                        if (i < SizeConcluidos)
                            ListadoFinalizado = Procesos_Concluidos.get(i);
                        else
                            ListadoFinalizado = new PROCESOS("");
                        if (i < SizeEliminados)
                            ListadoEliminado = Procesos_Matados.get(i);
                        else
                            ListadoEliminado = new PROCESOS("");
                        /*Imprime las dos columnas*/
                        if (ListadoEliminado.process.getName().compareTo("") != 0 && ListadoFinalizado.process.getName().compareTo("") != 0)
                            System.out.println(ListadoEliminado.process.getName() + "\t\t" + ListadoFinalizado.process.getName());
                        else {
                            /*Imprime la primera columna*/
                            if (ListadoEliminado.process.getName().compareTo("") != 0 && ListadoFinalizado.process.getName().compareTo("") == 0)
                                System.out.println(ListadoEliminado.process.getName());
                            else
                                /*Imprime la segunda columna*/
                                System.out.println("\t\t\t\t\t" + ListadoFinalizado.process.getName());
                        }
                        i++;
                    } while (ListadoFinalizado.process.getName().compareTo("") != 0 || ListadoEliminado.process.getName().compareTo("") != 0);
                    Impresion_Memoria();
                    break;
                case 'c':
                case 'C':
                    IMPRIMIR_COLA();
                    break;
                case 'd':
                case 'D':
                    proceso = Cola_Procesos.element();
                    System.out.println("PROCESO ACTUAL");
                    System.out.println("Nombre: " + proceso.process.getName() + " ID Process: " + proceso.id_process + " Instrucciones Totales: " + proceso.num_instructions);
                    System.out.println("Instrucciones ejecutadas: " + proceso.num_instructions_executed+" N\u00FAmero de p\u00E1ginas: "+proceso.Paginas.size());
                    System.out.println("Tabla de Paginaci\u00F3n:\n");
                    System.out.println("N\u00FAmero de p\u00E1gina  \tLocalidad de Memoria Asignada");
                    System.out.println("-------------------------------------------------");
                    for(ArrayList<String> iterador: proceso.Paginas) {
                        System.out.println("\t\t" + iterador.get(0) + "\t\t\t\t\t\t" + iterador.get(1));
                        System.out.println("-------------------------------------------------");
                    }
                    break;
                case 'e':
                case 'E':
                    EJECUCION_PROCESOS(proceso);
                    ASIGNACION_LIBERACION();
                    break;
                case 'f':
                case 'F':
                    Cola_Procesos.element().Estado = "Inactivo";
                    Cola_Procesos.add(Cola_Procesos.poll());
                    Cola_Procesos.element().Estado = "Activo";
                    break;
                case 'g':
                case 'G':
                    ELIMINAR_PROCESOS(proceso);
                    ASIGNACION_LIBERACION();
                    break;
                case 'h':
                case 'H':
                    LLENADO_LOCALIDADES_MEMORIA();
                    IMPRESION_LOCALIDADES_MEMORIA();
                    break;
                case 'i':
                case 'I':
                    Desfragmentacion();
                    break;
                case 'j':
                case 'J':
                    INICIO_MEMORIA();
                    Cola_Procesos.clear();
                    break;
                default:
                    System.out.println("Por favor escoja una de las opciones establecidas");
            }
        }while(opcion!='j'&&opcion!='J');
    }/*ESCRITO FINALIZADO*/

    /*Impresion del estado de la memoria*/
    public static void Impresion_Memoria(){
        System.out.println("\nIMPRESION MEMORIA\nLocalidad\t\tProceso");
        for(int i=0; i< MEMORIA.length;i++)
            System.out.println(i+"\t\t"+ MEMORIA[i]);
    }/*ESCRITO FINALIZADO*/

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
    }/*ESCRITO FINALIZADO*/

    /*Asignación N° instrucciones*/
    public static int NUMERO_INSTRUCCIONES(){
        int opcion=(int)(Math.random()*20+10);
        return opcion;
    }/*ESCRITO FINALIZADO*/

    /*Impresión de la cola de procesos*/
    public static void IMPRIMIR_COLA(){
        System.out.println("PROCESOS EN COLA");
        for (PROCESOS impresion:Cola_Procesos) {
            if (impresion.process.getName().length() != 14&&impresion.process.getName().length()!=13) {
                if(impresion.id_process<1000) {
                    System.out.print("Nombre: " + impresion.process.getName() + "\t\tID Process: " + impresion.id_process + "\t\tInstrucciones restantes: ");
                    System.out.println((impresion.num_instructions - impresion.num_instructions_executed) + "\t\tEstado: " + impresion.Estado);
                }
                else{
                    System.out.print("Nombre: " + impresion.process.getName() + "\t\tID Process: " + impresion.id_process + "\tInstrucciones restantes: ");
                    System.out.println((impresion.num_instructions - impresion.num_instructions_executed) + "\t\tEstado: " + impresion.Estado);
                }
            }
            else {
                if(impresion.id_process<1000) {
                    System.out.print("Nombre: " + impresion.process.getName() + "\tID Process: " + impresion.id_process + "\t\tInstrucciones restantes: ");
                    System.out.println((impresion.num_instructions - impresion.num_instructions_executed) + "\t\tEstado: " + impresion.Estado);
                }
                else{
                    System.out.print("Nombre: " + impresion.process.getName() + "\tID Process: " + impresion.id_process + "\tInstrucciones restantes: ");
                    System.out.println((impresion.num_instructions - impresion.num_instructions_executed) + "\t\tEstado: " + impresion.Estado);
                }
            }
            System.out.println();
        }
    }/*ESCIRTO FINALIZADO*/

    /*Eliminación de Procesos de la Cola*/
    public static void ELIMINAR_PROCESOS(PROCESOS proceso){
        proceso=Cola_Procesos.poll();
        proceso.Estado="Eliminado";
        Procesos_Matados.add(proceso);
        for(ArrayList<String> buscador:proceso.Paginas)
            MEMORIA[Integer.parseInt(buscador.get(1))]="";
        System.out.print("\nEl proceso eliminado le faltaban: "+(proceso.num_instructions-proceso.num_instructions_executed));
        System.out.println(" instrucciones para terminar su ejecuci\u00F3n");
        Cola_Procesos.element().Estado="Activo";
    }/*ESCRITO FINALIZADO*/

    /*Ejecución de procesos*/
    public static void EJECUCION_PROCESOS(PROCESOS proceso){
        proceso=PROCESOS.Selection_Process(Cola_Procesos.poll());
        if(proceso.num_instructions_executed==proceso.num_instructions) {
            Cola_Procesos.element().Estado="Activo";
            proceso.Estado="Terminado";
            Procesos_Concluidos.add(proceso);
            for(ArrayList<String> buscador:proceso.Paginas)
                MEMORIA[Integer.parseInt(buscador.get(1))]="";
        }
        else {
            proceso.Estado="Inactivo";
            Cola_Procesos.add(proceso);
            Cola_Procesos.element().Estado="Activo";
        }
    }/*ESCRTIO FINALIZADO*/

    /*Cuando se libera la memoria durante la ejecución de un proceso (fuera de la creación de uno), o sea en el método
    * MENU_SO(), o la eliminación de uno este método nos ayudará a revisar toda la cola de procesos en espera para ver
    * si uno o varios pueden pasar a la cola de procesos y ocupar espacio en memoria*/
    public static void ASIGNACION_LIBERACION(){
        int i;
        PROCESOS proceso;
        boolean espacio=false;

        for (i = 0; i < Procesos_espera_memoria.size(); i++) {
            proceso = Procesos_espera_memoria.element();
            if (Espacio_Suficiente(proceso.size_process)||Espacio_Suficiente_Huecos(proceso.size_process)) {
                proceso = Procesos_espera_memoria.poll();
                System.out.println("El proceso " + proceso.process.getName() + ", que se encontraba en espera, se pudo crear con \u00E9xito, se ha agregado a la cola y a la memoria.");
                //proceso.Dir_base = j;
                //proceso.Dir_limite = j + proceso.size_process - 1;
                Cola_Procesos.add(proceso);
                Asignacion_Memoria(proceso.process.getName(), proceso.size_process, Integer.parseInt(proceso.Paginas.get(0).get(0)), proceso.Paginas);
                espacio = true;
                i=0;
            } else
                Procesos_espera_memoria.add(Procesos_espera_memoria.poll());
        }
        if (!espacio&&!Procesos_espera_memoria.isEmpty())
            System.out.println("A\u00FAn no hay espacio suficiente para agregar los procesos en espera.");
    }/*ESCRITO FINALIZADO*/

    public static void LLENADO_LOCALIDADES_MEMORIA(){
        LocalidadesMemoria.clear();
        ArrayList<String>Arreglo=new ArrayList<>();
        String nombre;
        nombre=MEMORIA[0];
        if(MEMORIA[0].compareTo("")!=0)
            Arreglo.add("P");
        else
            Arreglo.add("H");
        Arreglo.add("0");
        int j=0;
        for(int i=0; i<MEMORIA.length; i++){
            if(MEMORIA[i].compareTo(nombre)!=0&&MEMORIA[i].compareTo("")!=0) {
                nombre=MEMORIA[i];
                Arreglo.add(Integer.toString(i-j));
                LocalidadesMemoria.add(Arreglo);
                Arreglo=new ArrayList<>();
                Arreglo.add("P");
                Arreglo.add(Integer.toString(i));
                j=i;
            }
            if(MEMORIA[i].compareTo(nombre)!=0&&MEMORIA[i].compareTo("")==0) {
                nombre=MEMORIA[i];
                Arreglo.add(Integer.toString(i-j));
                LocalidadesMemoria.add(Arreglo);
                Arreglo=new ArrayList<>();
                Arreglo.add("H");
                Arreglo.add(Integer.toString(i));
                j=i;
            }
            if(MEMORIA[i].compareTo("")==0&&(i+1)<MEMORIA.length&&MEMORIA[i+1].compareTo("")!=0){
                nombre=MEMORIA[i+1];
                Arreglo.add(Integer.toString(i-j+1));
                LocalidadesMemoria.add(Arreglo);
                Arreglo=new ArrayList<>();
                Arreglo.add("P");
                Arreglo.add(Integer.toString(i+1));
                j=i+1;
            }
            if(i==(MEMORIA.length-1)&&MEMORIA[i].compareTo("")==0){
                Arreglo.add(Integer.toString(i-j));
                LocalidadesMemoria.add(Arreglo);
            }
            if (i==1023&&MEMORIA[i].compareTo("")!=0){
                Arreglo.add(Integer.toString(i-j+1));
                LocalidadesMemoria.add(Arreglo);
            }
        }
    }

    public static void IMPRESION_LOCALIDADES_MEMORIA(){
        int i=0, z=0;
        for (ArrayList<String> impresion:LocalidadesMemoria) {
            System.out.print('|'+impresion.get(0)+'|'+impresion.get(1)+'|'+impresion.get(2)+'|');
            i++;
            if(z!=LocalidadesMemoria.size()-1)
                System.out.print("\u21D2");
            if(i==12){
                System.out.println("\n");
                i=0;
            }
            z++;
        }
    }

    public static LinkedList<ArrayList<String>> PAGINACION(int sizememory){
        LinkedList<ArrayList<String>> Paginas=new LinkedList<>();
        ArrayList<String> tupla;
        int NumPaginas=sizememory/16;
        for(int i=0; i<NumPaginas; i++){
            tupla=new ArrayList<>();
            tupla.add(Integer.toString(i+1));
            tupla.add("");
            Paginas.add(tupla);
        }
        return Paginas;
    }

    public static boolean Espacio_Suficiente_Huecos(int size_memory){
        int espacio=0;
        ArrayList<Integer> Localidades_vacias=new ArrayList<>();

        for(int i=0; i<MEMORIA.length; i++){
            if(MEMORIA[i].compareTo("")==0){
                espacio++;
                Localidades_vacias.add(i);
            }
            if(espacio==size_memory)
                return true;
        }
        return false;
    }

    public static void Asignacion_Memoria_Huecos(String nombre, int size_memory, LinkedList<ArrayList<String>> Paginas){
        if(Espacio_Suficiente_Huecos(size_memory)){
            LinkedList<Integer> EspaciosVacios=new LinkedList<>();
            for(int i=0; i<MEMORIA.length; i++){
                if(MEMORIA[i].compareTo("")==0)
                    EspaciosVacios.add(i);
                if(EspaciosVacios.size()==size_memory) {
                    for(Integer buscador:EspaciosVacios){
                        MEMORIA[buscador]=nombre;
                    }
                    break;
                }
            }
        }
    }

    public static void Desfragmentacion(){
        String procesoactual, head;
        LinkedList<String> LocalidadesLlenas=new LinkedList<>();
        LinkedList<Integer> Localidades;
        int posicon_vacia, posicion=0, num, conteo;
        boolean primera=true;
        PROCESOS HEAD;

        procesoactual=MEMORIA[0];
        for(posicon_vacia=0; posicon_vacia<MEMORIA.length; posicon_vacia++){
            if(MEMORIA[posicon_vacia].compareTo("")==0&&primera) {
                posicion = posicon_vacia;
                primera=false;
            }
            if(MEMORIA[posicon_vacia].compareTo("")!=0&&!primera){
                MEMORIA[posicion]=MEMORIA[posicon_vacia];
                MEMORIA[posicon_vacia]="";
                posicon_vacia=posicion;
                primera=true;
            }
        }
        head=Cola_Procesos.element().process.getName();
        for(posicon_vacia=0; posicon_vacia<MEMORIA.length; posicon_vacia++){
            procesoactual=MEMORIA[posicon_vacia];
            num = 0;
            Localidades=new LinkedList<>();
            for (posicion = 0; posicion < MEMORIA.length; posicion++) {
                if (MEMORIA[posicion].compareTo(procesoactual) == 0) {
                    num++;
                    Localidades.add(posicion);
                }
            }
            PROCESOS buscador;
            for(posicion = 0; posicion < Cola_Procesos.size(); posicion++){
                buscador=Cola_Procesos.poll();
                if(buscador.process.getName().compareTo(procesoactual)==0){
                    for(conteo=0; conteo<num; conteo++)
                        buscador.Paginas.get(conteo).set(1, Integer.toString(Localidades.get(conteo)));
                }
                Cola_Procesos.add(buscador);
            }
        }
        HEAD=Cola_Procesos.element();
        for(posicion=0; posicion<Cola_Procesos.size(); posicion++){
            if(HEAD.process.getName().compareTo(head)!=0){
                HEAD=Cola_Procesos.poll();
                Cola_Procesos.add(HEAD);
                HEAD=Cola_Procesos.element();
            }
            else
                break;
        }
    }
}
