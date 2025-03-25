package Estructuras;

import java.util.LinkedList;
import java.util.Queue;

import TDAs.Ciudad;

public class GrafoEtiquetado {

    private NodoVert inicio;

    public GrafoEtiquetado() {
        inicio = null;
    }

    public boolean insertarVertice(Object nuevoVertice) {

        boolean exito = false;
        NodoVert aux = ubicarVertice(nuevoVertice);

        if (aux == null) {
            if (inicio == null) {
                inicio = new NodoVert(nuevoVertice, null);
            } else {
                NodoVert siguiente = inicio.getSigVertice();
                if (siguiente == null) {
                    inicio.setSigVertice(new NodoVert(nuevoVertice, null));
                } else {
                    while (siguiente.getSigVertice() != null) {
                        siguiente = siguiente.getSigVertice();
                    }
                    siguiente.setSigVertice(new NodoVert(nuevoVertice, null));
                }
            }
            exito = true;
        }

        return exito;
    }

    private NodoVert ubicarVertice(Object buscado) {
        NodoVert aux = inicio;

        while (aux != null && !aux.getElem().equals(buscado)) {
            aux = aux.getSigVertice();
        }

        return aux;
    }

    public boolean eliminarVertice(Object vertice) {
        NodoVert buscado = ubicarVertice(vertice);

        if (buscado != null) {
            eliminarAux(inicio, inicio, buscado);
        }

        return buscado != null;
    }

    private void eliminarAux(NodoVert recorre, NodoVert anterior, NodoVert eliminar) {

        if (recorre != null) {

            if (recorre.equals(eliminar)) {

                if (recorre.equals(inicio)) {

                    eliminarAdyacente(eliminar);
                    inicio = inicio.getSigVertice();
                } else {

                    eliminarAdyacente(eliminar);
                    anterior.setSigVertice(recorre.getSigVertice());
                }
            }

            eliminarAux(recorre.getSigVertice(), recorre, eliminar);
        }

    }

    private void eliminarAdyacente(NodoVert eliminar) {

        NodoAdy adyacente = eliminar.getPrimerAdy();

        if (adyacente != null) {

            while (adyacente != null) {
                eliminarArcoAux(eliminar, adyacente.getVertice());
                eliminarArcoAux(adyacente.getVertice(), eliminar);
                adyacente = adyacente.getSigAdyacente();
            }

        }
    }

    public boolean existeVertice(Object vertice) {
        boolean exito = false;

        NodoVert recorre = inicio;

        while (recorre != null && !recorre.getElem().equals(vertice)) {
            recorre = recorre.getSigVertice();
        }

        if (recorre != null) {
            exito = true;
        }

        return exito;
    }

    public boolean insertarArco(Object elemOrigen, Object elemDestino, Object etiqueta) {
        boolean exito = false;

        NodoVert recorre = inicio, origen = null, destino = null;

        while (recorre != null && (origen == null || destino == null)) {

            if (recorre.getElem().equals(elemOrigen)) {
                origen = recorre;
            }

            if (recorre.getElem().equals(elemDestino)) {
                destino = recorre;
            }

            recorre = recorre.getSigVertice();
        }

        if (!existeArco(elemOrigen, elemDestino) && origen != null && destino != null) {
            arcoAux(origen, destino, etiqueta);
            arcoAux(destino, origen, etiqueta);
            exito = true;
        }

        return exito;
    }

    private void arcoAux(NodoVert origen, NodoVert destino, Object etiqueta) {

        if (origen.getPrimerAdy() == null) {
            origen.setPrimerAdy(new NodoAdy(destino, null, etiqueta));
        } else {
            NodoAdy adyacente = origen.getPrimerAdy();
            NodoAdy anterior = adyacente;

            while (adyacente != null) {
                anterior = adyacente;
                adyacente = adyacente.getSigAdyacente();
            }

            anterior.setSigAdyacente(new NodoAdy(destino, null, etiqueta));
        }
    }

