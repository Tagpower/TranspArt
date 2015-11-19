import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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




}
