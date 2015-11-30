/**
 * Exception appelée par la variante One-VS-ZeroOne si le couplage trouvé n'est pas compatible avec les contraintes de la variante (Tout transparent doit être associé à exactement une page)
 *
 * @author Clément Bauchet
 * @author Mélissa Obodje
 */
public class ImpossibleMatchingException extends Exception {

    public ImpossibleMatchingException(String message) {
        super(message);
    }

}
