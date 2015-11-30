import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe représentant un noeud dans le graphe, qui est soit un <i>Transparent</i> soit une <i>Page</i>.
 *
 * @author Clément Bauchet
 * @author Mélissa Obodje
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

    /**
     * Lecture des mots dans le fichier, pour remplir wordList.
     * @param file le chemin vers le fichier à lire
     */
    public void readFile(String file) {
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
                wordList.addAll(mots);
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Retourne le nom du noeud (pour la visualisation)
     * @return le nom du noeud
     */
    public String getName() {
        return name;
    }

    /**
     * Change le nom du noeud (pour la visualisation)
     * @param name le nom à donner
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retourne l'ensemble contenant tous les mots distincts du document
     * @return l'ensemble des mots du document
     */
    public Set<String> getWordList() {
        return wordList;
    }

    /**
     * Retourne un booléen indiquant si le noeud est marqué, c'est-à-dire s'il fait partie du couplage.
     * @return vrai si le noeud est dans le couplage, faux sinon
     */
    public boolean isMarked() {
        return marked;
    }

    /**
     * Change le marquage du noeud
     * @param marked vrai ou faux
     */
    public void setMarked(boolean marked) {
        this.marked = marked;
    }


}
