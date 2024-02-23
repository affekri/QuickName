# QuickName  

A plugin for Minecraft servers running PaperMC.  
Allows players to change the color of player names and add prefixes to names.

## Commands

#### Changecolor  

This command is used to change the color of a players name.  
To use this command type `/changecolor` followed by the player name and then the color you want the name to be.  
To be able to use this command the player must have the permission `quickname.changecolor`.

#### Prefix  

This command is used to manage prefixes.  
Type `/prefix` followed by one of the for subcommands `create:remove:set:list`.  
The `create` subcommand is used for creating prefixes. The command takes four arguments. A prefixID, prefixText,  
color and a boolean to determine if the prefix should be enclosed in square brackets.  
The `remove` subcommand removes prefixes. To use it write it and a prefix you want to remove.  
The `set` subcommand sets a prefix to a player. It takes one or two arguments. The first is a player name and  
the optional second is what prefix that player should have. If there is no second argument the player will have no prefix.  
The `list` subcommand shows you a list of all prefixes available.  
To be able to use the command the player needs the permission `quickname.prefix`.  
Note that player does not need the permission to have a prefix before their name.