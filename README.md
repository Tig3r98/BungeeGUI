
# 📑 BungeeGUI 
A Proxy wide GUI for Bungeecord  
This project is a fork of [VelocityGUI](https://github.com/james090500/VelocityGUI) by james090500.  
**Requires [Protocolize](https://github.com/Exceptionflug/protocolize)**

![bStats](https://bstats.org/signatures/bungeecord/BungeeGUI.svg)

## Permissions
| Permission | Purpose |  
|--|--|  
| `bgui.admin` | Needed for all `/bgui` commands |

## Commands
| Command | Response |  
|--|--|  
| `/bgui` | Info command |  
| `/bgui panel` | Lists all panels |  
| `/bgui panel <name>` | Loads up a specific panel |  
| `/bgui reload` | Reloads the panels and config

## Config
The config uses MiniMessage to define colors
```toml  
#The Name of the panel  
name = "example"

#The permission needed to open the panel (Can be anything)  
perm = "default"

#The rows in the GUI (Max 9)  
rows = 3

#The GUI Title  
title = "<light_purple>BungeeGUI"

#Whats empty slots should be filled with (AIR for empty)  
empty = "GREEN_STAINED_GLASS_PANE"

#Sound when opening the GUI  
sound = "ENTITY_ARROW_HIT_PLAYER"

#Sound when clicking empty slots (numbers are volume and pitch)
emptysound = "ENTITY_PLAYER_LEVELUP:1:1"

#The commands to open the gui (/rules /version etc)  
commands = [
    "rules", "version", "plugins", "help", "pl"
]

#The Items in the gui  
[items]

#This is in the 13th slot  
[items.13]
#The item material  
material = "OAK_SIGN"
#The item amount  
stack = 1
#The item name  
name = "<light_purple>BungeeGUI"
#The items lore  
lore = [
    "<yellow>A Bungee Side GUI",
    "<yellow>For all your servers"
]
#Is the item enchanted?  
enchanted = true
#Commands to run  
commands = [
    "psudo= I love BungeeGUI"
]  
```  

## Item Commands
| Command | Example | Response |  
|--|--|--|  
| `open` | `open= rules` | Open another panel |  
| `close` | `close` | Closes the current panel |    
| `psudo` | `psudo= /command` | Runs a command as the player on the proxy |  
| `sound` | `sound= MYSOUND:volume:pitch` | Plays a sound to the player | 
| `server` | `server= lobby`| Connects the player to a server

## Placeholders
| Placeholder | Value |  
|--|--|
| `%username%` | Get players username |  
| `%displayname%` | Get players displayname |  
| `%server_name%` | Get players server name |  
| `%server_players_servername%` | Get how many players are online on the specified servername |
| `%proxy_players%` | Get how many players are online in the network |  
| `%luckperms_meta_{meta}%` | Get luckperms meta value eg `luckperms_meta_home` |

Open an issue if you'd like others placeholders to be added.