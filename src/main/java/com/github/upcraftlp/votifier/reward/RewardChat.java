package com.github.upcraftlp.votifier.reward;

import com.github.upcraftlp.votifier.ForgeVotifier;
import com.github.upcraftlp.votifier.api.reward.Reward;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentUtils;

public class RewardChat extends Reward {

    private final boolean broadcastMessage;
    private final String messageRaw;
    private boolean parseAsTellraw;

    public RewardChat(String raw, boolean broadcastMessage, boolean parseAsTellraw) {
        this.broadcastMessage = broadcastMessage;
        this.messageRaw = raw;
        this.parseAsTellraw = parseAsTellraw;
    }

    @Override
    public String getType() {
        return "chat";
    }

    @Override
    public void activate(MinecraftServer server, EntityPlayer player, long timestamp, String service, String address) {
        String msg = replace(messageRaw, player, service);
        if(this.parseAsTellraw) {
            try {
                ITextComponent textComponent = ITextComponent.Serializer.jsonToComponent(msg);
                if(this.broadcastMessage) {
                    for(EntityPlayerMP playerMP : server.getPlayerList().getPlayerList()) {
                        playerMP.addChatComponentMessage(TextComponentUtils.processComponent(server, textComponent, playerMP));
                    }
                }
                else player.addChatComponentMessage(TextComponentUtils.processComponent(server, textComponent, player));
            }
            catch(Exception e) {
                ForgeVotifier.getLogger().error("error parsing chat reward!", e);
            }
        }
        else {
            String[] messages = msg.split("\n");
            for(String messageString : messages) {
                ITextComponent textComponent = new TextComponentString(messageString);
                if(this.broadcastMessage) server.getPlayerList().sendChatMsg(textComponent);
                else player.addChatComponentMessage(textComponent);
            }
        }
    }
}
