package ui;

/**
 * ANSI terminal colours. No external libraries required.
 *
 * Disable colours with:
 *   java -DnoColor=true -cp build app.Main
 * or by setting the NO_COLOR environment variable.
 */
public final class AnsiColors {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String BOLD = "\u001B[1m";

    private static final boolean ENABLED =
            !Boolean.getBoolean("noColor")
            && System.getenv("NO_COLOR") == null
            && !"dumb".equalsIgnoreCase(System.getenv("TERM"));

    private AnsiColors() {
    }

    public static String paint(String text, String colour) {
        return ENABLED ? colour + text + RESET : text;
    }

    public static String satisfaction(int value) {
        String colour = value >= 70 ? GREEN : (value >= 31 ? YELLOW : RED);
        return paint(Integer.toString(value), colour);
    }
}
