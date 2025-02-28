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
import TDAs.Partido;
import TDAs.PartidoKey;

public class CopaAmerica2024 {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        GrafoEtiquetado ciudades = new GrafoEtiquetado();
        AVL equipos = new AVL();
        MapeoAMuchos partidos = new MapeoAMuchos();
        Lista colCiudades = new Lista();
        Lista colEquipos = new Lista();

        String texto = "CopaAmerica2024.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(texto))) {

            String linea;

            while ((linea = br.readLine()) != null) {

            }

        } catch (IOException ex) {
            System.out.println("Error al leer el archivo");
        }
    }

    public static void cargarDatos(String datos, AVL equipos, MapeoAMuchos partidos, GrafoEtiquetado ciudades,
            Lista colCiudades) throws FileNotFoundException, IOException {

        try (BufferedReader bufferLectura = new BufferedReader(new FileReader(datos))) {

            String linea;

            while ((linea = bufferLectura.readLine()) != null) {

                String[] datosLinea = linea.split(";");

                if (datosLinea[0].equals("E")) {
                    cargarEquipo(datosLinea, equipos);
                } else if (datosLinea[0].equals("P")) {
                    cargarPartido(datosLinea, partidos, equipos);
                } else if (datosLinea[0].equals("C")) {
                    cargarCiudad(datosLinea, ciudades, colCiudades);
                } else if (datosLinea[0].equals("R")) {
                    cargarRuta(datosLinea, ciudades);
                }
            }
        }
    }

    public static void cargarCiudad(String[] datos, GrafoEtiquetado ciudades, Lista colCiudades) {
        String nombre = datos[1];
        boolean disponibilidad = datos[2].equals("TRUE");
        boolean sede = datos[3].equals("TRUE");

        Ciudad ciudad = new Ciudad(nombre, disponibilidad, sede);

        colCiudades.insertar(colCiudades.longitud() - 1, ciudad);

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

        Ciudad origen = (Ciudad) ciudades.obtenerIgual(new Ciudad(c1, false, false));
        Ciudad destino = (Ciudad) ciudades.obtenerIgual(new Ciudad(c2, false, false));

        ciudades.insertarArco(origen, destino, mins);
    }

    public static void menuCiudades(GrafoEtiquetado ciudades) {

        switch ("Elija una opcion para las ciudades") {
            case "1":

                break;

            case "2":

                break;

            case "3":

                break;

            default:
                break;
        }

    }

    public static void agregarCiudad(GrafoEtiquetado ciudades, Lista colCiudades) {
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
            colCiudades.insertar(colCiudades.longitud() - 1, ciudad);
        }
    }

    public static void eliminarCiudad(GrafoEtiquetado ciudades, Lista colCiudades) {
        Scanner scanner = new Scanner(System.in);
        int i = 0;

        System.out.println("Ingrese el nombre de la ciudad");
        String nombre = scanner.nextLine();
        boolean encontrada = false;

        while (!encontrada && i < colCiudades.longitud()) {
            Ciudad ciudad = (Ciudad) colCiudades.recuperar(i);

            if (ciudad.getNombre().equals(nombre)) {
                encontrada = true;
                colCiudades.eliminar(i);
                ciudades.eliminarVertice(ciudad);
                System.out.println("La ciudad fue eliminada con exito");
            } else {
                i++;
            }
        }

        if (!encontrada) {
            System.out.println("La ciudad no es parte del mapa");
        }
    }

    public static void modificarCiudad(GrafoEtiquetado ciudades, Lista colCiudades) {
        Scanner scanner = new Scanner(System.in);
        int i = 0;

        System.out.println("Ingrese el nombre de la ciudad");
        String nombre = scanner.nextLine();
        Ciudad ciudad = (Ciudad) colCiudades.recuperar(i);

        while (!ciudad.getNombre().equals(nombre) && i < colCiudades.longitud()) {
            i++;
            ciudad = (Ciudad) colCiudades.recuperar(i);
        }

        if (!(i < colCiudades.longitud())) {
            System.out.println("Ingrese la disponibilidad");
            boolean disponibilidad = ("TRUE".equals(scanner.nextLine().toUpperCase()));
            ciudad.setDisponibilidad(disponibilidad);

            System.out.println("Ingrese si es sede o no lo es");
            boolean sede = ("TRUE".equals(scanner.nextLine().toUpperCase()));
            ciudad.setSede(sede);

            //////////////////////////////////////////////////////////////////////////// MODIFICAR,
            //////////////////////////////////////////////////////////////////////////// IMPLEMENTAR
            //////////////////////////////////////////////////////////////////////////// SWITCH

            System.out.println("La ciudad fue modificada con exito");
        } else {
            System.out.println("La ciudad no es parte del mapa");
        }
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

    public static void eliminarEquipo(AVL equipos, Lista colEquipos) {
        Scanner scanner = new Scanner(System.in);
        int i = 0;

        System.out.println("Ingrese el nombre del equipo");
        String nombre = scanner.nextLine();
        boolean encontrado = false;

        while (!encontrado && i < colEquipos.longitud()) {
            Equipo equipo = (Equipo) colEquipos.recuperar(i);

            if (equipo.getNombre().equals(nombre)) {
                encontrado = true;
                colEquipos.eliminar(i);
                equipos.eliminar(equipo);
                System.out.println("El equipo fue eliminado con exito");
            } else {
                i++;
            }
        }

        if (!encontrado) {
            System.out.println("El equipo no es parte del fixture");
        }
    }

    public static void modificarEquipo(AVL equipos, Lista colEquipos) {
        Scanner scanner = new Scanner(System.in);

        int i = 0;
        boolean encontrado = false;
        System.out.println("Ingrese el nombre del equipo que desea modificar");
        String nombre = scanner.nextLine().toUpperCase();

        while (encontrado = false && i < colEquipos.longitud()) {
            Equipo equipo = (Equipo) colEquipos.recuperar(i);

            if (equipo.getNombre().equals(nombre)) {
                encontrado = true;
            } else {
                i++;
            }

            if (encontrado) {
                String opcion = "";

                while (opcion != "3") {

                    System.out.println("Ingrese 1 si desea modificar el DT, 2 si desea modificar el grupo, "
                            + "3 si quiere finalizar");
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

                System.out.println("La ciudad se modifico con exito");
            } else {
                System.out.println("El equipo no es parte del fixture");
            }
        }
    }

    public static void agregarPartido(MapeoAMuchos partidos, Lista colEquipos) {
        Scanner scanner = new Scanner(System.in);

        String muestraEquipos = "";

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
        
        if(eq1.compareTo(eq2)>0){
            String aux = eq1;
            eq1=eq2;
            eq2=aux;
        }
        
        if(eq1.compareTo(eq2)==0){
            System.out.println();
        }
        else{
            PartidoKey dominio = new PartidoKey(eq1, eq2);
            
            if(partidos.pertenece(dominio)){
                System.out.println(partidos.obtenerValor(dominio));
            }
        }
    }

    public static void menorEscala(GrafoEtiquetado ciudades) {
        /*
         * Obtener el camino que llegue de A a B pasando por la mínima cantidad de ciudades
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

    public static void caminosCompleto() {
        /*
         * Obtener todos los caminos posibles para llegar de A a B, mostrarlos y luego
         * filtrar y mostrar solo los que haya
         * posibilidad de conseguir alojamiento en la ciudad destino o en alguna ciudad
         * por la que tenga que pasar camino a ella
         */
        Scanner scanner = new Scanner(System.in);

        
    }

    public static void listaGoles() {

    }

    public static void mostrarSistema() {
        /*
         * Mostrar sistema: es una operación de debugging que permite ver todas las
         * estructuras utilizadas con su contenido
         * (grafo, AVL y Mapeo) para verificar, en cualquier momento de la ejecución del
         * sistema, que se encuentren cargadas correctamente.
         */
    }

}
