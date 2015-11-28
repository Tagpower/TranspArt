import java.io.*;
import java.util.*;

/**
 * Created by clement on 15/11/15.
 */
public abstract class Method {

    protected Map<Noeud, ArrayList<Noeud>> graph;
    protected ArrayList<Transparent> listeTrans;
    protected ArrayList<Page> listePages;
    protected Set<String> important_words;

    protected HashMap<Transparent, Page> matching; //Couplage trouvé


    //Remplissage du dictionnaire de mots importants
    public void readDico(String file) {
        try {
            InputStream ips = new FileInputStream(file);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);

            String line;
            //Lecture de toutes les lignes
            while ((line = br.readLine()) != null) {
                ArrayList<String> mots_tmp = new ArrayList<String>(Arrays.asList(line.split("\\s+|,\\s*|\\.\\s*")));
                ArrayList<String> mots = new ArrayList<String>();
                for (String s : mots_tmp) {
                    mots.add(s.toLowerCase());
                }
                this.important_words.addAll(mots);
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

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

    //WIP : Autre mesure
    public void build_graph(double alpha) { //Construire le graphe biparti entre les Transparents et les Pages. Les arêtes sont les associations possibles entre eux.
        Set<String> intersect = new HashSet<>(); //Mots en commun d'un transparent, d'une page et du dictionnaire.
        Set<String> union = new HashSet<>();

        for (Transparent t : listeTrans) {
            ArrayList<Noeud> successors = new ArrayList<Noeud>();
            for (Page p : listePages) { //Obtenir l'intersection des mots du transparents, ceux de la page et les mots du dictionnaire.
                intersect.clear();
                intersect.addAll(t.getWordList());
                intersect.retainAll(p.getWordList());
                union.addAll(t.getWordList());
                union.addAll(p.getWordList());
                if (!intersect.isEmpty() && !union.isEmpty()) {
                    intersect.retainAll(important_words);
                    union.retainAll(important_words);
                }
                double taux_commun = new Double(intersect.size()) / new Double(union.size());
                System.out.println("Le transparent " + (listeTrans.indexOf(t)+1) + " a " + (taux_commun*100) + " % de mots en commun avec la page " + (listePages.indexOf(p)+1));
                if (taux_commun >= alpha) {
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




    //DEBUG
    public void setGraph(Map<Noeud, ArrayList<Noeud>> g) { //Only for debug
        graph = g;
    }

    public ArrayList<Transparent> getListeTrans() {
        return listeTrans;
    }

    public void setListeTrans(ArrayList<Transparent> listeTrans) {
        this.listeTrans = listeTrans;
    }

    public ArrayList<Page> getListePages() {
        return listePages;
    }

    public void setListePages(ArrayList<Page> listePages) {
        this.listePages = listePages;
    }

    //Enregistrer le graphe en format dot et svg
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
        for (Page p : listePages) {
            pw.println("\t" + p.getName() + "[label=" + p.getName() + "]" /* + (t.isMarked() ? "[color=red]" : "") */ );
        }
        for (Transparent t : listeTrans) {
            pw.println("\t" + t.getName() + "[label=" + t.getName() + "]" /* + (t.isMarked() ? "[color=red]" : "")*/ );
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
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract boolean isMaxMatching();

    public abstract void findMaxMatching();




}
