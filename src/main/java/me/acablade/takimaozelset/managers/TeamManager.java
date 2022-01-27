package me.acablade.takimaozelset.managers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TeamManager {

    private final Set<UUID> teamOne;
    private final Set<UUID> teamTwo;

    public TeamManager(){
        teamOne = new HashSet<>();
        teamTwo = new HashSet<>();
    }

    public Set<UUID> getTeamTwo() {
        return teamTwo;
    }

    public Set<UUID> getTeamOne() {
        return teamOne;
    }
}
