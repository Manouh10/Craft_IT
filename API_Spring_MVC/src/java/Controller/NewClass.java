public class NewClass {
    public static void main(String[] args) {
        // Chaîne représentant un nombre décimal
        String strNumber = "0.55";

        try {
            // Conversion de la chaîne en double
            double doubleNumber = Double.parseDouble(strNumber);

            // Affichage du résultat
            System.out.println("La chaîne convertie en double : " + doubleNumber);
        } catch (NumberFormatException e) {
            // Gestion de l'exception si la chaîne n'est pas un nombre valide
            System.err.println("La chaîne n'est pas un nombre valide.");
        }
    }
}
