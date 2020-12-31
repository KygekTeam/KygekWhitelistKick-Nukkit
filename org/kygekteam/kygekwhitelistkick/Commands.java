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
import cn.nukkit.command.PluginCommand;
import cn.nukkit.utils.TextFormat;
import org.kygekteam.kygekwhitelistkick.form.Forms;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Commands extends PluginCommand<WhitelistKick> {

    private static final String NO_PERM = TextFormat.RED + "You do not have permission to use this command";
    private static final String CONFIG_NOT_EXISTS = WhitelistKick.PREFIX + TextFormat.RED + "Configuration file is missing, please restart the server!";

    public Commands(WhitelistKick plugin) {
        super("whitelistkick", plugin);

        this.setDescription("KygekWhitelistKick commands");
        this.setUsage("/wlkick [help|off|on|set]");
        this.setAliases(new String[]{"wlkick"});
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        WhitelistKick plugin = this.getPlugin();
        if (plugin.configExists()) plugin.reloadConfig();

        if (plugin.getConfig().getString("mode").equalsIgnoreCase("form")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(WhitelistKick.PREFIX + TextFormat.RED + "Form mode can only be executed in-game!");
                return true;
            }

            if (
                !sender.hasPermission("kygekwhitelistkick.cmd." + (Forms.isEnabled() ? "off" : "on")) &&
                !sender.hasPermission("kygekwhitelistkick.cmd.set")
            ) {
                sender.sendMessage(WhitelistKick.PREFIX + TextFormat.RED + "You do not have permission to open KygekWhitelistKick form!");
                return true;
            }

            if (!plugin.configExists()) {
                sender.sendMessage(CONFIG_NOT_EXISTS);
                return true;
            }

            new Forms().mainForm((Player) sender);
            return true;
        }

        if (args.length < 1) {
            if (sender.hasPermission("kygekwhitelistkick.cmd.help")) plugin.sendHelp(sender);
            else sender.sendMessage(NO_PERM);
        } else {
            switch (args[0]) {
                case "off":
                    if (sender.hasPermission("kygekwhitelistkick.cmd.off")) {
                        if (!plugin.configExists()) {
                            sender.sendMessage(CONFIG_NOT_EXISTS);
                            return true;
                        }
                        plugin.disableWhitelistKick(sender);
                    }
                    else sender.sendMessage(NO_PERM);
                    break;

                case "on":
                    if (sender.hasPermission("kygekwhitelistkick.cmd.on")) {
                        if (!plugin.configExists()) {
                            sender.sendMessage(CONFIG_NOT_EXISTS);
                            return true;
                        }
                        plugin.enableWhitelistKick(sender);
                    }
                    else sender.sendMessage(NO_PERM);
                    break;

                case "set":
                    if (sender.hasPermission("kygekwhitelistkick.cmd.set")) {
                        if (args.length < 2) plugin.sendSubCommandUsage(sender);
                        else {
                            if (!plugin.configExists()) {
                                sender.sendMessage(CONFIG_NOT_EXISTS);
                                return true;
                            }
                            List<String> reason = new LinkedList<String>(Arrays.asList(args));
                            reason.remove(0);
                            plugin.setKickReason(String.join(" ", reason), sender);
                        }
                    }
                    else sender.sendMessage(NO_PERM);
                    break;

                default:
                    if (sender.hasPermission("kygekwhitelistkick.cmd.help")) plugin.sendHelp(sender);
                    else sender.sendMessage(NO_PERM);
            }
        }

        return true;
    }

}