    public boolean eliminarArco(Object elemOrigen, Object elemDestino) {
        boolean exito = false;

        NodoVert recorre = inicio, origen = null, destino = null;

        while (recorre != null && (origen == null || destino == null)) {

            if (recorre.getElem().equals(elemOrigen)) {
                origen = recorre;
            }

            if (recorre.getElem().equals(elemDestino)) {
                destino = recorre;
            }

            recorre = recorre.getSigVertice();
        }

        eliminarArcoAux(origen, destino);
        eliminarArcoAux(destino, origen);

        return exito;
    }

    private void eliminarArcoAux(NodoVert origen, NodoVert destino) {

        if (origen != null) {
            NodoAdy adyacente = origen.getPrimerAdy();
            NodoAdy anterior = adyacente;

            while (adyacente != null && adyacente.getVertice() != destino) {
                anterior = adyacente;
                adyacente = adyacente.getSigAdyacente();
            }

            if (adyacente != null && adyacente.equals(origen.getPrimerAdy())) {
                origen.setPrimerAdy(adyacente.getSigAdyacente());
            } else if (anterior != null) {
                if (adyacente != null) {
                    anterior.setSigAdyacente(adyacente.getSigAdyacente());
                } else {
                    anterior.setSigAdyacente(null);
                }
            }
        }
    }

    public boolean existeArco(Object elemOrigen, Object elemDestino) {
        boolean exito = false;

        NodoVert origen = ubicarVertice(elemOrigen);

        if (origen != null) {
            NodoAdy adyacente = origen.getPrimerAdy();

            while (adyacente != null && !adyacente.getVertice().getElem().equals(elemDestino)) {
                adyacente = adyacente.getSigAdyacente();
            }

            if (adyacente != null) {
                // recorrio la lista de adyacencia hasta que encontró el vertice destino
                exito = true;
            }
        }

        return exito;
    }

    public boolean existeCamino(Object elemOrigen, Object elemDestino) {
        boolean exito = false;

        NodoVert origen = ubicarVertice(elemOrigen);

        if (origen != null) {

            NodoAdy adyacente = origen.getPrimerAdy();

            Lista recorridos = new Lista();

            recorridos.insertar(recorridos.longitud() + 1, elemOrigen);

            exito = caminoAux(false, recorridos, elemDestino, adyacente);

        }

        return exito;
    }

    private boolean caminoAux(boolean exito, Lista recorridos, Object elemDestino, NodoAdy adyacente) {

        if (adyacente != null && exito == false) {

            NodoVert proximo = adyacente.getVertice();

            if (proximo.getElem().equals(elemDestino)) {
                exito = true;
            } else if (recorridos.localizar(proximo.getElem()) == -1) {
                recorridos.insertar(recorridos.longitud() + 1, adyacente.getVertice().getElem());
                exito = caminoAux(exito, recorridos, elemDestino, proximo.getPrimerAdy());
            }

            if (exito == false) {
                exito = caminoAux(exito, recorridos, elemDestino, adyacente.getSigAdyacente());
            }
        }

        return exito;
    }

    private void llenar(Lista aLlenar, Lista llena) {
        for (int i = 1; i <= llena.longitud(); i++) {
            aLlenar.insertar(i, llena.recuperar(i));
        }
    }

    public Lista caminoMasLargo(Object elemOrigen, Object elemDestino) {
        Lista actual = new Lista(), largo = new Lista();
        NodoVert origen = ubicarVertice(elemOrigen);

        largoAux(actual, largo, elemDestino, origen);

        return largo;
    }

    private void largoAux(Lista actual, Lista largo, Object elemDestino, NodoVert vertice) {

        if (vertice != null) {

            actual.insertar(actual.longitud() + 1, vertice.getElem());

            if (vertice.getElem().equals(elemDestino)) {

                if (actual.esVacia() || actual.longitud() > largo.longitud()) {
                    largo.vaciar();
                    llenar(largo, actual);
                }
            } else {
                NodoAdy adyacente = vertice.getPrimerAdy();

                while (adyacente != null) {

                    vertice = adyacente.getVertice();

                    if (actual.localizar(vertice.getElem()) == -1) {
                        largoAux(actual, largo, elemDestino, vertice);
                    }

                    adyacente = adyacente.getSigAdyacente();
                }

            }
        }
    }

