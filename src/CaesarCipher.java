import java.util.*;

public class CaesarCipher {

    // Methode zur Verschlüsselung mit dem Caesar Cipher
    public static String encrypt(String text, int z) {
        StringBuilder result = new StringBuilder();

        for (char character : text.toCharArray()) {
            if (Character.isLetter(character)) {
                char base = Character.isUpperCase(character) ? 'A' : 'a';
                result.append((char) ((character - base + z) % 26 + base));
            } else {
                result.append(character);
            }
        }

        return result.toString();
    }

    // Methode zur Entschlüsselung mit bekanntem Schlüssel
    public static String decrypt(String text, int z) {
        return encrypt(text, 26 - z);  // Rückverschlüsselung durch inverse Rotation
    }

    // Brute-Force-Angriff: Erzeugt alle möglichen Klartexte
    public static Map<Integer, String> bruteForceAttack(String cipherText) {
        Map<Integer, String> possibleDecryptions = new HashMap<>();

        for (int key = 1; key < 26; key++) {
            String decrypted = decrypt(cipherText, key);
            possibleDecryptions.put(key, decrypted);
        }

        return possibleDecryptions;
    }

    // Methode zur Erkennung des korrekten Klartextes durch Wörterbuchabgleich
    public static String detectCorrectPlaintext(Map<Integer, String> candidates, Set<String> dictionary) {
        for (Map.Entry<Integer, String> entry : candidates.entrySet()) {
            String decryptedText = entry.getValue();
            String[] words = decryptedText.split(" ");

            int matches = 0;
            for (String word : words) {
                if (dictionary.contains(word.toLowerCase())) {
                    matches++;
                }
            }

            // Wenn genug Wörter übereinstimmen, könnte dies der Klartext sein
            if (matches > words.length / 2) {
                return "Vermutlich richtiger Text mit Schlüssel " + entry.getKey() + ": " + decryptedText;
            }
        }
        return "Kein eindeutiger Klartext erkannt.";
    }

    public static void main(String[] args) {
        // Beispiel-Text und Schlüssel
        String originalText = "Dies ist eine geheime Nachricht";
        int key = 5;

        // Verschlüsseln
        String encryptedText = encrypt(originalText, key);
        System.out.println("Verschlüsselter Text: " + encryptedText);

        // Brute-Force-Angriff
        System.out.println("\nBrute-Force-Attacke:");
        Map<Integer, String> decryptions = bruteForceAttack(encryptedText);

        for (Map.Entry<Integer, String> entry : decryptions.entrySet()) {
            System.out.println("Schlüssel " + entry.getKey() + ": " + entry.getValue());
        }

        // Wörterbuch für automatische Erkennung des Klartextes
        Set<String> dictionary = new HashSet<>(Arrays.asList("dies", "ist", "eine", "geheime", "nachricht", "der", "die", "und", "zu"));

        // Erkennung des richtigen Klartextes
        String detectedPlaintext = detectCorrectPlaintext(decryptions, dictionary);
        System.out.println("\n" + detectedPlaintext);
    }
}
