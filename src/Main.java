import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by clement on 18/11/15.
 */
public class Main {

    public static void main(String args[]) {

        OneVSzeroOne one_vs_01 = new OneVSzeroOne();

            one_vs_01.readDico(args[0]);

        //TODO: Une méthode pour construire tous les trans. + toutes les pages en recherchant dans les dossiers

            Page p1 = new Page("Pages/P1.txt");
            one_vs_01.listePages.add(p1);

            Page p2 = new Page("Pages/P2.txt");
            one_vs_01.listePages.add(p2);

            Page p3 = new Page("Pages/P3.txt");
            one_vs_01.listePages.add(p3);

            Transparent t1 = new Transparent("Transparents/T1.txt");
            one_vs_01.listeTrans.add(t1);

            Transparent t2 = new Transparent("Transparents/T2.txt");
            one_vs_01.listeTrans.add(t2);

            Transparent t3 = new Transparent("Transparents/T3.txt");
            one_vs_01.listeTrans.add(t3);

            System.out.println("P1 : " + p1.getWordList().toString());
            System.out.println("P2 : " + p2.getWordList().toString());
            System.out.println("P3 : " + p3.getWordList().toString());
            System.out.println("T1 : " + t1.getWordList().toString());
            System.out.println("T2 : " + t2.getWordList().toString());
            System.out.println("T3 : " + t3.getWordList().toString());
            System.out.println("Dico : " + one_vs_01.important_words.toString());

            one_vs_01.graph = new HashMap<Noeud, ArrayList<Noeud>>();

            one_vs_01.build_graph();

            System.out.println(one_vs_01.graph.toString());

            one_vs_01.findMaxMatching();

            one_vs_01.printMatching();

    }
}
