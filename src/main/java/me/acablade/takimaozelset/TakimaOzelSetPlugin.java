package me.acablade.takimaozelset;

import me.acablade.takimaozelset.commands.TeamCommand;
import me.acablade.takimaozelset.managers.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TakimaOzelSetPlugin extends JavaPlugin {

    private TeamManager teamManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        teamManager = new TeamManager();
        getCommand("team").setExecutor(new TeamCommand(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }
}
