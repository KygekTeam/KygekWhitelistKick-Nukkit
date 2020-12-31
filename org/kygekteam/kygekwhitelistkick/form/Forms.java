/*
 * Kicks not whitelisted players when server whitelist turned on
 * Copyright (C) 2020 KygekTeam
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

// COMING SOON, UNDER DEVELOPMENT

package org.kygekteam.kygekwhitelistkick.form;

import cn.nukkit.Player;
import org.kygekteam.kygekwhitelistkick.WhitelistKick;
import ru.nukkitx.forms.elements.CustomForm;
import ru.nukkitx.forms.elements.SimpleForm;

import java.lang.reflect.InvocationTargetException;

public class Forms {

    public static boolean isEnabled() {
        return WhitelistKick.getInstance().getConfig().getBoolean("enabled");
    }

    public void mainForm(Player player) {
        SimpleForm form = new SimpleForm("KygekWhitelistKick");

        form.setContent("Select options:");
        if (player.hasPermission("kygekwhitelistkick.cmd." + (isEnabled() ? "off" : "on")))
            form.addButton((isEnabled() ? "Disable" : "Enable") + " KygekWhitelistKick");
        if (player.hasPermission("kygekwhitelistkick.cmd.set"))
            form.addButton("Set kick reason");
        form.addButton("Exit");

        form.send(player, (targetPlayer, targetForm, data) -> {
            if (data == -1) return;
            switch (data) {
                case 0:
                    enableDisableForm(targetPlayer);
                    break;
                case 1:
                    setReasonForm(targetPlayer);
            }
        });
    }

    private void enableDisableForm(Player player) {
        SimpleForm form = new SimpleForm((isEnabled() ? "Disable" : "Enable") + " KygekWhitelistKick");

        form.setContent("Are you sure you want to " + (isEnabled() ? "disable" : "enable") + " KygekWhitelistKick?");
        form.addButton("Yes").addButton("Back");

        form.send(player, (targetPlayer, targetForm, data) -> {
            if (data == 0) {
                WhitelistKick.getInstance().setConfig("enabled", !isEnabled());
                resultForm(targetPlayer, "Success", "Successfully " + (isEnabled() ? "enabled" : "disabled") + " KygekWhitelistKick");
                return;
            }
            mainForm(targetPlayer);
        });
    }

    private void setReasonForm(Player player) {
        CustomForm form = new CustomForm("Set kick reason");

        form.addInput("Enter the kick reason you want to change:", "Enter here");

        form.send(player, (targetPlayer, targetForm, data) -> {
            if (data.isEmpty()) {
                mainForm(targetPlayer);
                return;
            }
            if (((String) data.get(0)).isEmpty()) {
                resultForm(targetPlayer, "Error", "Please enter kick reason", true, "setReasonForm");
                return;
            }
            WhitelistKick.getInstance().setConfig("reason", data.get(0));
            resultForm(targetPlayer, "Success", "Successfully changed kick reason");
        });
    }

    private void resultForm(Player player, String title, String content, boolean back, String returnedMtd) {
        SimpleForm form = new SimpleForm(title);

        form.setContent(content);
        form.addButton(back ? "Back" : "Ok");

        form.send(player, (targetPlayer, targetForm, data) -> {
            if (back) {
                try {
                    Forms.class.getMethod(returnedMtd, Player.class).invoke(new Forms(), targetPlayer);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void resultForm(Player player, String title, String content) {
        resultForm(player, title, content, false, "");
    }

}
