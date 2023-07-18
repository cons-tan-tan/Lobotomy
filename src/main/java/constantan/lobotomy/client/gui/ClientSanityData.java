package constantan.lobotomy.client.gui;

public class ClientSanityData {
    private static int playerSanity;
    private static int playerMaxSanity;

    public static int getPlayerSanity() {
        return playerSanity;
    }

    public static void setPlayerSanity(int sanity) {
        ClientSanityData.playerSanity = sanity;
    }

    public static int getPlayerMaxSanity() {
        return playerMaxSanity;
    }

    public static void setPlayerMaxSanity(int playerMaxSanity) {
        ClientSanityData.playerMaxSanity = playerMaxSanity;
    }
}
