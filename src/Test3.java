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
        t1.setName("T1");
        t2.setName("T2");
        t3.setName("T3");
        t4.setName("T4");
        z1_vs_many.listeTrans.add(t1);
        z1_vs_many.listeTrans.add(t2);
        z1_vs_many.listeTrans.add(t3);
        z1_vs_many.listeTrans.add(t4);

        Page p1 = new Page();
        Page p2 = new Page();
        Page p3 = new Page();
        Page p4 = new Page();
        p1.setName("P1");
        p2.setName("P2");
        p3.setName("P3");
        p4.setName("P4");
        z1_vs_many.listePages.add(p1);
        z1_vs_many.listePages.add(p2);
        z1_vs_many.listePages.add(p3);
        z1_vs_many.listePages.add(p4);

        Map<Noeud, ArrayList<Noeud>> leSuperGrapheDeTest = new HashMap<Noeud, ArrayList<Noeud>>();

        leSuperGrapheDeTest.put(t1, new ArrayList<Noeud>(Arrays.asList(p1)));
        leSuperGrapheDeTest.put(t2, new ArrayList<Noeud>(Arrays.asList(p1, p2)));
        leSuperGrapheDeTest.put(t3, new ArrayList<Noeud>(Arrays.asList(p1, p3)));
        leSuperGrapheDeTest.put(t4, new ArrayList<Noeud>(Arrays.asList(p1, p2, p3)));

        leSuperGrapheDeTest.put(p1, new ArrayList<Noeud>(Arrays.asList(t1, t2, t3, t4)));
        leSuperGrapheDeTest.put(p2, new ArrayList<Noeud>(Arrays.asList(t2, t4)));
        leSuperGrapheDeTest.put(p3, new ArrayList<Noeud>(Arrays.asList(t3, t4)));
        leSuperGrapheDeTest.put(p4, new ArrayList<Noeud>());

        z1_vs_many.setGraph(leSuperGrapheDeTest);
        System.out.println(z1_vs_many.graph.toString());
        z1_vs_many.save_graph("test01M");

        z1_vs_many.findMaxMatching();

        z1_vs_many.printMatching();

        z1_vs_many.save_graph("test01M");

    }
}
