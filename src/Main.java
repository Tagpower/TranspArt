import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

/**
 * Classe principale
 *
 */
public class Main {

    public static void main(String args[]) { //Argument 1 : Nom du dico, Argument 2 : seuil de similarité, Argument 3 : verbose

        String path_dico = "";
        double seuil = 0.1;
        boolean verbose = false;

        switch (args.length) {
            case 3:
                if (args[2].equals("verbose")) verbose = true;
            case 2:
                seuil = Double.parseDouble(args[1]);
            case 1:
                path_dico = args[0];
                break;
            default:
                System.out.println("Usage : java Main chemin_du_dictionnaire.txt [seuil_de_similarite] [verbose]\n" +
                                   "\tchemin_du_dictionnaire : le fichier texte contenant les mots importants\n" +
                                   "\tseuil_de_similarite : valeur reelle entre 0 et 1 indiquant le taux de similarite minimum pour lier deux documents\n" +
                                   "\tverbose : taper 'verbose' pour imprimer des informations supplementaires en console");
                System.exit(0);
        }

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

        one_vs_01.readDico(path_dico);
        z1_vs_01.readDico(path_dico);
        z1_vs_m.readDico(path_dico);


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

        try {
            System.out.println("--- VARIANTE 1-VS-01 ---");
            one_vs_01.build_graph(seuil, verbose);
            one_vs_01.findMaxMatching();
            one_vs_01.printMatching();
            one_vs_01.save_graph("oneVS01");
        } catch (ImpossibleMatchingException e) {
            System.out.println("La variante 1-VS-01 n'a pas de solution.\nRecours à la variante ZeroOne-VS-ZeroOne");
            for (Page p : z1_vs_01.getListePages()) {
                p.setMarked(false);
            }
            for (Transparent t : z1_vs_01.getListeTrans()) {
                t.setMarked(false);
            }

            System.out.println("--- VARIANTE 01-VS-01 ---");
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
        System.out.println("--- VARIANTE 01-VS-MANY ---");
        z1_vs_m.build_graph(seuil, verbose);
        z1_vs_m.findMaxMatching();
        z1_vs_m.printMatching();
        z1_vs_m.save_graph("z1vsM");














    }
}
