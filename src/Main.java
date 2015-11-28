import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by clement on 18/11/15.
 */
public class Main {

    public static void main(String args[]) {

        OneVSzeroOne one_vs_01 = new OneVSzeroOne();

        one_vs_01.readDico(args[0]);

        File rep_pages = new File("./Pages");
        File rep_trans = new File("./Transparents");

        //Création d'un filtre pour n'accepter que des fichiers texte
        FileFilter only_txt = new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.getName().endsWith(".txt")) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        File[] page_files = rep_pages.listFiles(only_txt);
        File[] trans_files = rep_trans.listFiles(only_txt);

        for (int i = 0; i < page_files.length; i++) {
            one_vs_01.listePages.add(new Page("Pages/"+page_files[i].getName()));
        }
        for (int i = 0; i < trans_files.length; i++) {
            one_vs_01.listeTrans.add(new Transparent("Transparents/"+trans_files[i].getName()));
        }

        for (Page p : one_vs_01.listePages) {
            System.out.println(p.getName() + " : " + p.getWordList().toString());
        }
        for (Transparent t : one_vs_01.listeTrans) {
            System.out.println(t.getName() + " : " + t.getWordList().toString());
        }

        System.out.println("Dico : " + one_vs_01.important_words.toString());

        one_vs_01.graph = new HashMap<Noeud, ArrayList<Noeud>>();
        one_vs_01.build_graph();
        System.out.println(one_vs_01.graph.toString());

        one_vs_01.findMaxMatching();

        one_vs_01.printMatching();

        one_vs_01.save_graph("aaa");

    }
}
