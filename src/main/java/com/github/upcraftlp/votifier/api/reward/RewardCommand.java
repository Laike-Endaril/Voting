package com.github.upcraftlp.votifier.api.reward;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class RewardCommand extends Reward {

    private final String command;

    public RewardCommand(String command) {
        this.command = command;
    }

    @Override
    public String getType() {
        return "command";
    }

    @Override
    public void activate(MinecraftServer server, EntityPlayer player, long timestamp, String service, String address) {
        server.commandManager.executeCommand(server, replace(this.command, player, service));
    }
}
