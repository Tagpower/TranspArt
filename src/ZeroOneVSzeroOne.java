import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Classe résolvant le problème de couplage maximum dans un graphe biparti - Variante ZeroOne-VS-ZeroOne :
 * Variante du problème qui associe <b>un maximum de transparents à exactement une page chacun</b>.
 * Une page ne peut pas être associée à plusieurs transparents.
 * Contrairement à la variante One-VS-ZeroOne, le couplage trouvé peut ne pas coupler tous les transparents.
 *
 * @author Clément Bauchet
 * @author Mélissa Obodje
 */
public class ZeroOneVSzeroOne extends Method {

    private HashMap<Page, Transparent> opposite_matching; //Couplage opposé, utilisé pour retrouver un transparent à partir d'une page dans le couplage initial.
    private ArrayList<HashMap<Transparent, Page>> previous_matchings; //Liste des couplages trouvés. Sert à détecter si l'algorithme ne se termine pas.
    private boolean chemin_de_croissance = true;

    public ZeroOneVSzeroOne(boolean v) {
        listePages = new ArrayList<Page>();
        listeTrans = new ArrayList<Transparent>();
        important_words = new HashSet<String>();
        graph = new HashMap<Noeud, ArrayList<Noeud>>();
        matching = new HashMap<Transparent, Page>(); //Couplage transparent -> page
        opposite_matching = new HashMap<Page, Transparent>(); //Couplage page -> transparent

        verbose = v;
    }

    /**
     * Retourne un booléen indiquant si le couplage est optimal, c'est-à-dire si tous les transparents ou toutes les pages sont couplés.
     * @return vrai si le couplage est optimal
     */
    @Override
    public boolean isMaxMatching() {
        return (matching.size() == listeTrans.size() || matching.size() == listePages.size() || !chemin_de_croissance);
    }

    /**
     * Fonction permettant de trouver un couplage maximal dans le graphe, par remplissage d'un premier couplage trivial, puis par améliorations successives en trouvant un chemin augmentant.
     */
    public void findMaxMatching() {
        //Création d'un premier couplage trivial.
        for (Transparent t : listeTrans) {
            Noeud neighbour = null; //On cherche le premier voisin de t non marqué
            if (!graph.get(t).isEmpty()) {
                for (Noeud p : graph.get(t)) {
                    if (!p.isMarked()) {
                        neighbour = p;
                        break;
                    }
                }
            }
            if (neighbour != null) { //S'il y a un voisin non marqué, on ajoute t et ce voisin au couplage.
                matching.put(t, (Page)neighbour);
                opposite_matching.put((Page)neighbour, t);
                t.setMarked(true);
                neighbour.setMarked(true);
            }
        } //Premier couplage trivial fait

        //Recherche d'un meilleur couplage
        while (!isMaxMatching()) { //Tant que le couplage n'est pas maximum, on cherche à l'améliorer.
            for (Transparent t : listeTrans) { //Pour tout transparent n non marqué
                if (!t.isMarked()) {
                    ArrayList<Noeud> chemin_augmentant = ameliorer(t);
                    chemin_de_croissance = improveMatching(chemin_augmentant); //Améliorer le couplage
                }
            }
        }
        System.out.println("Il n'y a plus de chemin de croissance. Le couplage courant est maximal, arrêt de l'algorithme.");

    }

    /**
     * Recherche d'un chemin augmentant à partir d'un seul sommet
     * @param n le sommet d'où commencer le chemin
     * @return une liste de noeuds représentant le chemin augmentant trouvé
     */
    public ArrayList<Noeud> ameliorer(Noeud n) {
        return ameliorer(new ArrayList<Noeud>(Arrays.asList(n)));
    }

    /**
     * Recherche d'un chemin de croissance à partir d'une chaîne de sommets déjà trouvée
     * La fonction est appelée récursivement jusqu'à ce qu'un chemin de croissance ne puisse plus être trouvé.
     * @param chaine la chaîne à prolonger
     * @return une liste de noeuds représentant le chemin de croissance trouvé
     */
    public ArrayList<Noeud> ameliorer(ArrayList<Noeud> chaine) { //Trouver un chemin augmentant à partir d'une chaine donnée
        ArrayList<Noeud> chaine_alternee = new ArrayList<Noeud>();
        Transparent last = (Transparent)chaine.get(chaine.size() - 1); //On récupère le dernier sommet de la chaine
        boolean chemin_de_croissance_trouve = false;
        for (Noeud voisin : graph.get(last)) { //Pour tous les voisins de ce sommet
            if (!chaine.contains(voisin)) {
                if (!voisin.isMarked()) { //Si un voisin non saturé est trouvé, il y a un chemin de croissance : les deux sommets aux extrémités sont insaturés.
                    chaine.add(voisin);
                    chaine_alternee = chaine; //chaine alternée = chaine U voisin
                    last.setMarked(true);
                    voisin.setMarked(true);
                    chemin_de_croissance_trouve = true;
                    break;
                } else { //Si un voisin saturé est trouvé
                    Noeud t = opposite_matching.get(voisin); //On récupère le partenaire de voisin dans le couplage (un transparent depuis une page) pour continuer le chemin.
                    chaine.add(voisin);
                    chaine.add(t);
                    return ameliorer(chaine);
                }
            }
            if (!chemin_de_croissance_trouve) {
                chaine_alternee.clear();
            }
        }
        return chaine_alternee;
    }

    //Appliquer le chemin augmentant pour modifier le couplage
    public boolean improveMatching(ArrayList<Noeud> chemin) {
        if (!chemin.isEmpty()) {
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
            return true;
        } else {
            return false;
        }
    }

    //Affichage du couplage en console
    public void printMatching() {
        for (Transparent t : matching.keySet()) {
            System.out.println(t.getName() + " <-> " + matching.get(t).getName());
        }
    }

}
