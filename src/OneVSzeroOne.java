import java.util.*;

/**
 * Created by clement on 15/11/15.
 */
public class OneVSzeroOne extends Method {

    private HashMap<Transparent, Page> matching;
    private HashMap<Page, Transparent> opposite_matching;

    public OneVSzeroOne() {
        listePages = new ArrayList<Page>();
        listeTrans = new ArrayList<Transparent>();
        graph = new HashMap<Noeud, ArrayList<Noeud>>();
        matching = new HashMap<Transparent, Page>();
        opposite_matching = new HashMap<Page, Transparent>();
        important_words = new HashSet<String>();
    }

    boolean isMaxMatching() {
        return matching.size() == listeTrans.size();
    }

    @Override
    public void build_graph() { //Construire le graphe b
        Set<String> intersect = new HashSet<String>();

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
            } else {
                break;
            }
        } //Premier couplage trivial fait

        //Recherche d'un meilleur couplage
        while (!isMaxMatching()) { //Tant que le couplage n'est pas maximum, on cherche à l'améliorer.
            for (Noeud n : listeTrans) { //Pour tout sommet n non marqué
                if (!n.isMarked()) {
                    ArrayList<Noeud> chemin_augmentant = ameliorer(n);
                    improveMatching(chemin_augmentant); //Améliorer le couplage
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
                } else {
                    Noeud t = opposite_matching.get(voisin); //On récupère le partenaire de voisin dans le couplage (un transparent depuis une page)
                    chaine.add(voisin);
                    chaine.add(t);
                    return ameliorer(chaine);
                }
            }
        }
        return chaine_alternee;
    }

    public void improveMatching(ArrayList<Noeud> chemin) {
        boolean ajouter_arc = true;
        for (int i=0; i<chemin.size()-1; i++) {
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

    public void printMatching() {
        for (Transparent t : matching.keySet()) {
            System.out.println(t.getName() + " <-> " + matching.get(t).getName());
        }
        for (Page p : opposite_matching.keySet()) {
            System.out.println(p.getName() + " <-> " + opposite_matching.get(p).getName());
        }
        assert (matching.size() == opposite_matching.size());
    }


}
