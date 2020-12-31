### NOTICE: This is the Nukkit version of KygekWhitelistKick. Please see [here](https://github.com/KygekTeam/KygekWhitelistKick) for PocketMine-MP version.

# KygekWhitelistKick

This plugin will kick players that are not OP or whitelisted in white-list.txt when server whitelist turned on (/whitelist on). This plugin will be useful if you often need to whitelist your server and you want all not whitelisted players to be kicked automatically. You can change the kick reason and to enable/disable kick via command or config.

# Features

- Kicks not whitelisted players automatically
- Config file can be reset
- Supports `&` as formatting codes
- Enable or disable plugin with command
- Kick reason can be changed using command
- Missing configuration file detection

# How to Install

1. Download the latest version (It is recommended to always download the latest version for the best experience, except you're having compatibility issues).
2. Place the `KygekWhitelistKick.jar` file into the `plugins` folder.
3. Restart or start your server.
4. Done!

# Commands & Permissions

| Command | Description | Permission | Default |
| --- | --- | --- | --- |
| `/whitelistkick help` | Display KygekWhitelistKick subcommands | `kygekwhitelistkick.cmd.help` | op |
| `/whitelistkick off` | Disable automatic kick on whitelist enabled | `kygekwhitelistkick.cmd.off` | op |
| `/whitelistkick on` | Enable automatic kick on whitelist enabled | `kygekwhitelistkick.cmd.on` | op |
| `/whitelistkick set` | Change whitelist kick reason | `kygekwhitelistkick.cmd.set` | op |

Use `kygekwhitelistkick.cmd` to give players permission to all subcommands. Typing `/whitelistkick` without args or args other than above will show KygekWhitelistKick help (Player needs to have `kygekwhitelistkick.cmd.help` permission).

Command alias: `/wlkick`

# Upcoming Features

- Form mode (Currently under development)
- Automatic plugin update checker on server startup

# Additional Notes

KygekWhitelistKick plugin is made by KygekTeam and licensed under GPL-3.0.

- Join our Discord server <a href="https://discord.gg/CXtqUZv">here</a> for latest updates from KygekTeam.
- If you found bugs or want to give suggestions, please visit <a href="https://github.com/KygekTeam/KygekWhitelistKick-Nukkit/issues">here</a> or join our Discord server.
- We accept all contributions! If you want to contribute please make a pull request in <a href="https://github.com/KygekTeam/KygekWhitelistKick-Nukkit/pulls">here</a>.