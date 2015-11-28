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


    public abstract void build_graph();

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
            pw = new PrintWriter(new BufferedWriter(new FileWriter("./dot/" + S + ".dot")));
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
