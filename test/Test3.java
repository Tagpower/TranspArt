import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by clement on 25/11/15.
 */
public class Test3 {
    //TEST
    public static void main(String[] args) {

        ZeroOneVSmany z1_vs_many = new ZeroOneVSmany();

        Transparent t1 = new Transparent();
        Transparent t2 = new Transparent();
        Transparent t3 = new Transparent();
        Transparent t4 = new Transparent();
        Transparent t5 = new Transparent();
        Transparent t6 = new Transparent();
        Transparent t7 = new Transparent();
        Transparent t8 = new Transparent();
        Transparent t9 = new Transparent();
        Transparent t10 = new Transparent();
        t1.setName("T1");
        t2.setName("T2");
        t3.setName("T3");
        t4.setName("T4");
        t5.setName("T5");
        t6.setName("T6");
        t7.setName("T7");
        t8.setName("T8");
        t9.setName("T9");
        t10.setName("T10");
        z1_vs_many.listeTrans.add(t1);
        z1_vs_many.listeTrans.add(t2);
        z1_vs_many.listeTrans.add(t3);
        z1_vs_many.listeTrans.add(t4);
        z1_vs_many.listeTrans.add(t5);
        z1_vs_many.listeTrans.add(t6);
        z1_vs_many.listeTrans.add(t7);
        z1_vs_many.listeTrans.add(t8);
        z1_vs_many.listeTrans.add(t9);
        z1_vs_many.listeTrans.add(t10);

        Page p1 = new Page();
        Page p2 = new Page();
        Page p3 = new Page();
        Page p4 = new Page();
        Page p5 = new Page();
        Page p6 = new Page();
        Page p7 = new Page();
        Page p8 = new Page();
        Page p9 = new Page();
        p1.setName("P1");
        p2.setName("P2");
        p3.setName("P3");
        p4.setName("P4");
        p5.setName("P5");
        p6.setName("P6");
        p7.setName("P7");
        p8.setName("P8");
        p9.setName("P9");
        z1_vs_many.listePages.add(p1);
        z1_vs_many.listePages.add(p2);
        z1_vs_many.listePages.add(p3);
        z1_vs_many.listePages.add(p4);
        z1_vs_many.listePages.add(p5);
        z1_vs_many.listePages.add(p6);
        z1_vs_many.listePages.add(p7);
        z1_vs_many.listePages.add(p8);
        z1_vs_many.listePages.add(p9);

        Map<Noeud, ArrayList<Noeud>> grapheTest = new HashMap<Noeud, ArrayList<Noeud>>();

        grapheTest.put(t1, new ArrayList<Noeud>(Arrays.asList(p1, p6)));
        grapheTest.put(t2, new ArrayList<Noeud>(Arrays.asList(p2)));
        grapheTest.put(t3, new ArrayList<Noeud>(Arrays.asList(p3, p6)));
        grapheTest.put(t4, new ArrayList<Noeud>(Arrays.asList(p2, p6)));
        grapheTest.put(t5, new ArrayList<Noeud>(Arrays.asList(p1, p2, p3)));
        grapheTest.put(t6, new ArrayList<Noeud>(Arrays.asList(p2, p4, p8)));
        grapheTest.put(t7, new ArrayList<Noeud>(Arrays.asList(p4, p7, p9)));
        grapheTest.put(t8, new ArrayList<Noeud>(Arrays.asList(p3, p5, p8)));
        grapheTest.put(t9, new ArrayList<Noeud>(Arrays.asList(p6)));
        grapheTest.put(t9, new ArrayList<Noeud>(Arrays.asList(p6)));
        grapheTest.put(t10, new ArrayList<Noeud>());


        grapheTest.put(p1, new ArrayList<Noeud>(Arrays.asList(t1, t5)));
        grapheTest.put(p2, new ArrayList<Noeud>(Arrays.asList(t2, t4, t5, t6)));
        grapheTest.put(p3, new ArrayList<Noeud>(Arrays.asList(t3, t5, t8)));
        grapheTest.put(p4, new ArrayList<Noeud>(Arrays.asList(t6, t7)));
        grapheTest.put(p5, new ArrayList<Noeud>(Arrays.asList(t8)));
        grapheTest.put(p6, new ArrayList<Noeud>(Arrays.asList(t1, t3, t4, t9)));
        grapheTest.put(p7, new ArrayList<Noeud>(Arrays.asList(t7)));
        grapheTest.put(p8, new ArrayList<Noeud>(Arrays.asList(t6, t8)));
        grapheTest.put(p9, new ArrayList<Noeud>(Arrays.asList(t7)));

        z1_vs_many.setGraph(grapheTest);
        System.out.println(z1_vs_many.graph.toString());

        z1_vs_many.findMaxMatching();

        z1_vs_many.printMatching();

        z1_vs_many.save_graph("test01_vs_M", "test");

    }
}
