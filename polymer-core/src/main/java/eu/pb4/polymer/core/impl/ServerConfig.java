package eu.pb4.polymer.core.impl;

public class ServerConfig {
    public String _c7 = "Displays vanilla/modded creatives tabs in /polymer creative";
    public boolean displayNonPolymerCreativeTabs = true;
    public String _c9 = "Makes server send additional block updates around clicked area";
    public boolean sendBlocksAroundClicked = true;
    public String _c11 = "Makes polymer report time it's handshake took";
    public boolean logHandshakeTime = false;
    public String _c12 = "Enables logging of BlockState ids rebuilds";
    public boolean logBlockStateRebuilds = true;
    public String _c1 = "Enables syncing of non-polymer entries as polymer ones, when PolyMc is present";
    public boolean polyMcSyncModdedEntries = true;
    public String _c2 = "Delay from last light updates to syncing it to clients, in ticks";
    public int lightUpdateTickDelay = 1;
}
