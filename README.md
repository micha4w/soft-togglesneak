# Soft-ToggleSneak

This is a variation to the classic ToggleSneak mods. Here you can not only Toggle sneak, but also tell the mod how long you need to press shift to Toggle sneaking. You can also have some conditions, that make you stop sneaking. Like being in lava/water, or trying to fly in creative. All options are togglable.

Original idea by: [Zebra's ToggleSneak](https://shotbow.net/forum/threads/zebras-togglesneak-for-minecraft-1-8-1-10-2.342553/).

## Curseforge
Available [here](https://www.curseforge.com/minecraft/mc-mods/soft-togglesneak).

## Manual update
Incase you have some free time and the mod isn't on the newest Minecraft Version you can try updating it yourself.
All you need is JDK 17 and Python3 with click and requests.

Then run these in a terminal:
```shell
git clone https://github.com/micha4w/soft-togglesneak
cd soft-togglesneak
python3 updater.py 1.20.1
./gradlew build
./gradlew runClient # to test
```

The new jars should appear in build/libs/.
