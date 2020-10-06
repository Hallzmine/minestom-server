package fr.themode.demo;

import fr.themode.demo.blocks.StoneBlock;
//import fr.themode.demo.generator.ChunkGeneratorDemo;
//import fr.themode.demo.generator.NoiseTestGenerator;
import fr.themode.demo.items.SwordConstructor;
import fr.themode.demo.items.TestItem;
import net.minestom.server.MinecraftServer;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.benchmark.BenchmarkManager;
import net.minestom.server.benchmark.ThreadResult;
import net.minestom.server.chat.ChatColor;
import net.minestom.server.chat.ColoredText;
import net.minestom.server.data.Data;
import net.minestom.server.entity.*;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.entity.type.vehicle.EntityBoat;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.ItemUpdateStateEvent;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.WorldBorder;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.CustomBlock;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.MapMeta;
import net.minestom.server.network.ConnectionManager;
import net.minestom.server.ping.ResponseDataConsumer;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.storage.StorageLocation;
import net.minestom.server.storage.StorageOptions;
import net.minestom.server.utils.BlockPosition;
import net.minestom.server.utils.MathUtils;
import net.minestom.server.utils.Position;
import net.minestom.server.utils.Vector;
import net.minestom.server.utils.time.TimeUnit;
import net.minestom.server.world.DimensionType;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import static net.minestom.server.instance.block.Block.TNT;

public class PlayerInit {
    private static volatile InstanceContainer instanceContainer;

    private static volatile Inventory inventory;

    static {
        StorageLocation storageLocation = MinecraftServer.getStorageManager().getLocation("instance_data", new StorageOptions().setCompression(true));
        //ChunkGeneratorDemo chunkGeneratorDemo = new ChunkGeneratorDemo();
        //NoiseTestGenerator noiseTestGenerator = new NoiseTestGenerator();
        instanceContainer = MinecraftServer.getInstanceManager().createInstanceContainer(DimensionType.OVERWORLD, storageLocation);
        instanceContainer.enableAutoChunkLoad(true);
        //instanceContainer.setChunkDecider((x,y) -> (pos) -> pos.getY()>40?(short)0:(short)1);
        //instanceContainer.setChunkGenerator(noiseTestGenerator);

        // Load some chunks beforehand
        final int loopStart = -10;
        final int loopEnd = 10;
        for (int x = loopStart; x < loopEnd; x++)
            for (int z = loopStart; z < loopEnd; z++) {
                instanceContainer.loadChunk(x, z);
            }

        inventory = new Inventory(InventoryType.CHEST_1_ROW, "Test inventory");
        inventory.addInventoryCondition((p, slot, clickType, inventoryConditionResult) -> {
            p.sendMessage("click type inventory: " + clickType);
            System.out.println("slot inv: " + slot);
            inventoryConditionResult.setCancel(false);
        });
        inventory.setItemStack(0, new ItemStack(Material.DIAMOND, (byte) 34));
    }

