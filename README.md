# Better Burning Reborn

An unofficial port of [Better Burning](https://github.com/Darkhax-Minecraft/Better-Burning) by Darkhax, which stops at Minecraft 1.20.4, to 1.21.1, 1.21.11, 26.2 and 26.3 on NeoForge and Fabric.

Small changes to how fire works, each with its own switch:

- Fire resistance puts you out instead of leaving you visibly burning.
- Mobs killed by fire damage drop cooked food even if the flames never caught on them.
- A burning mob hitting with an empty hand can set its target alight (30%); zombies already do this in vanilla and are left alone.
- Flint and steel used as a weapon sets what it hits on fire.
- Skeletons that are on fire may shoot burning arrows (70%).
- Punching out fire burns you for a second (two for soul fire); punching out fire can be turned off completely.
- A water bottle used on fire puts it out and gives the empty bottle back.

Config: `config/betterburning.properties`. The original's option to hide the fire overlay for fire-immune players is not ported; fire resistance putting the flames out covers the usual case.

Tests: every feature has a GameTest, and a second one that runs the same setup with that feature kept on vanilla behaviour, on all four Minecraft versions for both loaders. A client smoke test starts the game with the mod.

LGPL-2.1, like the original.
