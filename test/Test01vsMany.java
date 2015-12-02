import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by clement on 18/11/15.
 */
public class Test01vsMany {

    public static void main(String args[]) {

        ZeroOneVSmany z1_vs_many = new ZeroOneVSmany(true);

            z1_vs_many.readDico(args[0]);

            Page p1 = new Page("Pages/P1.txt");
            z1_vs_many.listePages.add(p1);

            Page p2 = new Page("Pages/P2.txt");
            z1_vs_many.listePages.add(p2);

            Page p3 = new Page("Pages/P3.txt");
            z1_vs_many.listePages.add(p3);

            Transparent t1 = new Transparent("Transparents/T1.txt");
            z1_vs_many.listeTrans.add(t1);

            Transparent t2 = new Transparent("Transparents/T2.txt");
            z1_vs_many.listeTrans.add(t2);

            Transparent t3 = new Transparent("Transparents/T3.txt");
            z1_vs_many.listeTrans.add(t3);

            System.out.println("P1 : " + p1.getWordList().toString());
            System.out.println("P2 : " + p2.getWordList().toString());
            System.out.println("P3 : " + p3.getWordList().toString());
            System.out.println("T1 : " + t1.getWordList().toString());
            System.out.println("T2 : " + t2.getWordList().toString());
            System.out.println("T3 : " + t3.getWordList().toString());
            System.out.println("Dico : " + z1_vs_many.important_words.toString());

            z1_vs_many.graph = new HashMap<Noeud, ArrayList<Noeud>>();

            z1_vs_many.build_graph(0.1);

            System.out.println(z1_vs_many.graph.toString());

            z1_vs_many.findMaxMatching();

            z1_vs_many.printMatching();

            z1_vs_many.save_graph("test01vsM", "test");

    }
}
