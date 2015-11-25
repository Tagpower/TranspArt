public class Transparent extends Noeud {

    public static int nbTrans = 0;

    public Transparent() {
        super();
    }

    public Transparent(String file) {
        super(file);
        nbTrans++;
        setName("T"+nbTrans);
    }

}