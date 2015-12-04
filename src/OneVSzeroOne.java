import java.util.*;

/**
 * Classe résolvant le problème de couplage maximum dans un graphe biparti - Variante One-VS-ZeroOne :
 * Variante du problème qui associe <b>tout transparent à exactement une page</b>.
 * Une page ne peut pas être associée à plusieurs transparents.
 *
 * @author Clément Bauchet
 * @author Mélissa Obodje
 */
public class OneVSzeroOne extends Method {

    private HashMap<Page, Transparent> opposite_matching; //Couplage opposé, utilisé pour retrouver un transparent à partir d'une page dans le couplage initial.
    private ArrayList<HashMap<Transparent, Page>> previous_matchings; //Liste des couplages trouvés. Sert à détecter si l'algorithme ne se termine pas.
    private boolean chemin_de_croissance = true;

    public OneVSzeroOne(boolean v) {
        listePages = new ArrayList<Page>(); //Liste des noeuds de l'ensemble Page
        listeTrans = new ArrayList<Transparent>(); //Liste des noeuds de l'ensemble Transparent
        important_words = new HashSet<String>(); //Ensemble des mots du dictionnaire
        graph = new HashMap<Noeud, ArrayList<Noeud>>(); //Graphe correspondant au problème, sous forme de listes d'adjacence.
        matching = new HashMap<Transparent, Page>(); //Couplage transparent -> page
        opposite_matching = new HashMap<Page, Transparent>(); //Couplage page -> transparent

        verbose = v; //Paramètre de verbosité
    }

    /**
     * Retourne un booléen indiquant si le couplage est maximal, c'est-à-dire si le maximum de transparents sont couplés.
     * @return vrai si le couplage est maximal
     */
    public boolean isMaxMatching() {
        return (matching.size() == listeTrans.size() || !chemin_de_croissance);
    }


    /**
     * Fonction permettant de trouver un couplage maximal, par remplissage d'un premier couplage trivial, puis par améliorations successives en trouvant un chemin augmentant.
     * @throws ImpossibleMatchingException s'il n'est pas possible de coupler tous les transparents, pour une des raisons suivantes :
     *      -S'il y a moins de pages que de transparents
     *      -Si des transparents ne possèdent pas de voisins (pas de pages à coupler)
     *      -Si la cardinalité du couplage maximal n'est pas égale à celle de l'ensemble des transparents.
     */
    public void findMaxMatching() throws ImpossibleMatchingException {

        if (listeTrans.size() > listePages.size()) {
            throw new ImpossibleMatchingException("Couplage impossible : il y a plus de transparents que de pages.");
        }

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
            for (Noeud n : listeTrans) { //Pour tout transparent n non marqué
                if (!n.isMarked()) {
                    ArrayList<Noeud> chemin_augmentant = ameliorer(n); //Chercher un chemin augmentant dans le graphe.
                    chemin_de_croissance = improveMatching(chemin_augmentant); //Améliorer le couplage
                }
            }
        }
        if(matching.size() < listeTrans.size()) {
            throw new ImpossibleMatchingException("Couplage optimal impossible : il n'est pas possible de coupler tous les transparents de ce graphe.");
        }

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
     * Recherche d'un chemin augmentant à partir d'une chaîne de sommets déjà trouvée
     * La fonction est appelée récursivement jusqu'à ce que le chemin ne puisse plus être amélioré.
     * @param chaine la chaîne augmentante à prolonger
     * @return une liste de noeuds représentant le chemin augmentant trouvé
     */
    public ArrayList<Noeud> ameliorer(ArrayList<Noeud> chaine) { //Rechercher un chemin de croissance à partir d'une chaine donnée
        ArrayList<Noeud> chaine_alternee = new ArrayList<Noeud>();
        Noeud last = chaine.get(chaine.size() - 1); //On récupère le dernier sommet de la chaine
        boolean chemin_de_croissance_trouve = false;
        for (Noeud voisin : graph.get(last)) { //Pour tous les voisins de ce sommet
            if (!chaine.contains(voisin)) {
                if (!voisin.isMarked()) {//Si un voisin non saturé est trouvé, il y a un chemin de croissance : les deux sommets aux extrémités sont insaturés.
                    chaine.add(voisin);
                    chaine_alternee = chaine; //chaine alternée = chaine U voisin
                    last.setMarked(true);
                    voisin.setMarked(true);
                    chemin_de_croissance_trouve = true;
                    break;
                } else { //Si un voisin saturé est trouvé
                    Noeud t = opposite_matching.get(voisin); //On récupère le partenaire de voisin dans le couplage (un transparent depuis une page)
                    chaine.add(voisin);
                    chaine.add(t);
                    return ameliorer(chaine); //chaine = chaine U voisin U t
                }
            }
            if (!chemin_de_croissance_trouve) {
                chaine_alternee.clear();
            }
        }
        return chaine_alternee; //retourner la chaine trouvée
    }

    /**
     * Fonction appliquant un chemin augmentant pour modifier (voire améliorer) le couplage actuellement trouvé
     * @param chemin le chemin augmentant à appliquer
     */
    public boolean improveMatching(ArrayList<Noeud> chemin) {
        if (!chemin.isEmpty()) {
            boolean ajouter_arc = true; //On alterne entre la création et la destruction d'un couplage
            for (int i = 0; i < chemin.size() - 1; i++) { //Pour chaque arête du chemin
                if (ajouter_arc) { //Ajouter une arête entre deux sommets non liés
                    matching.put((Transparent) chemin.get(i), (Page) chemin.get(i + 1));
                    opposite_matching.put((Page) chemin.get(i + 1), (Transparent) chemin.get(i));
                } else {          //Retirer les arêtes déjà présentes sur le chemin
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

    /**
     * Affichage du couplage dans la console.
     */
    public void printMatching() {
        for (Transparent t : matching.keySet()) {
            System.out.println(t.getName() + " <-> " + matching.get(t).getName());
        }
        assert (matching.size() == opposite_matching.size());
    }

}
