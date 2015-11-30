/**
 * Exception appelée par la variante One-VS-ZeroOne si le couplage trouvé n'est pas compatible avec les contraintes de la variante (Tout transparent est associé à exactement une page)
 */
public class ImpossibleMatchingException extends Exception {

    public ImpossibleMatchingException(String message) {
        super(message);
    }

}
