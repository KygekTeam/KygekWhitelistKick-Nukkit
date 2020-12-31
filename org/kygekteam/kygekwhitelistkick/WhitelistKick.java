/*
 * Kicks not whitelisted players when server whitelist turned on
 * Copyright (C) 2020 KygekTeam
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.kygekteam.kygekwhitelistkick;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.server.ServerCommandEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

import java.io.File;

public class WhitelistKick extends PluginBase implements Listener {

    public static final String PREFIX = TextFormat.YELLOW + "[" + TextFormat.AQUA + "KygekWhitelistKick" + TextFormat.YELLOW + "] " + TextFormat.RESET;
    private static WhitelistKick instance;

    public static WhitelistKick getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        if (!this.getConfig().getString("config-version").equals("1.3")) {
            this.getLogger().notice("Your configuration file is outdated, updating the config.yml...");
            this.getLogger().notice("The old configuration file can be found at config_old.yml");
            this.renameConfig();
            this.saveDefaultConfig();
            this.reloadConfig();
        }
        if (this.getConfig().getBoolean("reset")) {
            new File(this.getDataFolder() + "/config.yml").delete();
            this.saveDefaultConfig();
            this.getLogger().notice("Successfully reset the configuration file");
        }

        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getCommandMap().register("KygekWhitelistKick", new Commands(this));
    }

    @EventHandler
    public void onWhitelistEnabled(ServerCommandEvent event) {
        if (event.getCommand().equals("whitelist on") && this.getConfig().getBoolean("enabled")) {
            this.kickPlayers();
        }
    }

    @EventHandler
    public void onWhitelistEnabled(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equals("/whitelist on") && this.getConfig().getBoolean("enabled")) {
            this.kickPlayers();
        }
    }

    private void kickPlayers() {
        this.getConfig().reload();
        this.getServer().getOnlinePlayers().forEach((uuid, player) -> {
            if (this.isWhitelisted(player)) return;

            String reason = this.getConfig().getString("reason");
            reason = !reason.isEmpty() ? reason.replace("&", "§") : TextFormat.RED + "Whitelist have been enabled and you are not whitelisted!";
            player.kick(reason);
        });
    }

    public void enableWhitelistKick(CommandSender sender) {
        if (this.getConfig().getBoolean("enabled")) {
            sender.sendMessage(PREFIX + TextFormat.RED + "KygekWhitelistKick have been enabled");
        } else {
            this.setConfig("enabled", true);
            sender.sendMessage(PREFIX + TextFormat.GREEN + "Successfully enabled KygekWhitelistKick");
        }
    }

    public void disableWhitelistKick(CommandSender sender) {
        if (!this.getConfig().getBoolean("enabled")) {
            sender.sendMessage(PREFIX + TextFormat.RED + "KygekWhitelistKick have been disabled");
        } else {
            this.setConfig("enabled", false);
            sender.sendMessage(PREFIX + TextFormat.GREEN + "Successfully disabled KygekWhitelistKick");
        }
    }

    public void setKickReason(String reason, CommandSender sender) {
        this.setConfig("reason", reason);
        sender.sendMessage(PREFIX + TextFormat.GREEN + "Successfully changed kick reason");
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage(TextFormat.YELLOW + "===== " + TextFormat.GREEN + "KygekWhitelistKick Commands" + TextFormat.YELLOW + " =====");
        if (sender.hasPermission("kygekwhitelistkick.cmd.help"))
            sender.sendMessage(TextFormat.AQUA + "> help: " + TextFormat.GRAY + "/wlkick help");
        if (sender.hasPermission("kygekwhitelistkick.cmd.off"))
            sender.sendMessage(TextFormat.AQUA + "> off: " + TextFormat.GRAY + "/wlkick off");
        if (sender.hasPermission("kygekwhitelistkick.cmd.on"))
            sender.sendMessage(TextFormat.AQUA + "> on: " + TextFormat.GRAY + "/wlkick on");
        if (sender.hasPermission("kygekwhitelistkick.cmd.set"))
            sender.sendMessage(TextFormat.AQUA + "> set: " + TextFormat.GRAY + "/wlkick set <reason>");
    }

    public void sendSubCommandUsage(CommandSender sender) {
        sender.sendMessage(PREFIX + TextFormat.GRAY + "Usage: /wlkick set <reason>");
    }

    public void setConfig(String key, Object value) {
        this.getConfig().set(key, value);
        this.getConfig().save();
        this.getConfig().reload();
    }

    private boolean isWhitelisted(Player player) {
        if (player.isOp()) return true;

        this.getServer().getWhitelist().reload();
        return this.getServer().getWhitelist().getAll().containsValue(player.getName().toLowerCase());
    }

    public boolean configExists() {
        return new File(this.getDataFolder() + "/config.yml").exists();
    }

    private void renameConfig() {
        File oldConfig = new File(this.getDataFolder() + "/config.yml");
        File newConfig = new File(this.getDataFolder() + "/config-old.yml");

        if (newConfig.exists()) newConfig.delete();

        oldConfig.renameTo(newConfig);
    }

}
