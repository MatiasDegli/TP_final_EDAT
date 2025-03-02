import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import Estructuras.AVL;
import Estructuras.GrafoEtiquetado;
import Estructuras.Lista;
import Estructuras.MapeoAMuchos;
import TDAs.Ciudad;
import TDAs.Equipo;
import TDAs.EquipoGoles;
import TDAs.Partido;
import TDAs.PartidoKey;

public class CopaAmerica2024 {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        Scanner scanner = new Scanner(System.in);

        GrafoEtiquetado ciudades = new GrafoEtiquetado();
        AVL equipos = new AVL();
        MapeoAMuchos partidos = new MapeoAMuchos();

        String texto = "CopaAmerica2024.txt";

        System.out.println("Presione cualquier caracter para iniciar la carga o 1 si quiere finalizar el programa");
        String inicio = scanner.nextLine();

        if (inicio != "1") {
            cargarDatos(texto, equipos, partidos, ciudades);
            mostrarSistema(ciudades, equipos, partidos);
        }
    }

    public static void cargarDatos(String texto, AVL equipos, MapeoAMuchos partidos, GrafoEtiquetado ciudades)
            throws FileNotFoundException, IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(texto))) {

            String linea;

            while ((linea = br.readLine()) != null) {

                String[] datosLinea = linea.split(";");

                if (datosLinea[0].equals("E")) {
                    cargarEquipo(datosLinea, equipos);
                } else if (datosLinea[0].equals("P")) {
                    cargarPartido(datosLinea, partidos, equipos);
                } else if (datosLinea[0].equals("C")) {
                    cargarCiudad(datosLinea, ciudades);
                } else if (datosLinea[0].equals("R")) {
                    cargarRuta(datosLinea, ciudades);
                }
            }
        }
    }

    public static void cargarCiudad(String[] datos, GrafoEtiquetado ciudades) {
        String nombre = datos[1];
        boolean disponibilidad = datos[2].equals("TRUE");
        boolean sede = datos[3].equals("TRUE");

        Ciudad ciudad = new Ciudad(nombre, disponibilidad, sede);

        ciudades.insertarVertice(ciudad);
    }

    public static void cargarEquipo(String[] datos, AVL equipos) {
        String nombre = datos[1];
        String DT = datos[2];
        char grupo = datos[3].charAt(0);

        Equipo equipo = new Equipo(nombre, DT, grupo);

        equipos.insertar(equipo);
    }

    public static void cargarPartido(String[] datos, MapeoAMuchos partidos, AVL equipos) {
        String eq1 = datos[1];
        String eq2 = datos[2];

        PartidoKey key = new PartidoKey(eq1, eq2);

        Equipo equipo1 = new Equipo(eq1, "xx", 'X');
        Equipo equipo2 = new Equipo(eq2, "xx", 'X');

        String ins = datos[3];
        String ciudad = datos[4];
        String estadio = datos[5];
        int gol1 = Integer.parseInt(datos[6]);
        int gol2 = Integer.parseInt(datos[7]);

        equipo1 = (Equipo) equipos.obtenerIgual(equipo1);
        equipo2 = (Equipo) equipos.obtenerIgual(equipo2);

        equipo1.sumarGoles(gol1);
        equipo1.sumarEnContra(gol2);
        equipo2.sumarGoles(gol2);
        equipo2.sumarEnContra(gol1);

        if (ins.equals("GRUPO")) {
            if (gol1 > gol2) {
                equipo1.sumarPuntos(3);
            } else if (gol2 > gol1) {
                equipo2.sumarPuntos(3);
            } else {
                equipo1.sumarPuntos(1);
                equipo2.sumarPuntos(1);
            }
        }

        Partido partido = new Partido(ins, ciudad, estadio, gol1, gol2);

        partidos.insertar(key);
        partidos.asociar(key, partido);
    }

    public static void cargarRuta(String[] datos, GrafoEtiquetado ciudades) {
        String c1 = datos[1];
        String c2 = datos[2];
        int mins = Integer.parseInt(datos[3]);

        Ciudad origen = (Ciudad) ciudades.obtenerCiudad(new Ciudad(c1, false, false));
        Ciudad destino = (Ciudad) ciudades.obtenerCiudad(new Ciudad(c2, false, false));

        ciudades.insertarArco(origen, destino, mins);
    }

    public static void menuCiudades(GrafoEtiquetado ciudades) {
        Scanner scanner = new Scanner(System.in);
        String opcion = "";

        while (opcion != "8") {
            System.out.println(menuCiudadAux());

            opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    agregarCiudad(ciudades);
                    break;

                case "2":
                    eliminarCiudad(ciudades);
                    break;

                case "3":
                    modificarCiudad(ciudades);
                    break;

                case "4":
                    menorEscala(ciudades);
                    break;

                case "5":
                    menorTiempo(ciudades);
                    break;

                case "6":
                    menorTiempoAlt(ciudades);
                    break;

                case "7":
                    caminosCompleto(ciudades);
                    break;

                case "8":

                    break;

                default:
                    System.out.println("Opción invalida");
                    break;
            }
        }
    }

    private static String menuCiudadAux() {
        String mensaje = "Ingrese 1 para agregar una ciudad.\n" +
                "Ingrese 2 para eliminar una ciudad.\n" +
                "Ingrese 3 para modificar una ciudad\n" +
                "Ingrese 4 si quiere conocer el camino de una ciudad a otra con la menor cantidad de escalas posibles\n"
                +
                "Ingrese 5 si quiere conocer el camino de una ciudad a otra con el menor tiempo de vuelo posible\n" +
                "Ingrese 6 si quiere conocer el camino de una ciudad a otra con el menor tiempo de vuelo posible sin pasar por"
                +
                "una ciudad dada\n" +
                "Ingrese 7 si quiere ver todos los caminos posibles entre dos ciudades dadas\n" +
                "Ingrese 8 si quiere salir del menu";

        return mensaje;
    }

    public static void agregarCiudad(GrafoEtiquetado ciudades) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el nombre de la ciudad");
        String nombre = scanner.nextLine();
        System.out.println("Indique la disponibilidad");
        boolean disponibilidad = ("TRUE".equals(scanner.nextLine().toUpperCase()));
        System.out.println("Indique si es sede");
        boolean sede = ("TRUE".equals(scanner.nextLine().toUpperCase()));

        Ciudad ciudad = new Ciudad(nombre, disponibilidad, sede);

        if (!ciudades.existeVertice(ciudad)) {
            ciudades.insertarVertice(ciudad);
        }
    }

    public static void eliminarCiudad(GrafoEtiquetado ciudades) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el nombre de la ciudad");
        String nombre = scanner.nextLine();

        Ciudad ciudad = new Ciudad(nombre, false, false);
        ciudad = ciudades.obtenerCiudad(ciudad);

        if (ciudad != null) {
            ciudades.eliminarVertice(ciudad);
            System.out.println("La ciudad fue eliminada con exito");
        } else {
            System.out.println("La ciudad no es parte del mapa");
        }
    }

    public static void modificarCiudad(GrafoEtiquetado ciudades) {
        Scanner scanner = new Scanner(System.in);
        int i = 0;

        System.out.println("Ingrese el nombre de la ciudad");
        String nombre = scanner.nextLine();
        Ciudad ciudad = new Ciudad(nombre, false, false);
        ciudad = ciudades.obtenerCiudad(ciudad);

        if (ciudad != null) {
            String opcion = "";

            while (opcion != "3") {

                System.out.println(
                        "Ingrese 1 si desea modificar la disponibilidad, 2 si desea modificar si la ciudad es o no es sede, "
                                + "3 si quiere salir del menu");
                opcion = scanner.nextLine();

                switch (opcion) {
                    case "1":
                        System.out.println("Ingrese true si hay disponibilidad, de lo contrario ingrese false");
                        String disp = scanner.nextLine().toUpperCase();
                        ciudad.setDisponibilidad(disp.equals("TRUE"));
                        break;

                    case "2":
                        System.out.println("Ingrese true si es sede, de lo contrario ingrese false");
                        String sede = scanner.nextLine().toUpperCase();
                        ciudad.setSede(sede.equals("TRUE"));
                        break;

                    case "3":
                        break;

                    default:
                        System.out.println("Cadena inválida");
                        break;
                }
            }
            System.out.println("La ciudad se modifico con exito");
        } else {
            System.out.println("La ciudad no es parte del mapa");
        }
    }

    public static void menuEquipos(AVL equipos, MapeoAMuchos partidos) {
        Scanner scanner = new Scanner(System.in);
        String opcion = "";

        while (opcion != "4") {
            System.out.println(menuEquipoAux());

            opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    agregarEquipo(equipos);
                    break;

                case "2":
                    eliminarEquipo(equipos);
                    break;

                case "3":
                    modificarEquipo(equipos);
                    break;

                case "4":
                    datosPais(equipos, partidos);
                    break;

                case "5":
                    rangoAlfabetico(equipos);
                    break;

                case "6":
                    listaGoles(equipos);
                    break;

                case "7":
                    break;

                default:
                    System.out.println("Opción invalida");
                    break;
            }
        }
    }

    private static String menuEquipoAux() {
        String mensaje = "Ingrese 1 para agregar un equipo.\n" +
                "Ingrese 2 para eliminar un equipo.\n" +
                "Ingrese 3 para modificar un equipo\n" +
                "Ingrese 4 si quiere ver los datos de una selección\n" +
                "Ingrese 5 si quiere ver los equipos por orden alfabético\n" +
                "Ingrese 6 si quiere ver los equipos por orden de goles a favor\n" +
                "Ingrese 7 si quiere salir del menu";

        return mensaje;
    }

    public static void agregarEquipo(AVL equipos) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el nombre del pais");
        String pais = scanner.nextLine();

        System.out.println("Ingresar nombre del DT");
        String dt = scanner.nextLine();

        System.out.println("Ingrese el grupo al que pertenece");
        char grupo = scanner.nextLine().charAt(0);

        Equipo equipo = new Equipo(pais, dt, grupo);

        equipos.insertar(equipo);

    }

    public static void eliminarEquipo(AVL equipos) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el nombre del equipo");
        String nombre = scanner.nextLine();
        Equipo equipo = new Equipo(nombre, "xx", 'X');
        equipo = (Equipo) equipos.obtenerIgual(equipo);

        if (equipo != null) {
            equipos.eliminar(equipo);
            System.out.println("El equipo fue eliminado con exito");
        } else {
            System.out.println("El equipo no es parte del fixture");
        }
    }

    public static void modificarEquipo(AVL equipos) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el nombre del equipo que desea modificar");
        String nombre = scanner.nextLine().toUpperCase();

        Equipo equipo = new Equipo(nombre, "xx", 'X');
        equipo = (Equipo) equipos.obtenerIgual(equipo);

        if (equipo != null) {
            String opcion = "";

            while (opcion != "3") {

                System.out.println("Ingrese 1 si desea modificar el DT, 2 si desea modificar el grupo, "
                        + "3 si quiere salir del menu");
                opcion = scanner.nextLine();

                switch (opcion) {
                    case "1":
                        System.out.println("Ingrese el nombre del DT");
                        String dir = scanner.nextLine();
                        equipo.setDT(dir);
                        break;

                    case "2":
                        System.out.println("Ingrese el nuevo grupo");
                        break;

                    default:
                        System.out.println("Cadena inválida");
                        break;
                }
            }
            System.out.println("El equipo se modifico con exito");
        } else {
            System.out.println("El equipo no es parte del fixture");
        }

    }

    public static void menuPartidos(MapeoAMuchos partidos, AVL equipos) {
        Scanner scanner = new Scanner(System.in);
        String opcion = "";

        while (opcion != "4") {
            System.out.println(menuPartidoAux());

            opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    agregarPartido(partidos, equipos);
                    break;

                case "2":
                    resultados(partidos);
                    break;

                case "3":

                    break;

                default:
                    System.out.println("Opción invalida");
                    break;
            }
        }
    }

    private static String menuPartidoAux() {
        String mensaje = "Ingrese 1 para agregar un partido.\n" +
                "Ingrese 2 para ver los resultados entre dos equipos dados.\n" +
                "Ingrese 3 si quiere salir del menu";

        return mensaje;
    }

    public static void agregarPartido(MapeoAMuchos partidos, AVL equipos) {
        Scanner scanner = new Scanner(System.in);

        String muestraEquipos = "";
        Lista colEquipos = equipos.listar();

        for (int i = 0; i < colEquipos.longitud(); i++) {
            muestraEquipos += colEquipos.recuperar(i) + ": " + (i + 1) + "; ";
        }

        System.out.println(muestraEquipos);
        System.out.println("Ingrese el número del equipo 1");
        int eleccion1 = scanner.nextInt();
        Equipo eq1 = (Equipo) colEquipos.recuperar(eleccion1);
        String nomEq1 = eq1.getNombre();

        System.out.println(muestraEquipos);
        System.out.println("Ingrese el número del equipo 2");
        int eleccion2 = scanner.nextInt();

        if (eleccion2 != eleccion1) {
            Equipo eq2 = (Equipo) colEquipos.recuperar(eleccion2);
            String nomEq2 = eq2.getNombre();

            if (nomEq1.compareTo(nomEq2) > 0) {
                // Intercambio los equipos ya que se debe respetar el orden alfabetico
                String temp = nomEq1;
                nomEq1 = nomEq2;
                nomEq2 = temp;
            }

            System.out.println("Ingrese la instancia del partido");
            String ins = scanner.nextLine();

            System.out.println("Ingrese la ciudad en la que se jugó el partido");
            String ciu = scanner.nextLine();

            System.out.println("Ingrese el nombre del estadio");
            String estadio = scanner.nextLine();

            System.out.println("Ingrese la cantidad de goles que marcó " + nomEq1);
            int golEq1 = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Ingrese la cantidad de goles que marcó " + nomEq2);
            int golEq2 = scanner.nextInt();

            PartidoKey dominio = new PartidoKey(nomEq1, nomEq2);
            Partido partido = new Partido(ins, ciu, estadio, golEq1, golEq2);

            partidos.insertar(dominio);
            partidos.asociar(dominio, partido);
        } else {
            System.out.println("Partido inválido");
        }
    }

    public static void datosPais(AVL equipos, MapeoAMuchos partidos) {
        /*
         * Dado un país, mostrar puntos ganados, goles a favor y en contra y diferencia
         * de goles
         */
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el nombre del pais que desea conocer sus datos");
        String nombre = scanner.nextLine().toUpperCase();

        Equipo equipo = new Equipo(nombre, "xx", 'X');
        equipo = (Equipo) equipos.obtenerIgual(equipo);

        if (equipo != null) {
            System.out.println(equipo.datos());
        } else {
            System.out.println("El equipo ingresado no es parte del fixture");
        }
    }

    public static void rangoAlfabetico(AVL equipos) {
        /*
         * Dadas dos cadenas (min y max) devolver todos los equipos cuyo nombre esté
         * alfabéticamente en el rango [min, max].
         */
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese la cadena de menor orden alfabético");
        String min = scanner.nextLine();

        System.out.println("Ingrese la cadena de mayor orden alfabético");
        String max = scanner.nextLine();

        Equipo piso = new Equipo(min, "", 'X');
        Equipo techo = new Equipo(max, "", 'X');

        System.out.println(equipos.listarRango(piso, techo).toString());
    }

    public static void resultados(MapeoAMuchos partidos) {
        /*
         * Dados 2 equipos, si jugaron algún partido entre sí, mostrar los resultados.
         */
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el primer equipo");
        String eq1 = scanner.nextLine();
        System.out.println("Ingrese el segundo equipo");
        String eq2 = scanner.nextLine();

        if (eq1.compareTo(eq2) > 0) {
            String aux = eq1;
            eq1 = eq2;
            eq2 = aux;
        }

        if (eq1.compareTo(eq2) == 0) {
            System.out.println();
        } else {
            PartidoKey dominio = new PartidoKey(eq1, eq2);

            if (partidos.pertenece(dominio)) {
                System.out.println(partidos.obtenerValor(dominio));
            }
        }
    }

    public static void menorEscala(GrafoEtiquetado ciudades) {
        /*
         * Obtener el camino que llegue de A a B pasando por la mínima cantidad de
         * ciudades
         */
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el nombre de la ciudad origen");
        String c1 = scanner.nextLine();
        System.out.println("Ingrese el nombre de la ciudad destino");
        String c2 = scanner.nextLine();

        Ciudad origen = new Ciudad(c1, false, false);
        Ciudad destino = new Ciudad(c2, false, false);

        System.out.println(ciudades.caminoMasCorto(origen, destino).toString());
    }

    public static void menorTiempo(GrafoEtiquetado ciudades) {
        /* Obtener el camino que llegue de A a B de menor tiempo */
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el nombre de la ciudad origen");
        String c1 = scanner.nextLine();
        System.out.println("Ingrese el nombre de la ciudad destino");
        String c2 = scanner.nextLine();

        Ciudad origen = new Ciudad(c1, false, false);
        Ciudad destino = new Ciudad(c2, false, false);

        System.out.println(ciudades.caminoMenorTiempo(origen, destino).toString());
    }

    public static void menorTiempoAlt(GrafoEtiquetado ciudades) {
        /*
         * El camino más corto en minutos de vuelo para llegar de A a B y que no pase
         * por una ciudad C dada
         */
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el nombre de la ciudad origen");
        String c1 = scanner.nextLine();
        System.out.println("Ingrese el nombre de la ciudad destino");
        String c2 = scanner.nextLine();
        System.out.println("Ingrese el nombre de la ciudad a evitar");
        String c3 = scanner.nextLine();

        Ciudad origen = new Ciudad(c1, false, false);
        Ciudad destino = new Ciudad(c2, false, false);
        Ciudad evitar = new Ciudad(c3, false, false);

        System.out.println(ciudades.caminoRapidoAlt(origen, destino, evitar).toString());
    }

    public static void caminosCompleto(GrafoEtiquetado ciudades) {
        /*
         * Obtener todos los caminos posibles para llegar de A a B, mostrarlos y luego
         * filtrar y mostrar solo los que haya
         * posibilidad de conseguir alojamiento en la ciudad destino o en alguna ciudad
         * por la que tenga que pasar camino a ella
         */
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el nombre de la ciudad origen");
        String c1 = scanner.nextLine();
        System.out.println("Ingrese el nombre de la ciudad destino");
        String c2 = scanner.nextLine();

        Ciudad origen = new Ciudad(c1, false, false);
        Ciudad destino = new Ciudad(c2, false, false);

        Lista caminos = ciudades.caminoMenorTiempo(origen, destino);

        System.out.println("Todos los caminos posibles desde " + c1 + " hasta " + c2 + ", son: ");

        Lista conAlojamiento = new Lista();

        for (int i = 0; i < caminos.longitud(); i++) {
            Lista camino = (Lista) caminos.recuperar(i);
            System.out.println(camino.toString());

            if (camino.tieneAlojamiento()) {
                conAlojamiento.insertar(conAlojamiento.longitud() + 1, camino);
            }
        }

        if (conAlojamiento.esVacia()) {
            System.out.println("No hay ningun camino con alojamiento disponible");
        } else {
            System.out.println("Los caminos que tienen alojamiento son: ");

            for (int i = 0; i < conAlojamiento.longitud(); i++) {
                Lista camino = (Lista) conAlojamiento.recuperar(i);
                System.out.println(camino.toString());
            }
        }
    }

    public static void listaGoles(AVL equipos) {
        Lista listaEquipos = equipos.listar();
        AVL equiposPorGol = new AVL();

        for (int i = 0; i < listaEquipos.longitud(); i++) {
            EquipoGoles equipo = new EquipoGoles((Equipo) listaEquipos.recuperar(i));
            equiposPorGol.insertar(equipo);
        }

        System.out.println("La lista de los equipos en orden de mas cantidad de goles a favor a menor es: "
                + equiposPorGol.toString());
        equiposPorGol.vaciar();
        listaEquipos.vaciar();
    }

    private static void mostrarSistema(GrafoEtiquetado ciudades, AVL equipos, MapeoAMuchos partidos) {
        /*
         * Mostrar sistema: es una operación de debugging que permite ver todas las
         * estructuras utilizadas con su contenido
         * (grafo, AVL y Mapeo) para verificar, en cualquier momento de la ejecución del
         * sistema, que se encuentren cargadas correctamente.
         */

        System.out.println("Grafo de ciudades: " + ciudades.toString());
        System.out.println("AVL de equipos: " + equipos.toString());
        System.out.println("Mapeo de partidos: " + partidos.toString());
    }

}
