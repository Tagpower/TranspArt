import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * Created by clement on 15/11/15.
 */
public abstract class Method {

    protected Map<Noeud, ArrayList<Noeud>> graph;
    protected ArrayList<Transparent> listeTrans;
    protected ArrayList<Page> listePages;
    protected Set<String> important_words;



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

    public void setGraph(Map<Noeud, ArrayList<Noeud>> g) { //Only for debug
        graph = g;
    }


    //Enregistrer le graphe en format dot
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
            pw.println(t.getName() + "[label=" + t.getName() + "]");
            for (Noeud p : graph.get(t)) {
                pw.println(t.getName() + " -- " + p.getName());
                //pw.println(p.getName() + "[label=" + p.getName() + "]");
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