    public Lista listarEnProfundidad() {
        Lista visitados = new Lista();

        NodoVert aux = inicio;

        while (aux != null) {

            if (visitados.localizar(aux.getElem()) == -1) {
                profundidadDesde(aux, visitados);
            }
            aux = aux.getSigVertice();
        }

        return visitados;
    }

    private void profundidadDesde(NodoVert recorre, Lista visitados) {

        visitados.insertar(visitados.longitud() + 1, recorre.getElem());

        NodoAdy adyacente = recorre.getPrimerAdy();

        while (adyacente != null) {

            if (visitados.localizar(adyacente.getVertice().getElem()) == -1) {
                profundidadDesde(adyacente.getVertice(), visitados);
            }
            adyacente = adyacente.getSigAdyacente();
        }
    }

    public Lista listarEnAnchura() {
        Lista visitados = new Lista();

        NodoVert aux = inicio;

        while (aux != null) {
            if (visitados.localizar(aux.getElem()) == -1) {
                anchuraDesde(aux, visitados);
            }
            aux = aux.getSigVertice();
        }

        return visitados;
    }

    private void anchuraDesde(NodoVert verticeInicial, Lista visitados) {
        Cola q = new Cola();
        visitados.insertar(visitados.longitud() + 1, verticeInicial.getElem());
        q.poner(verticeInicial);

        while (!q.esVacia()) {

            NodoVert u = (NodoVert) q.obtenerFrente();

            q.sacar();

            NodoAdy v = u.getPrimerAdy();

            while (v != null) {

                if (visitados.localizar(v.getVertice().getElem()) == -1) {
                    visitados.insertar(visitados.longitud() + 1, v.getVertice().getElem());
                    q.poner(v.getVertice());
                }
                v = v.getSigAdyacente();
            }
        }
    }

    public Lista caminoMasCorto(Object elemOrigen, Object elemDestino) {
        // Creamos una cola para almacenar caminos
        Cola cola = new Cola();
        Lista caminoCorto = new Lista();
        Lista caminoInicial = new Lista();

        caminoInicial.insertar(1, elemOrigen);
        // Ponemos el camino inicial
        cola.poner(caminoInicial); 

        while (!cola.esVacia()) {
            // Obtenemos el camino en el frente de la cola y lo sacamos
            Lista caminoActual = (Lista) cola.obtenerFrente();
            cola.sacar();

            // Obtenemos el último elemento del camino
            Object ultimoElem = caminoActual.recuperar(caminoActual.longitud());

            // Si el último elemento es el destino, retornamos este camino
            if (ultimoElem.equals(elemDestino)) {
                caminoCorto = caminoActual;
                cola.vaciar();

            } else {
                // Buscamos en el grafo el nodo correspondiente al último elemento
                NodoVert nodo = ubicarVertice(ultimoElem);

                if (nodo != null) {
                    NodoAdy adyacente = nodo.getPrimerAdy();
                    
                    while (adyacente != null) {
                        Object elemActual = adyacente.getVertice().getElem();
                        
                        if (caminoActual.localizar(elemActual) == -1) {
                            Lista nuevoCamino = caminoActual.clone();
                            nuevoCamino.insertar(nuevoCamino.longitud() + 1, elemActual);
                            System.out.println("El camino actual es: "+nuevoCamino);
                            cola.poner(nuevoCamino);
                        }
                        adyacente = adyacente.getSigAdyacente();
                    }
                }
            }
        }
        
        return caminoCorto;
    }

    public boolean esVacio() {
        return inicio == null;
    }

    public GrafoEtiquetado clone() {
        GrafoEtiquetado clon = new GrafoEtiquetado();

        clon.inicio = new NodoVert(inicio.getElem(), null);

        cloneAux(clon.inicio, inicio);

        return clon;
    }

