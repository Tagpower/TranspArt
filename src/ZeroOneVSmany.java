import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Classe résolvant le problème de couplage maximum dans un graphe biparti - Variante ZeroOne-VS-Many :
 * Variante du problème qui associe <b>un maximum de transparents à exactement une page chacun</b>.
 * Une page peut être associée à plusieurs transparents.
 *
 * @author Clément Bauchet
 * @author Mélissa Obodje
 */
public class ZeroOneVSmany extends Method {

    public ZeroOneVSmany() {
        listePages = new ArrayList<Page>();
        listeTrans = new ArrayList<Transparent>();
        graph = new HashMap<Noeud, ArrayList<Noeud>>();
        matching = new HashMap<Transparent, Page>(); //Couplage transparent -> page
        important_words = new HashSet<String>();
    }

    /**
     * Retourne un booléen indiquant si le couplage est optimal, c'est-à-dire si tous les transparents sont couplés.
     * @return vrai si le couplage est optimal
     */
    public boolean isMaxMatching() {
        return matching.size() == listeTrans.size();
    }

    /**
     * Fonction permettant de trouver un couplage maximal dans le graphe.
     * Dans cette variante, il est possible de trouver un couplage maximal sans avoir à chercher des chemins augmentants.
     * En traitant tour à tour chaque transparent, on peut tous les lier à une page, si le graphe le permet (présence de voisin(s) pour chaque transparent)
     * On cherche à coupler de préférence des transparents avec des pages non encore couplées, si c'est possible.
     */
    @Override
    public void findMaxMatching() {
        //Création d'un premier couplage trivial, en couplant chaque transparent avec le premier voisin trouvé (si possible)
        for (Transparent t : listeTrans) {
            Noeud neighbour = null; //On cherche le premier voisin de t non marqué
            for (Noeud p : graph.get(t)) {
                if (!p.isMarked()) {
                    neighbour = p;
                    break;
                }
            }
            if (neighbour != null) { //S'il y a un voisin non marqué, on ajoute t et ce voisin au couplage.
                matching.put(t, (Page)neighbour);
                t.setMarked(true);
                neighbour.setMarked(true);
            } else { //Si tous les voisins sont marqués, prendre le premier.
                if (!graph.get(t).isEmpty()) {
                    neighbour = graph.get(t).get(0);
                    matching.put(t, (Page)neighbour);
                    t.setMarked(true);
                    neighbour.setMarked(true);
                }
            }
        }
    }

    /**
     * Affichage du couplage trouvé en console
     */
    public void printMatching() {
        for (Transparent t : matching.keySet()) {
            System.out.println(t.getName() + " <-> " + matching.get(t).getName());
        }
    }
}
