public class Page extends Noeud {

    public static int nbPages = 0;

    public Page() {
        super();
    }

    public Page(String file) {
        super(file);
        nbPages++;
        setName("P"+nbPages);
    }

}
