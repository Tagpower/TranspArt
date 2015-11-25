import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by clement on 15/11/15.
 */
public class OneVSzeroOne extends Method {

    private HashMap<Transparent, Page> matching; //Couplage trouvé
    private HashMap<Page, Transparent> opposite_matching; //Couplage opposé, utilisé pour retrouver un transparent à partir d'une page dans le couplage initial.
    private ArrayList<HashMap<Transparent, Page>> previous_matchings; //Liste des couplages trouvés. Sert à détecter si l'algorithme ne se termine pas.

    public OneVSzeroOne() {
        listePages = new ArrayList<Page>();
        listeTrans = new ArrayList<Transparent>();
        important_words = new HashSet<String>();
        graph = new HashMap<Noeud, ArrayList<Noeud>>();
        matching = new HashMap<Transparent, Page>(); //Couplage transparent -> page
        opposite_matching = new HashMap<Page, Transparent>(); //Couplage page -> transparent
        previous_matchings = new ArrayList<>();
    }

    public boolean isMaxMatching() {
        return (matching.size() == listeTrans.size());
    }

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

    //Algo recherche de couplage max
    public void findMaxMatching() {

        if (listeTrans.size() > listePages.size()) {
            //Exception : impossible d'associer tous les transparents.
            //TODO: Vérifier aussi si tous les transparents ont des voisins.
        }

        //Création d'un premier couplage trivial.
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
                opposite_matching.put((Page)neighbour, t);
                t.setMarked(true);
                neighbour.setMarked(true);
            }
        } //Premier couplage trivial fait
        previous_matchings.add(matching);

        //Recherche d'un meilleur couplage
        while (!isMaxMatching()) { //Tant que le couplage n'est pas maximum, on cherche à l'améliorer.
            for (Noeud n : listeTrans) { //Pour tout transparent n non marqué
                if (!n.isMarked()) {
                    ArrayList<Noeud> chemin_augmentant = ameliorer(n);
                    improveMatching(chemin_augmentant); //Améliorer le couplage
                    if (previous_matchings.contains(matching) && !isMaxMatching()) {
                        System.out.println("Détection d'une boucle ! Arrêt de l'algorithme.");
                        return;
                    } else {
                        previous_matchings.add(matching);
                    }
                }
            }
        }

    }

    //Recherche de chemin augmentant (à partir d'un seul sommet)
    public ArrayList<Noeud> ameliorer(Noeud n) {
        return ameliorer(new ArrayList<Noeud>(Arrays.asList(n)));
    }

    //Recherche de chemin augmentant
    public ArrayList<Noeud> ameliorer(ArrayList<Noeud> chaine) { //Trouver un chemin augmentant à partir d'une chaine donnée
        ArrayList<Noeud> chaine_alternee = new ArrayList<Noeud>();
        Noeud last = chaine.get(chaine.size() - 1); //On récupère le dernier sommet de la chaine
        for (Noeud voisin : graph.get(last)) { //Pour tous les voisins de ce sommet
            if (!chaine.contains(voisin)) {
                if (!voisin.isMarked()) { //Si un voisin non marqué est trouvé, marquer les deux sommets : cela fera une nouvelle paire dans le couplage.
                    chaine.add(voisin);
                    chaine_alternee = chaine; //chaine alternée = chaine U voisin
                    last.setMarked(true);
                    voisin.setMarked(true);
                    break;
                } else { //Si un voisin non marqué est trouvé
                    Noeud t = opposite_matching.get(voisin); //On récupère le partenaire de voisin dans le couplage (un transparent depuis une page)
                    chaine.add(voisin);
                    chaine.add(t);
                    return ameliorer(chaine);
                }
            }
        }
        return chaine_alternee;
    }

    //Appliquer le chemin augmentant pour modifier le couplage
    public void improveMatching(ArrayList<Noeud> chemin) {
        boolean ajouter_arc = true;
        for (int i=0; i<chemin.size()-1; i++) { //Pour chaque arête du chemin
            if (ajouter_arc) {
                matching.put((Transparent)chemin.get(i), (Page)chemin.get(i+1));
                opposite_matching.put((Page)chemin.get(i+1), (Transparent)chemin.get(i));
            } else {
                matching.remove(chemin.get(i));
                opposite_matching.remove(chemin.get(i + 1));
            }
            ajouter_arc = !ajouter_arc;
        }
    }

    //Affichage du couplage en console
    public void printMatching() {
        for (Transparent t : matching.keySet()) {
            System.out.println(t.getName() + " <-> " + matching.get(t).getName());
        }
//        for (Page p : opposite_matching.keySet()) {
//            System.out.println(p.getName() + " <-> " + opposite_matching.get(p).getName());
//        }
        assert (matching.size() == opposite_matching.size());
    }

    public void save_graph(String S) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter("./dot/" + S)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert pw != null;

        // print the graph in dot file format
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

        // create the svg file from the dot file
        try {
            Process p = Runtime.getRuntime().exec("dot -O -Tsvg ./dot/" + S);
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
