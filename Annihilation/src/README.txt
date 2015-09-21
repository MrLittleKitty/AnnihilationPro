Annihilation Pro Readme (Currently in-progress)

Files:

"Annihilation.jar" (NOT OPTIONAL)
--This goes in the "plugins" folder
--This is the main plugin required to run the Annihilation game mode

"AnnihilationStarterKits.jar" (OPTIONAL, but RECOMMENDED)
--This goes in the "plugins/Annihilation/Kits" folder
--This provides the starting kits for the Annihilation game mode
--Any addition kit packs should also be placed in the "plugins/Annihilation/Kits" folder
--Additional kit packs:
----StarterKitsPlus (http://www.spigotmc.org/resources/annihilation-pro-starterkitsplus.7199/)

"AnnihilationVault.jar" (OPTIONAL)
--This goes in the "plugins" folder
--This requires that you have the plugin "Vault" and an economy plugin on your server

"AnnihilationXP.jar" (OPTIONAL)
--This goes in the "plugins" folder
--This requires that you have a MySQL server
--You must configure the MySQL server in the XPConfig file

"AnnihilationWorldRestorer.jar" (OPTIONAL, but RECOMMENDED)
--This goes in the "plugins" folder
--This plugin restores the Annihilation worlds on the start of your server
--This plugins works as follows:
----1. This plugin looks through all of the worlds in the "plugins/Annihilation/Worlds" folder
----2. If that world is already in the "plugins/Annihilation/WorldBackups" folder, the world is restored from its backup
----3. If that world is not already in the "plugins/Annihilation/WorldBackups" folder, it is backed up to the backups folder
----Example: You have a world "Cat" in the "plugins/Annihilation/Worlds" folder. When the plugin starts, the world "Cat"
             is copied to the "plugins/Annihilation/WorldBackups" folder. Now, every time you start your server,
             the world "Cat" is copied from the backups folder and put into the worlds folder.


All of these have configuration files.
The configuration files are all found in the "plugins/Annihilation" folder.

