import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by clement on 25/11/15.
 */
public class ZeroOneVSmany extends Method { //TODO: tout

    private HashMap<Transparent, Page> matching;
    //private HashMap<Page, ArrayList<Transparent>> opposite_matching;

    @Override
    public void build_graph() { //Construire le graphe biparti entre les Transparents et les Pages. Les arêtes sont les associations possibles entre eux.
        Set<String> intersect = new HashSet<String>(); //Mots en commun d'un transparent, d'une page et du dictionnaire.

        for (Transparent t : listeTrans) {
            ArrayList<Noeud> successors = new ArrayList<Noeud>();
            for (Page p : listePages) { //Obtenir l'intersection des mots du transparents, ceux de la page et les mots du dictionnaire.
                intersect.clear();
                intersect.addAll(t.getWordList());
                intersect.retainAll(p.getWordList());
                if (!intersect.isEmpty()) {
                    intersect.retainAll(important_words);
                }
                System.out.println("Le transparent " + (listeTrans.indexOf(t)+1) + " a " + intersect.size() + " mots en commun avec la page " + (listePages.indexOf(p)+1));
                if (intersect.size() >= 1 ) { //TODO: Trouver une meilleure condition
                    successors.add(p);
                }
            }
            graph.put(t, successors);
            for (Noeud s : successors) { //Ajout de t comme voisin/successeur de la page
                graph.putIfAbsent(s, new ArrayList<Noeud>());
                graph.get(s).add(t);
            }
        }
    }

    @Override
    public void save_graph(String S) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter("./dot/" + S)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert pw != null;

        //Enregistrer le graphe au format .dot
        pw.println("graph " + S + "{");
        for (Transparent t : listeTrans) {
            pw.println(t.getName() + "[label=" + t.getName() + "]" /* + (t.isMarked() ? "[color=red]" : "")*/ );
            for (Noeud p : graph.get(t)) {
                if (matching.get(t) == p) {
                    pw.println(t.getName() + " -- " + p.getName() + "[color=red,penwidth=3.0]");
                } else {
                    pw.println(t.getName() + " -- " + p.getName());
                }
                pw.println(p.getName() + "[label=" + p.getName() + "]" /* + (t.isMarked() ? "[color=red]" : "") */ );
            }
        }
        pw.println("}");
        pw.close();

        //Créer un fichier .svg du graphe et son couplage.
        try {
            Process p = Runtime.getRuntime().exec("dot -O -Tsvg ./dot/" + S);
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
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
                neighbour = graph.get(t).get(0);
                matching.put(t, (Page)neighbour);
                //opposite_matching.put((Page)neighbour, t);
                t.setMarked(true);
                neighbour.setMarked(true);
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
