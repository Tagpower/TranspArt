import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by clement on 25/11/15.
 */
public class Test {
    //TEST
    public static void main(String[] args) {

        OneVSzeroOne one_vs_01 = new OneVSzeroOne(true);

        Transparent t1 = new Transparent();
        Transparent t2 = new Transparent();
        Transparent t3 = new Transparent();
        Transparent t4 = new Transparent();
        Transparent t5 = new Transparent();
        t1.setName("T1");
        t2.setName("T2");
        t3.setName("T3");
        t4.setName("T4");
        t5.setName("T5");
        one_vs_01.listeTrans.add(t1);
        one_vs_01.listeTrans.add(t2);
        one_vs_01.listeTrans.add(t3);
        one_vs_01.listeTrans.add(t4);
        one_vs_01.listeTrans.add(t5);

        Page p1 = new Page();
        Page p2 = new Page();
        Page p3 = new Page();
        Page p4 = new Page();
        Page p5 = new Page();
        p1.setName("P1");
        p2.setName("P2");
        p3.setName("P3");
        p4.setName("P4");
        p5.setName("P5");
        one_vs_01.listePages.add(p1);
        one_vs_01.listePages.add(p2);
        one_vs_01.listePages.add(p3);
        one_vs_01.listePages.add(p4);
        one_vs_01.listePages.add(p5);

        Map<Noeud, ArrayList<Noeud>> grapheTest = new HashMap<Noeud, ArrayList<Noeud>>();

        grapheTest.put(t1, new ArrayList<Noeud>(Arrays.asList(p2, p3, p4)));
        grapheTest.put(t2, new ArrayList<Noeud>(Arrays.asList(p2, p3, p5)));
        grapheTest.put(t3, new ArrayList<Noeud>(Arrays.asList(p1, p3, p5)));
        grapheTest.put(t4, new ArrayList<Noeud>(Arrays.asList(p4)));
        grapheTest.put(t5, new ArrayList<Noeud>(Arrays.asList(p2)));

        grapheTest.put(p1, new ArrayList<Noeud>(Arrays.asList(t3)));
        grapheTest.put(p2, new ArrayList<Noeud>(Arrays.asList(t2, t5)));
        grapheTest.put(p3, new ArrayList<Noeud>(Arrays.asList(t1, t2, t3)));
        grapheTest.put(p4, new ArrayList<Noeud>(Arrays.asList(t1, t4)));
        grapheTest.put(p5, new ArrayList<Noeud>(Arrays.asList(t2, t3)));

        one_vs_01.setGraph(grapheTest);
        System.out.println(one_vs_01.graph.toString());
        one_vs_01.save_graph("test", "test");

        try {
            one_vs_01.findMaxMatching();
        } catch (ImpossibleMatchingException e) {
            e.printStackTrace();
        }

        one_vs_01.printMatching();

        one_vs_01.save_graph("test", "test");

    }
}