    public static void init() {
        ConnectionManager connectionManager = MinecraftServer.getConnectionManager();
        BenchmarkManager benchmarkManager = MinecraftServer.getBenchmarkManager();

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            long ramUsage = benchmarkManager.getUsedMemory();
            ramUsage /= 1e6; // bytes to MB

            String benchmarkMessage = "";
            for (Map.Entry<String, ThreadResult> resultEntry : benchmarkManager.getResultMap().entrySet()) {
                String name = resultEntry.getKey();
                ThreadResult result = resultEntry.getValue();
                benchmarkMessage += ChatColor.GRAY + name;
                benchmarkMessage += ": ";
                benchmarkMessage += ChatColor.YELLOW.toString() + MathUtils.round(result.getCpuPercentage(), 2) + "% CPU ";
                benchmarkMessage += ChatColor.RED.toString() + MathUtils.round(result.getUserPercentage(), 2) + "% USER ";
                benchmarkMessage += ChatColor.PINK.toString() + MathUtils.round(result.getBlockedPercentage(), 2) + "% BLOCKED ";
                benchmarkMessage += ChatColor.BRIGHT_GREEN.toString() + MathUtils.round(result.getWaitedPercentage(), 2) + "% WAITED ";
                benchmarkMessage += "\n";
            }

            for (Player player : connectionManager.getOnlinePlayers()) {
                ColoredText header = ColoredText.of("RAM USAGE: " + ramUsage + " MB");
                ColoredText footer = ColoredText.of(benchmarkMessage);
                player.sendHeaderFooter(header, footer);
            }
        }).repeat(10, TimeUnit.TICK).schedule();

        connectionManager.onPacketReceive((player, packetController, packet) -> {
            // Listen to all received packet
            //System.out.println("PACKET: "+packet.getClass().getSimpleName());
            packetController.setCancel(false);
        });

        connectionManager.addPlayerInitialization(player -> {

            player.addEventCallback(EntityAttackEvent.class, event -> {
                final Entity entity = event.getTarget();
                if (entity instanceof EntityCreature) {
                    EntityCreature creature = (EntityCreature) entity;
                    creature.damage(DamageType.fromPlayer(player), -1);
                    Vector velocity = player.getPosition().clone().getDirection().multiply(6);
                    velocity.setY(4f);
                    entity.setVelocity(velocity);
                } else if (entity instanceof Player) {
                    Player target = (Player) entity;
                    Vector velocity = player.getPosition().clone().getDirection().multiply(4);
                    velocity.setY(3.5f);
                    target.setVelocity(velocity);
                    target.damage(DamageType.fromPlayer(player), 5);
                }
            });

            player.addEventCallback(PlayerBlockPlaceEvent.class, event -> {
                if (event.getHand() != Player.Hand.MAIN)
                    return;

                final Block block = Block.fromStateId(event.getBlockStateId());

                if (block == Block.STONE) {
                    event.setCustomBlock((short) 2); // custom stone block
                    System.out.println("custom stone");
                }
                if (block == Block.TORCH) {
                    event.setCustomBlock((short) 3); // custom torch block
                }

                /*for (Player p : player.getInstance().getPlayers()) {
                    if (p != player)
                        p.teleport(player.getPosition());
                }*/

                /*for (int i = 0; i < 100; i++) {
                    ChickenCreature chickenCreature = new ChickenCreature(player.getPosition());
                    chickenCreature.setInstance(player.getInstance());
                }*/

                /*EntityZombie zombie = new EntityZombie(player.getPosition());
                zombie.setAttribute(Attribute.MOVEMENT_SPEED, 0.25f);
                zombie.setInstance(player.getInstance());*/

                /*FakePlayer.initPlayer(UUID.randomUUID(), "test", fakePlayer -> {
                    //fakePlayer.setInstance(player.getInstance());
                    fakePlayer.teleport(player.getPosition());
                    fakePlayer.setSkin(PlayerSkin.fromUsername("TheMode911"));

                    fakePlayer.addEventCallback(EntityDeathEvent.class, e -> {
                        fakePlayer.getController().respawn();
                    });

                    fakePlayer.setArrowCount(25);
                    FakePlayerController controller = fakePlayer.getController();
                    controller.sendChatMessage("I am a bot!");
                });*/
                //Hologram hologram = new Hologram(player.getInstance(), player.getPosition(), "Hey guy");

            });

            player.addEventCallback(PlayerBlockInteractEvent.class, event -> {
                if (event.getHand() != Player.Hand.MAIN)
                    return;

                final Instance instance = player.getInstance();
                final BlockPosition blockPosition = event.getBlockPosition();

                final CustomBlock customBlock = instance.getCustomBlock(blockPosition);
                if (customBlock instanceof StoneBlock) {
                    final Data data = instance.getBlockData(blockPosition);
                    if (data != null) {
                        player.sendMessage("test: " + data.get("test"));
                    }
                }

                final short blockStateId = player.getInstance().getBlockStateId(event.getBlockPosition());
                final Block block = Block.fromStateId(blockStateId);
                player.sendMessage("You clicked at the block " + block);
                if(block == TNT) {
                    player.setFlying(true);
                }
            });

            player.addEventCallback(PickupItemEvent.class, event -> {
                // Cancel event if player does not have enough inventory space
                event.setCancelled(!player.getInventory().addItemStack(event.getItemStack()));
            });

            player.addEventCallback(ItemDropEvent.class, event -> {
                ItemStack droppedItem = event.getItemStack();

                Position position = player.getPosition().clone().add(0, 1.5f, 0);
                ItemEntity itemEntity = new ItemEntity(droppedItem, position);
                itemEntity.setPickupDelay(500, TimeUnit.MILLISECOND);
                itemEntity.setInstance(player.getInstance());
                Vector velocity = player.getPosition().clone().getDirection().multiply(6);
                itemEntity.setVelocity(velocity);
            });

            player.addEventCallback(PlayerDisconnectEvent.class, event -> {
                System.out.println("DISCONNECTION " + player.getUsername());
            });

            player.addEventCallback(PlayerLoginEvent.class, event -> {
                //final String url = "https://download.mc-packs.net/pack/a83a04f5d78061e0890e13519fea925550461c74.zip";
                //final String hash = "a83a04f5d78061e0890e13519fea925550461c74";
                //player.setResourcePack(new ResourcePack(url, hash));

                event.setSpawningInstance(instanceContainer);
                player.setEnableRespawnScreen(false);

                player.setPermissionLevel(4);

                player.getInventory().addInventoryCondition((p, slot, clickType, inventoryConditionResult) -> {
                    player.sendMessage("CLICK PLAYER INVENTORY");
                    System.out.println("slot player: " + slot);
                });

                /*Sidebar scoreboard = new Sidebar("Scoreboard Title");
                for (int i = 0; i < 15; i++) {
                    scoreboard.createLine(new Sidebar.ScoreboardLine("id" + i, "Hey guys " + i, i));
                }
                scoreboard.addViewer(player);
                scoreboard.updateLineContent("id3", "I HAVE BEEN UPDATED");

                scoreboard.setTitle("test");*/

                {
                    /*AdvancementManager advancementManager = MinecraftServer.getAdvancementManager();
                    AdvancementRoot root = new AdvancementRoot(ColoredText.of("title"), ColoredText.of(ChatColor.BLUE + "description"),
                            Material.APPLE, FrameType.TASK, 0, 0,
                            "minecraft:textures/block/red_wool.png");
                    root.setAchieved(true);
                    AdvancementTab tab = advancementManager.createTab("root", root);
                    Advancement advancement = new Advancement(ColoredText.of("adv"), ColoredText.of("desc"),
                            Material.WOODEN_AXE, FrameType.CHALLENGE, 1, 0)
                            .showToast(true).setHidden(false);
                    tab.createAdvancement("second", advancement, root);

                    tab.addViewer(player);

                    root.setTitle(ColoredText.of("test ttlechange"));

                    Advancement advancement2 = new Advancement(ColoredText.of(ChatColor.BLUE + "Title"),
                            ColoredText.of("description of the advancement"),
                            Material.GOLD_BLOCK, FrameType.CHALLENGE, 3, 0)
                            .showToast(true).setHidden(false);
                    tab.createAdvancement("second2", advancement2, root);*/
                }
            });

            player.addEventCallback(PlayerSpawnEvent.class, event -> {
                player.setGameMode(GameMode.CREATIVE);
                player.teleport(new Position(0, 73f, 0));
                BlockPosition blockpos = new Position(0, 72f, 0).toBlockPosition();


                //player.setHeldItemSlot((byte) 5);

                //player.setGlowing(true);
                player.getInventory().addItemStack(new ItemStack(Material.STONE, (byte) 127));
                /*for (int i = 0; i < 9; i++) {
                    player.getInventory().setItemStack(i, new ItemStack(Material.STONE, (byte) 127));
                }*/

                {
                    Sidebar sidebar = new Sidebar("Epic Server");
                    sidebar.createLine(new Sidebar.ScoreboardLine("id3", ColoredText.of("========"), 3));
                    sidebar.createLine(new Sidebar.ScoreboardLine("id2", ColoredText.of(" Hello!"), 2));
                    sidebar.createLine(new Sidebar.ScoreboardLine("id1", ColoredText.of("========"), 1));
                    sidebar.addViewer(player);
                }

                {
                    ItemStack map = new ItemStack(Material.FILLED_MAP, (byte) 1);
                    MapMeta mapMeta = (MapMeta) map.getItemMeta();
                    mapMeta.setMapId(1);
                    //player.getInventory().setItemStack(0, map);

                }


                ItemStack item = new ItemStack(Material.STONE_SWORD, (byte) 1);
                item.setDisplayName(ColoredText.of("Item name"));
                item.setDamage(5);
                //item.getLore().add(ColoredText.of(ChatColor.RED + "a lore line " + ChatColor.BLACK + " BLACK"));
                //item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                //item.setEnchantment(Enchantment.SHARPNESS, (short) 50);
                player.getInventory().addItemStack(item);
                player.getInventory().addItemStack(new TestItem());

                //just testing some items here :D
                ItemStack Helmet = new ItemStack(Material.DIAMOND_HELMET, (byte) 5);
                ArrayList<ColoredText> lore = new ArrayList<ColoredText>();
                lore.add(ColoredText.of("Hello!"));
                Helmet.setLore(lore);
                player.setHelmet(Helmet);

                inventory.addItemStack(item.clone());
                //player.openInventory(inventory);

                //player.getInventory().addItemStack(new ItemStack(Material.STONE, (byte) 100));

                {
                    /*EntityItemFrame entityItemFrame = new EntityItemFrame(new Position(-5, 36, 9, 0, 180), EntityItemFrame.ItemFrameOrientation.DOWN);
                    entityItemFrame.setNoGravity(true);
                    entityItemFrame.setInstance(player.getInstance());
                    entityItemFrame.setItemStack(item);*/
                }

                Instance instance = player.getInstance();
                WorldBorder worldBorder = instance.getWorldBorder();
                //worldBorder.setDiameter(30);

                //player.getInventory().addItemStack(new ItemStack(Material.DIAMOND_CHESTPLATE, (byte) 1));

            /*TeamManager teamManager = Main.getTeamManager();
            Team team = teamManager.createTeam(getUsername());
            team.setTeamDisplayName("display");
            team.setPrefix("[Test] ");
            team.setTeamColor(ChatColor.RED);
            setTeam(team);

            setAttribute(Attribute.MAX_HEALTH, 10);
            heal();

            BelowNameScoreboard belowNameScoreboard = new BelowNameScoreboard();
            setBelowNameScoreboard(belowNameScoreboard);
            belowNameScoreboard.updateScore(this, 50);*/

                //player.sendLegacyMessage("&aIm &bHere", '&');
                //player.sendMessage(ColoredText.of("{#ff55ff}" + ChatColor.RESET + "test"));

            });

            player.addEventCallback(PlayerRespawnEvent.class, event -> {
                event.setRespawnPosition(new Position(0f, 75f, 0f));
            });

            player.addEventCallback(PlayerCommandEvent.class, event -> {
                System.out.println("COMMAND EVENT");
            });



            player.addEventCallback(PlayerUseItemOnBlockEvent.class, useEvent -> {
                player.sendMessage("Main item: " + player.getInventory().getItemInMainHand().getMaterial());
                player.sendMessage("Using item on block: " + useEvent.getItemStack().getMaterial() + " at " + useEvent.getPosition() + " on face " + useEvent.getBlockFace());

                //Boat placing system

                //Checks if the item is a boat
                if(useEvent.getItemStack().getMaterial() == Material.OAK_BOAT) {
                    //Creates a new boat entity at the block the player clicked on
                    EntityBoat entityBoat = new EntityBoat(useEvent.getPosition().toPosition());
                    //Rotates the boat to the rotation of the player
                    entityBoat.setView(player.getPosition().getYaw(), player.getPosition().getPitch());
                    entityBoat.setInstance(player.getInstance());
                    //Places the player inside of the boat
                    entityBoat.addPassenger(player);
                    //Removes the item from the player's hand (I should probably move this to the beginning of this if statement)
                    player.getItemInMainHand().setAmount((byte) (player.getItemInMainHand().getAmount()-1));
                }
            });

            player.addEventCallback(PlayerUseItemEvent.class, useEvent -> {
                player.sendMessage("Using item in air: " + useEvent.getItemStack().getMaterial());
            });



            player.addEventCallback(ItemUpdateStateEvent.class, event -> {
                System.out.println("ITEM UPDATE STATE");
            });

            player.addEventCallback(PlayerTickEvent.class, event -> {
                //Displays the ActionBar every tick
                player.sendActionBarMessage(ColoredText.of(ChatColor.fromRGB(0,100,250) + Float.toString(player.getAttributeValue(Attribute.ARMOR)) +"/20 " + ChatColor.fromRGB(154,205,50) + "   XP: " +player.getExp()+ "/" + "0"));
                //Places a stone block every tick because void world need something to stand on
                instanceContainer.setBlock(0, 50, 0, Block.STONE);


                //Code below handles armor system!

                //Resets the player's attribute so it doesn't increment infinitely
                player.setAttribute(Attribute.ARMOR, 0);
                //Gets the armor value of main hand items
                double main_hand_armor = player.getItemInMainHand().getAttribute("Armor").getValue();
                //This mess of comments below is for adding armor into the armor attributes for the future
                //double helmet_armor = player.getHelmet().getAttribute("Armor").getValue();
                //double chestplate_armor = player.getChestplate().getAttribute("Armor").getValue();
                //double leggings_armor = player.getLeggings().getAttribute("Armor").getValue();
                //double boots_armor = player.getBoots().getAttribute("Armor").getValue();
                double gear_armor = main_hand_armor; //+ helmet_armor + chestplate_armor + leggings_armor + boots_armor;
                //Sets armor
                player.setAttribute(Attribute.ARMOR,  player.getAttributeValue(Attribute.ARMOR) + Float.valueOf((float) gear_armor));

            });

            player.addEventCallback(PlayerPreEatEvent.class, event -> {
                ItemStack itemStack = event.getFoodItem();
                Material material = itemStack.getMaterial();
                event.setEatingTime(material == Material.PORKCHOP ? 100 : 1000);
            });

            player.addEventCallback(PlayerEatEvent.class, event -> {
                System.out.println("PLAYER EAT EVENT");
            });

            player.addEventCallback(PlayerChunkUnloadEvent.class, event -> {
                Instance instance = player.getInstance();

                Chunk chunk = instance.getChunk(event.getChunkX(), event.getChunkZ());

                if (chunk == null)
                    return;

                // Unload the chunk (save memory) if it has no remaining viewer
                if (chunk.getViewers().isEmpty()) {
                    //player.getInstance().unloadChunk(chunk);
                }
            });

        });
    }

    public static ResponseDataConsumer getResponseDataConsumer() {
        return (playerConnection, responseData) -> {
            responseData.setMaxPlayer(0);
            responseData.setOnline(MinecraftServer.getConnectionManager().getOnlinePlayers().size());
            responseData.addPlayer("A name", UUID.randomUUID());
            responseData.addPlayer("Could be some message", UUID.randomUUID());
            responseData.setDescription("IP test: " + playerConnection.getRemoteAddress());
        };
    }

}
