import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by clement on 25/11/15.
 */
public class ZeroOneVSmany extends Method { //TODO: tout

    //private HashMap<Transparent, Page> matching;
    //private HashMap<Page, ArrayList<Transparent>> opposite_matching;

    public ZeroOneVSmany() {
        listePages = new ArrayList<Page>();
        listeTrans = new ArrayList<Transparent>();
        graph = new HashMap<Noeud, ArrayList<Noeud>>();
        matching = new HashMap<Transparent, Page>(); //Couplage transparent -> page
        important_words = new HashSet<String>();
    }

    @Override
    public boolean isMaxMatching() {
        return matching.size() == listeTrans.size();
    }

    @Override
    public void findMaxMatching() {
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
                //opposite_matching.put((Page)neighbour, t);
                t.setMarked(true);
                neighbour.setMarked(true);
            } else { //Si tous les voisins sont marqués, prendre le premier.
                if (!graph.get(t).isEmpty()) {
                    neighbour = graph.get(t).get(0);
                    matching.put(t, (Page)neighbour);
                    //opposite_matching.put((Page)neighbour, t);
                    t.setMarked(true);
                    neighbour.setMarked(true);
                }
            }
        } //Premier couplage trivial fait (et en fait on a déjà fini :P )
    }

    //Affichage du couplage en console
    public void printMatching() {
        for (Transparent t : matching.keySet()) {
            System.out.println(t.getName() + " <-> " + matching.get(t).getName());
        }
    }
}
