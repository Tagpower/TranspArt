/**
 * Classe représentant un noeud correspondant à une <i>Page</i>.
 * @author Clément Bauchet
 * @author Mélissa Obodje
 */
public class Page extends Noeud {

    public static int nbPages = 0; //Nombre total de pages

    public Page() {
        super();
    }

    /**
     * Création d'une nouvelle page
     * @param file le chemin du fichier de la page
     */
    public Page(String file) {
        super(file);
        nbPages++;
        setName("A"+nbPages);
    }

}
