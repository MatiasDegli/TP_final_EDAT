package TDAs;

public class PartidoKey {
    
    String eq1, eq2;
    
    public PartidoKey(String e1, String e2){
        eq1=e1;
        eq2=e2;
    }

    public String toString(){
        return "Equipo 1: "+eq1+" - Equipo 2: "+eq2;
    }
}
