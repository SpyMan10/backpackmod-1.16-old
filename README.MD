![](https://raw.githubusercontent.com/SpyMan10/backpackmod/1.17/imgs/mod_image.png)

## Description

This mod allow you to add new backpack items using JSON to the popular [Minecraft](http://minecraft.net) game,
the mod require [Fabric Loader](https://fabricmc.net/) and [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) to work

## Download

**[CurseForge](https://www.curseforge.com/minecraft/mc-mods/backpackmod)**

## Supported Minecraft Versions

- 1.16.x
- 1.17.x
- 1.18.x
- 1.19 (Custom Configuration)

## How to configure/add new backpack item ?

Open confguration file `backpackmod.json` located into your configuration folder (Example: `~/.minecraft/config/backpackmod.json`)

This file look like that :

```json
{
    "general": {
        "enableEnderBackpack": true,
        "nbtTagName": "BackpackContent"
    },
    "backpacks": [
        {
            "name": "leather_backpack",
            "fireproof": false,
            "rarity": "common",
            "inventory": {
                "width": 9,
                "height": 2
            }
        }
    ]
}
```

### Configuration Keys

- `general`
  - `enableEnderBackpack` [`boolean`] : Set to true to enable EnderBackpack (ender chest inventory)
  - `nbtTagName` [`string`] : NBT tag name where backpack content will be written, don't change it when you're already using this mod, or your bacpacks will lose all stored items
- `backpacks` [`array`] : List of backpack items to add

  #### Array Elements
    ```json
    {
        "name": "leather_backpack",
        "fireproof": false,
        "rarity": "common",
        "inventory": {
            "width": 9,
            "height": 2
        }
    }
    ```
  - `name` [`string`] : Registry item name (`backpackmod:leather_backpack`)
  - `fireproof` [`boolean`] : Set to true to make your backpack fire proof (the backpack item will not be destroyed in fire/lava, like netherite items) 
  - `rarity` [`string`] : Item rarity, must be one of the following values : `common`, `uncommon`, `rare`, `epic`
  - `inventory`
    - `width`  [`int`] : Inventory slot matrix width
    - `height` [`int`] : Inventory slot matrix height

Resource like texture, translation, recipe must be manually added with an external resource pack / data pack

## Modpacks

`Yes you can include this mod in your own modpack`

## Minecraft Forge Version ?

`Maybe`

## Built-in Items

- Leather Backpack (18 slots)
- Copper Backpack (27 slots)
- Iron Backpack (36 slots)
- Amethyst Backpack (45 slots)
- Gold Backpack (54 slots)
- Diamond Backpack (77 slots)
- Netherite Backpack (117 slots, fireproof)
- Ender backpack (Ender chest inventory)

All built-in items can be modified or fully removed using configuration file

### Recipes

![](https://raw.githubusercontent.com/SpyMan10/backpackmod/1.19/imgs/recipes.png)