    private void cloneAux(NodoVert clon, NodoVert recorre) {

        if (recorre != null) {

            if (recorre.getPrimerAdy() != null) {
                NodoAdy adyacente = recorre.getPrimerAdy();
                NodoAdy clonAdy = clon.getPrimerAdy();

                while (adyacente != null) {
                    NodoVert nuevo = new NodoVert(adyacente.getVertice().getElem(), null);

                    if (clon.getPrimerAdy() == null) {
                        clon.setPrimerAdy(new NodoAdy(nuevo, null, adyacente.getEtiqueta()));
                        clonAdy = clon.getPrimerAdy();
                    } else {
                        clonAdy.setSigAdyacente(new NodoAdy(nuevo, null, adyacente.getEtiqueta()));
                        clonAdy = clonAdy.getSigAdyacente();
                    }

                    adyacente = adyacente.getSigAdyacente();

                }
            }

            recorre = recorre.getSigVertice();
            if (recorre != null) {
                clon.setSigVertice(new NodoVert(recorre.getElem(), null));
                clon = clon.getSigVertice();
            }
            cloneAux(clon, recorre);
        }
    }

    public String toString() {
        String msj = stringAux("", inicio);
        return msj;
    }

    private String stringAux(String msj, NodoVert recorre) {

        if (recorre != null) {

            NodoAdy adyacente = recorre.getPrimerAdy();

            if (adyacente == null) {
                msj += recorre.getElem().toString();
            }

            while (adyacente != null) {

                if (adyacente.getSigAdyacente() != null) {
                    msj += recorre.getElem().toString() + " -" + adyacente.getEtiqueta() + "->";
                    msj += " " + adyacente.getVertice().getElem().toString() + ", ";
                } else {
                    msj += recorre.getElem().toString() + " -" + adyacente.getEtiqueta() + "->";
                    msj += " " + adyacente.getVertice().getElem().toString();
                }
                adyacente = adyacente.getSigAdyacente();
            }
            msj += "\n";
            msj = stringAux(msj, recorre.getSigVertice());
        }

        return msj;
    }

    // ----------------------- METODOS PARA LAS CIUDADES ------------------------

    private NodoVert ubicarCiudad(Ciudad ciudad) {
        NodoVert aux = inicio;

        if (aux != null) {

            while (aux != null && ((Ciudad) aux.getElem()).compareTo(ciudad) != 0) {
                aux = aux.getSigVertice();
            }
        }

        return aux;
    }

    public Ciudad obtenerCiudad(Ciudad ciudad) {
        NodoVert aux = inicio;
        Ciudad ciudadEncontrada = null;

        while (aux != null && ((Ciudad) aux.getElem()).compareTo(ciudad) != 0) {
            aux = aux.getSigVertice();
        }

        if (aux != null) {
            ciudadEncontrada = (Ciudad) aux.getElem();
        }

        return ciudadEncontrada;
    }

    public boolean existeCiudad(Ciudad ciudad) {
        boolean exito = false;

        NodoVert recorre = inicio;
        String nombre = ((Ciudad) recorre.getElem()).getNombre();

        while (recorre != null && !nombre.equals(ciudad.getNombre())) {
            nombre = ((Ciudad) recorre.getElem()).getNombre();
            recorre = recorre.getSigVertice();
        }

        if (recorre != null) {
            exito = true;
        }

        return exito;
    }

    public Lista caminoMenorTiempo(Ciudad ciudadOrigen, Ciudad ciudadDestino) {
        Lista actual = new Lista(), rapida = new Lista();
        NodoVert origen = ubicarCiudad(ciudadOrigen);
        NodoVert destino = ubicarCiudad(ciudadDestino);
        int[] tiempoMenor = new int[] { Integer.MAX_VALUE };

        rapida = menorTiempoAux(origen, destino, actual, rapida, 0, tiempoMenor);

        return rapida;
    }

    private Lista menorTiempoAux(NodoVert recorre, NodoVert destino, Lista actual, Lista rapida, int tiempoActual,
            int[] tiempoMenor) {

        actual.insertar(actual.longitud() + 1, recorre.getElem());

        System.out.println("Lista actual: " + actual.toString());
        System.out.println("Lista rapida: " + rapida.toString());
        System.out.println("Tiempo actual: " + tiempoActual + " y tiempo menor: " + tiempoMenor[0]);

        if (recorre.getElem().equals(destino.getElem())) {
            if (tiempoActual < tiempoMenor[0]) {
                System.out.println(" ----------------- Modificacion -----------------");
                rapida = actual.clone();
                tiempoMenor[0] = tiempoActual;
            }
        } else {
            NodoAdy adyacente = recorre.getPrimerAdy();

            while (adyacente != null) {
                NodoVert siguienteVertice = adyacente.getVertice();

                if (actual.localizar(siguienteVertice.getElem()) == -1
                        && tiempoActual + (int) adyacente.getEtiqueta() < tiempoMenor[0] && siguienteVertice != null) {
                    // La ciudad no esta en la lista de ciudades visitadas y la el camino es mas
                    // rapido que el almacenado

                    rapida = menorTiempoAux(siguienteVertice, destino, actual, rapida,
                            tiempoActual + (int) adyacente.getEtiqueta(), tiempoMenor);
                }
                adyacente = adyacente.getSigAdyacente();
            }
        }

        actual.eliminar(actual.longitud());

        return rapida;
    }

