import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by clement on 19/11/15.
 */
public class Noeud {

    private String name; //Nom du document
    private Set<String> wordList; //Liste des mots du document
    private boolean marked; //Un noeud est marqué s'il est couplé avec un autre noeud

    public Noeud() {
        wordList = new HashSet<String>();
    }

    public Noeud(String file) {
        wordList = new HashSet<String>();
        this.readFile(file);
        name = file.substring(0, file.length()-4);
    }

    public void readFile(String file) { //Lecture des mots du fichier
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
                    mots.add(s.toLowerCase());
                }
                wordList.addAll(mots);
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getWordList() {
        return wordList;
    }

    public void setWordList(Set<String> newList) {
        wordList = newList;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }


}
