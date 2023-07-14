package constantan.lobotomy.client.gui;

public class ClientSanityData {
    private static int playerSanity;

    public static int getPlayerSanity() {
        return playerSanity;
    }

    public static void setPlayerSanity(int sanity) {
        ClientSanityData.playerSanity = sanity;
    }
}