    public Lista caminoRapidoAlt(Ciudad ciudadOrigen, Ciudad ciudadDestino, Ciudad ciudadEvitar) {
        Lista actual = new Lista(), rapida = new Lista();
        NodoVert origen = ubicarCiudad(ciudadOrigen);
        NodoVert destino = ubicarCiudad(ciudadDestino);
        NodoVert evitar = ubicarCiudad(ciudadEvitar);

        int[] tiempoMenor = new int[] { Integer.MAX_VALUE };

        rapida = menorRapidoAux(origen, destino, evitar, actual, rapida, 0, tiempoMenor);

        return rapida;
    }

    private Lista menorRapidoAux(NodoVert recorre, NodoVert destino, NodoVert evitar, Lista actual, Lista rapida,
            int tiempoActual,
            int[] tiempoMenor) {

        actual.insertar(actual.longitud() + 1, recorre.getElem());

        System.out.println("Lista actual: " + actual.toString());
        System.out.println("Lista rapida: " + rapida.toString());
        System.out.println("Tiempo actual: " + tiempoActual + " y tiempo menor: " + tiempoMenor[0]);

        if (recorre.getElem().equals(destino.getElem())) {
            if (tiempoActual < tiempoMenor[0]) {
                System.out.println(" ----------------- Modificacion -----------------");
                rapida = actual.clone();
                tiempoMenor[0] = tiempoActual;
            }
        } else {
            NodoAdy adyacente = recorre.getPrimerAdy();

            while (adyacente != null) {
                NodoVert siguienteVertice = adyacente.getVertice();

                if (actual.localizar(siguienteVertice.getElem()) == -1
                        && tiempoActual + (int) adyacente.getEtiqueta() < tiempoMenor[0] && siguienteVertice != null
                        && !siguienteVertice.equals(evitar)) {
                    // La ciudad no esta en la lista de ciudades visitadas, el camino es mas
                    // rapido que el almacenado y no pasa por la ciudad a evitar

                    rapida = menorTiempoAux(siguienteVertice, destino, actual, rapida,
                            tiempoActual + (int) adyacente.getEtiqueta(), tiempoMenor);
                }
                adyacente = adyacente.getSigAdyacente();
            }
        }

        actual.eliminar(actual.longitud());

        return rapida;
    }

    public Lista caminosTotales(Ciudad ciudadOrigen, Ciudad ciudadDestino) {
        Lista caminos = new Lista(), actual = new Lista();
        NodoVert origen = ubicarCiudad(ciudadOrigen);
        NodoVert destino = ubicarCiudad(ciudadDestino);

        caminos = totalesAux(origen, destino, caminos, actual);

        return caminos;
    }

    private Lista totalesAux(NodoVert recorre, NodoVert destino, Lista caminos, Lista actual) {

        actual.insertar(actual.longitud() + 1, recorre.getElem());

        if (recorre.getElem().equals(destino.getElem())) {
            caminos.insertar(caminos.longitud() + 1, actual.clone());
        } else {
            NodoAdy adyacente = recorre.getPrimerAdy();
            while (adyacente != null) {

                NodoVert siguienteVertice = adyacente.getVertice();

                if (actual.localizar(siguienteVertice.getElem()) < 0 && siguienteVertice != null) {
                    // La ciudad no está en la lista de visitados
                    caminos = totalesAux(siguienteVertice, destino, caminos, actual);
                }
                adyacente = adyacente.getSigAdyacente();
            }
        }

        actual.eliminar(actual.longitud());

        return caminos;
    }

}
