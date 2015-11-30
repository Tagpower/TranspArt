/**
 * Classe représentant un noeud correspondant à un <i>Transparent</i>.
 *
 * @author Clément Bauchet
 * @author Mélissa Obodje
 */
public class Transparent extends Noeud {

    public static int nbTrans = 0; //Nombre total de transparents

    public Transparent() {
        super();
    }

    /**
     * Création d'un nouveau transparent
     * @param file le chemin du fichier du transparent
     */
    public Transparent(String file) {
        super(file);
        nbTrans++;
        setName("T"+nbTrans);
    }

}