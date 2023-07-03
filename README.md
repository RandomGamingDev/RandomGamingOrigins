# RandomGamingOrigins
A basic Origins plugin that's heavily inspired and improves on the original origins skript of ZevOrigins.minehut.gg (Now rebranded as Minting.minehut.gg)

This plugin has been tested with:
Spigot 1.18 (Should work with multiple versions older and newer than this)
Spigot 1.19.3

If the plugin isn't working on your version trying changing the api-version in plugin.yml!

This plugin relies purely on the spigot API and thus should easily work on multiple Minecraft versions

Wanna try out this plugin for yourself? Try it out at Minting.minehut.gg!

To see all the commands check them out at https://github.com/RandomGamingDev/RandomGamingOrigins/blob/main/src/main/resources/plugin.yml!

Note: The goal of this plugin isn't to get as close to the Origins mod as possible, but to provide and nice, easy and balanced origins environment!

## Want to add new origins?
Here's how to do it:

1. Create a Github account
2. Install git
3. Create a fork of the repository
4. Clone the fork of the repository
5. Install an editor for compilation and to open up the project. I recommend using IntelliJ IDEA.
6. Create an origin class in src/main/java/me/randomgamingdev/randomgamingorigins/core/origins based the `NullOrigin` class (that's the base class all origins rely on) and the other origins (you can copy and paste little parts to get an idea of how to use the application)
7. Add the origin with it's corresponding name and an instance of the class you previously created under the `Origins` enum here: src/main/java/me/randomgamingdev/randomgamingorigins/core/types/Origin.java
8. Push to your fork and then send a pull request and if it's good enough and most importantly, balanced I'll add it!
