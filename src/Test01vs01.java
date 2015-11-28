import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by clement on 25/11/15.
 */
public class Test01vs01 {
    //TEST
    public static void main(String[] args) {

        ZeroOneVSzeroOne z1_vs_01 = new ZeroOneVSzeroOne();

        Transparent t1 = new Transparent();
        Transparent t2 = new Transparent();
        Transparent t3 = new Transparent();
        Transparent t4 = new Transparent();
        Transparent t5 = new Transparent();
        Transparent t6 = new Transparent();
        Transparent t7 = new Transparent();
        Transparent t8 = new Transparent();
        Transparent t9 = new Transparent();
        t1.setName("T1");
        t2.setName("T2");
        t3.setName("T3");
        t4.setName("T4");
        t5.setName("T5");
        t6.setName("T6");
        t7.setName("T7");
        t8.setName("T8");
        t9.setName("T9");
        z1_vs_01.listeTrans.add(t1);
        z1_vs_01.listeTrans.add(t2);
        z1_vs_01.listeTrans.add(t3);
        z1_vs_01.listeTrans.add(t4);
        z1_vs_01.listeTrans.add(t5);
        z1_vs_01.listeTrans.add(t6);
        z1_vs_01.listeTrans.add(t7);
        z1_vs_01.listeTrans.add(t8);
        z1_vs_01.listeTrans.add(t9);

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
        z1_vs_01.listePages.add(p1);
        z1_vs_01.listePages.add(p2);
        z1_vs_01.listePages.add(p3);
        z1_vs_01.listePages.add(p4);
        z1_vs_01.listePages.add(p5);
        z1_vs_01.listePages.add(p6);
        z1_vs_01.listePages.add(p7);
        z1_vs_01.listePages.add(p8);
        z1_vs_01.listePages.add(p9);

        Map<Noeud, ArrayList<Noeud>> leSuperGrapheDeTest = new HashMap<Noeud, ArrayList<Noeud>>();

        leSuperGrapheDeTest.put(t1, new ArrayList<Noeud>(Arrays.asList(p1, p6)));
        leSuperGrapheDeTest.put(t2, new ArrayList<Noeud>(Arrays.asList(p2)));
        leSuperGrapheDeTest.put(t3, new ArrayList<Noeud>(Arrays.asList(p3, p6)));
        leSuperGrapheDeTest.put(t4, new ArrayList<Noeud>(Arrays.asList(p2, p6)));
        leSuperGrapheDeTest.put(t5, new ArrayList<Noeud>(Arrays.asList(p1, p2, p3)));
        leSuperGrapheDeTest.put(t6, new ArrayList<Noeud>(Arrays.asList(p2, p4, p8)));
        leSuperGrapheDeTest.put(t7, new ArrayList<Noeud>(Arrays.asList(p4, p7, p9)));
        leSuperGrapheDeTest.put(t8, new ArrayList<Noeud>(Arrays.asList(p3, p5, p8)));
        leSuperGrapheDeTest.put(t9, new ArrayList<Noeud>(Arrays.asList(p6)));

        leSuperGrapheDeTest.put(p1, new ArrayList<Noeud>(Arrays.asList(t1, t5)));
        leSuperGrapheDeTest.put(p2, new ArrayList<Noeud>(Arrays.asList(t2, t4, t5, t6)));
        leSuperGrapheDeTest.put(p3, new ArrayList<Noeud>(Arrays.asList(t3, t5, t8)));
        leSuperGrapheDeTest.put(p4, new ArrayList<Noeud>(Arrays.asList(t6, t7)));
        leSuperGrapheDeTest.put(p5, new ArrayList<Noeud>(Arrays.asList(t8)));
        leSuperGrapheDeTest.put(p6, new ArrayList<Noeud>(Arrays.asList(t1, t3, t4, t9)));
        leSuperGrapheDeTest.put(p7, new ArrayList<Noeud>(Arrays.asList(t7)));
        leSuperGrapheDeTest.put(p8, new ArrayList<Noeud>(Arrays.asList(t6, t8)));
        leSuperGrapheDeTest.put(p9, new ArrayList<Noeud>(Arrays.asList(t7)));

        z1_vs_01.setGraph(leSuperGrapheDeTest);
        System.out.println(z1_vs_01.graph.toString());

        z1_vs_01.findMaxMatching();

        z1_vs_01.printMatching();

        z1_vs_01.save_graph("test01vs01");

    }
}
