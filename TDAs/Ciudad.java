package TDAs;

public class Ciudad implements Comparable {

    private String nombre;
    private boolean disponibilidad;
    private boolean esSede;

    public Ciudad(String nom, boolean dis, boolean sede) {
        nombre = nom;
        disponibilidad = dis;
        esSede = sede;
    }

    public String getNombre() {
        return nombre;
    }

    public void setDisponibilidad(boolean dis) {
        disponibilidad = dis;
    }

    public void setSede(boolean sede) {
        esSede = sede;
    }

    @Override
    public int compareTo(Object otra) {
        return nombre.compareTo(((Ciudad) otra).getNombre());
    }

}
