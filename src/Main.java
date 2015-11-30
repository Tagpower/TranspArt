import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

/**
 * Classe principale
 *
 */
public class Main {

    public static void main(String args[]) { //Argument 1 : Nom du dico, Argument 2 : seuil de similarité, Argument 3 : verbose

        //Récupération des répertoires contenant les pages & transparents
        File rep_pages = new File("./Pages");
        File rep_trans = new File("./Transparents");

        //Création d'un filtre pour n'accepter que des fichiers texte
        FileFilter only_txt = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".txt");
            }
        };

        File[] page_files = rep_pages.listFiles(only_txt);
        File[] trans_files = rep_trans.listFiles(only_txt);
        Arrays.sort(page_files);
        Arrays.sort(trans_files);

        OneVSzeroOne one_vs_01 = new OneVSzeroOne();
        ZeroOneVSzeroOne z1_vs_01 = new ZeroOneVSzeroOne();
        ZeroOneVSmany z1_vs_m = new ZeroOneVSmany();

        one_vs_01.readDico(args[0]);
        z1_vs_01.readDico(args[0]);
        z1_vs_m.readDico(args[0]);

        boolean verbose = false;
        if (args[2].equals("verbose")) {
            verbose = true;
        }

        for (int i = 0; i < page_files.length; i++) {
            Page p = new Page("Pages/" + page_files[i].getName());
            one_vs_01.listePages.add(p);
            z1_vs_01.listePages.add(p);
            z1_vs_m.listePages.add(p);

        }
        for (int i = 0; i < trans_files.length; i++) {
            Transparent t = new Transparent("Transparents/" + trans_files[i].getName());
            one_vs_01.listeTrans.add(t);
            z1_vs_01.listeTrans.add(t);
            z1_vs_m.listeTrans.add(t);
        }

        double seuil = 0.001;
        if (!args[1].isEmpty()) {
            seuil = Double.parseDouble(args[1]);
        }

        try {
            one_vs_01.build_graph(seuil, verbose);
            one_vs_01.findMaxMatching();
            one_vs_01.printMatching();
            one_vs_01.save_graph("oneVS01");
        } catch (ImpossibleMatchingException e) {
            System.out.println("Recours à la variante ZeroOne-VS-ZeroOne");
            z1_vs_01.build_graph(seuil, verbose);
            z1_vs_01.findMaxMatching();
            z1_vs_01.printMatching();
            z1_vs_01.save_graph("z1vs01");
        }

        for (Page p : z1_vs_m.getListePages()) {
            p.setMarked(false);
        }
        for (Transparent t : z1_vs_m.getListeTrans()) {
            t.setMarked(false);
        }
        z1_vs_m.build_graph(seuil, verbose);
        z1_vs_m.findMaxMatching();
        z1_vs_m.printMatching();
        z1_vs_m.save_graph("z1vsM");














    }
}
