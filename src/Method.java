import java.io.*;
import java.util.*;

/**
 * Classe abstraite regroupant les méthodes communes aux trois variantes du problème.
 *
 * @author Clément Bauchet
 * @author Mélissa Obodje
 */
public abstract class Method {

    protected Map<Noeud, ArrayList<Noeud>> graph;
    protected ArrayList<Transparent> listeTrans;
    protected ArrayList<Page> listePages;
    protected Set<String> important_words;

    protected HashMap<Transparent, Page> matching; //Couplage trouvé

    protected boolean verbose;


    /**
     * Fonction remplissant le dictionnaire des mots importants depuis un fichier texte.
     * @param file le chemin du fichier dictionnaire contenant les mots importants.
     */
    public void readDico(String file) {
        try {
            InputStream ips = new FileInputStream(file);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);

            String line;
            //Lecture de toutes les lignes
            while ((line = br.readLine()) != null) {
                ArrayList<String> mots_tmp = new ArrayList<String>(Arrays.asList(line.split("\\s+|,\\s*|\\.\\s*"))); //Ne garder que les mots, aucune ponctuation.
                ArrayList<String> mots = new ArrayList<String>();
                for (String s : mots_tmp) {
                    mots.add(s.toLowerCase()); //Tous les mots sont gardés en minuscule
                }
                this.important_words.addAll(mots);
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Construction du graphe biparti avec les <i>Transparents</i> d'un côté, et les <i>Pages</i> de l'autre.
     * Une arête est construite entre deux documents si et seulement si ils possèdent suffisamment de mots importants en commun.
     * Pour cette version, un seul mot en commun suffit.
     */
    public void build_graph(int nb_mots_min) { //Construire le graphe biparti entre les Transparents et les Pages. Les arêtes sont les associations possibles entre eux.
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
                if (intersect.size() >= nb_mots_min ) {
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

    /**
     * Construction du graphe biparti avec les <i>Transparents</i> d'un côté, et les <i>Pages</i> de l'autre.
     * Une arête est construite entre deux documents si et seulement si ils possèdent un taux de mots importants en commun suffisamment élevé.
     *
     * @param taux_commun_min le taux minimum de mots en commun à avoir pour créer une arête
     */
    public void build_graph(double taux_commun_min) { //Construire le graphe biparti entre les Transparents et les Pages. Les arêtes sont les associations possibles entre eux.
        Set<String> intersect = new HashSet<>(); //Mots en commun d'un transparent, d'une page et du dictionnaire.
        Set<String> union = new HashSet<>(); //Ensemble des mots du transparent et de la page qui sont présents dans le dictionnaire.

        for (Transparent t : listeTrans) {
            ArrayList<Noeud> successors = new ArrayList<Noeud>(); //Préparation des successeurs à mettre pour chaque noeud
            for (Page p : listePages) { //Obtenir l'intersection des mots du transparents, ceux de la page et les mots du dictionnaire.
                intersect.clear();
                intersect.addAll(t.getWordList());
                intersect.retainAll(p.getWordList());
                union.clear();  //Obtenir l'union des mots du transparent et des mots de la page
                union.addAll(t.getWordList());
                union.addAll(p.getWordList());
                if (!intersect.isEmpty() && !union.isEmpty()) { //Filtrer les deux ensembles avec les mots du dictionnaire
                    intersect.retainAll(important_words);
                    union.retainAll(important_words);
                }
                double taux_commun = new Double(intersect.size()) / new Double(union.size()); //Mesure du taux de mots importants en commun
                if (verbose) {
                    System.out.println("Le transparent " + (listeTrans.indexOf(t)+1) + " a " + (taux_commun*100) + " % de mots en commun avec la page " + (listePages.indexOf(p)+1));
                }
                if (taux_commun >= taux_commun_min) {
                    successors.add(p); //Si la proportion de mots importants est plus grande que le seuil spécifié, une association des deux documents est alors envisageable : construire une arête.
                }
            }
            graph.put(t, successors);
            for (Noeud s : successors) { //Ajout de t comme voisin/successeur de la page, et inversement
                graph.putIfAbsent(s, new ArrayList<Noeud>());
                graph.get(s).add(t);
            }
        }
    }

    /**
     * Retourne la liste des noeuds de type <i>Page</i> traîtés par la méthode
     * @return la liste des pages du graphe
     */
    public ArrayList<Page> getListePages() {
        return this.listePages;
    }

    /**
     * Retourne la liste des noeuds de type <i>Transparent</i> traîtés par la méthode
     * @return la liste des transparents du graphe
     */
    public ArrayList<Transparent> getListeTrans() {
        return this.listeTrans;
    }

    /**
     * Change le graphe actuellement considéré par le problème (Fonction de débug)
     * @param g le graphe à établir
     */
    public void setGraph(Map<Noeud, ArrayList<Noeud>> g) { //Only for debug
        graph = g;
    }

    /**
     * Enregistrement du graphe sous forme d'un schéma en format .dot et .svg, pour la visualisation.
     * @param S le nom sous lequel enregistrer les fichiers
     */
    public void save_graph(String S, String title) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter("./dot/" + S)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert pw != null;

        //Enregistrer le graphe au format .dot
        pw.println("graph " + S + "{");
        pw.println("labelloc=\"t\";");
        pw.println("label=\""+ title +"\";");

        for (Page p : listePages) {
            pw.println("\t" + p.getName() + "[label=" + p.getName() + ", style=filled, fillcolor=\"#cccccc\"]" /* + (t.isMarked() ? "[color=red]" : "") */ );
        }
        for (Transparent t : listeTrans) {
            pw.println("\t" + t.getName() + "[label=" + t.getName() + ", style=filled, fillcolor=\"#999999\"]" /* + (t.isMarked() ? "[color=red]" : "")*/ );
            for (Noeud n : graph.get(t)) {
                if (matching.get(t) == n) {
                    pw.println("\t" + t.getName() + " -- " + n.getName() + "[color=red,penwidth=3.0]");
                } else {
                    pw.println("\t" + t.getName() + " -- " + n.getName());
                }
            }
        }
        pw.println("}");
        pw.close();

        //Créer un fichier .svg du graphe et son couplage.
        try {
            Process p = Runtime.getRuntime().exec("dot -O -Tsvg ./dot/" + S);
            p.waitFor();
            System.out.println("Graphe sauvegardé sous ./dot/" + S + ".svg");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retourne un booléen indiquant si le couplage est optimal, c'est-à-dire si tous les transparents sont couplés.
     * @return vrai si le couplage est optimal
     */
    public abstract boolean isMaxMatching();

    /**
     * Fonction permettant de trouver un couplage maximal
     * Dans la variante OneVSzeroOne, renvoie une exception s'il n'est pas possible de coupler tous les transparents.
     * @throws ImpossibleMatchingException si le couplage optimal n'est pas trouvable
     */
    public abstract void findMaxMatching() throws ImpossibleMatchingException;




}
