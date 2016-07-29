package clickme.nocubes;

import clickme.nocubes.ForgeEventHandler;
import clickme.nocubes.gui.GuiNoCubes;
import clickme.nocubes.renderer.SurfaceNets;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.relauncher.Side;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.config.Configuration;

@Mod(
   modid = "noCubes",
   version = "1.0"
)
public class NoCubes {
   public static final String MOD_ID = "noCubes";
   public static final String VERSION = "1.0";
   private boolean isOutdated = false;
   public static boolean isNoCubesEnabled;
   public static boolean isAutoStepEnabled;
   public static KeyBinding keyOpenSettings;
   private static Configuration noCubesConfig;
   private static List naturalBlockList = new ArrayList();
   private static List liquidBlockList = new ArrayList();
   private static List leavesBlockList = new ArrayList();

   @EventHandler
   public void preInitialization(FMLPreInitializationEvent event) {
      if(event.getSide() == Side.CLIENT) {
         keyOpenSettings = new KeyBinding("key.noCubes", 24, "key.noCubes");
         ClientRegistry.registerKeyBinding(keyOpenSettings);
         noCubesConfig = new Configuration(event.getSuggestedConfigurationFile());
         noCubesConfig.load();
         isNoCubesEnabled = noCubesConfig.get("general", "EnableNoCubes", true).getBoolean(true);
         isAutoStepEnabled = noCubesConfig.get("general", "EnableAutoStep", true).getBoolean(true);
         new SurfaceNets();
         isAutoStepEnabled = false;
         noCubesConfig.save();
         this.checkForPromotions();
         FMLCommonHandler.instance().bus().register(new ForgeEventHandler(this));
      }

   }

   public static void saveConfig() {
      noCubesConfig.load();
      noCubesConfig.get("general", "EnableNoCubes", true).set(isNoCubesEnabled);
      noCubesConfig.get("general", "EnableAutoStep", true).set(isAutoStepEnabled);
      noCubesConfig.save();
   }

   protected void openNoCubesGui() {
      Minecraft.getMinecraft().displayGuiScreen(new GuiNoCubes());
   }

   protected void notificatePlayerInChat(EntityPlayer player) {
      if(this.isOutdated) {
         player.addChatMessage(new ChatComponentTranslation("animals.outdated", new Object[0]));
         String updateUrl = "http://goo.gl/z7zh90";
         ChatComponentText url = new ChatComponentText(updateUrl);
         url.getStyle().setClickEvent(new ClickEvent(Action.OPEN_URL, updateUrl));
         url.getStyle().setUnderlined(Boolean.valueOf(true));
         player.addChatMessage(new ChatComponentTranslation("animals.download", new Object[]{url}));
      }

   }

   private void checkForPromotions() {
      (new Thread("No Cubes Version Check") {
         public void run() {
            try {
               URL e = new URL("https://dl.dropboxusercontent.com/u/71419016/nc/promotions.json");
               InputStream input = e.openStream();
               String data = new String(ByteStreams.toByteArray(input));
               input.close();
               Map json = (Map)(new Gson()).fromJson(data, Map.class);
               Map promos = (Map)json.get("promos");
               String lat = (String)promos.get("1.7.10-latest");
               DefaultArtifactVersion current = new DefaultArtifactVersion("1.0");
               if(lat != null) {
                  DefaultArtifactVersion latest = new DefaultArtifactVersion(lat);
                  if(latest.compareTo(current) > 0) {
                     NoCubes.this.isOutdated = true;
                  }
               }
            } catch (IOException var9) {
               ;
            } catch (JsonSyntaxException var10) {
               ;
            }

         }
      }).start();
   }

   public static void registerAsNatural(Block block) {
      naturalBlockList.add(block);
   }

   public static void registerAsLiquid(Block block) {
      liquidBlockList.add(block);
   }

   public static void registerAsLeaves(Block block) {
      leavesBlockList.add(block);
   }

   public static boolean isBlockNatural(Block block) {
      return naturalBlockList.contains(block) && isNoCubesEnabled;
   }

   public static boolean isBlockLiquid(Block block) {
      return liquidBlockList.contains(block);
   }

   public static boolean isBlockLeaves(Block block) {
      return leavesBlockList.contains(block);
   }

   static {
      naturalBlockList.add(Blocks.GRASS);
      naturalBlockList.add(Blocks.DIRT);
      naturalBlockList.add(Blocks.SAND);
      naturalBlockList.add(Blocks.GRAVEL);
      naturalBlockList.add(Blocks.CLAY);
      naturalBlockList.add(Blocks.FARMLAND);
      naturalBlockList.add(Blocks.MYCELIUM);
      naturalBlockList.add(Blocks.SNOW_LAYER);
      naturalBlockList.add(Blocks.STONE);
      naturalBlockList.add(Blocks.COAL_ORE);
      naturalBlockList.add(Blocks.IRON_ORE);
      naturalBlockList.add(Blocks.GOLD_ORE);
      naturalBlockList.add(Blocks.DIAMOND_ORE);
      naturalBlockList.add(Blocks.REDSTONE_ORE);
      naturalBlockList.add(Blocks.LIT_REDSTONE_ORE);
      naturalBlockList.add(Blocks.EMERALD_ORE);
      naturalBlockList.add(Blocks.BEDROCK);
      naturalBlockList.add(Blocks.SANDSTONE);
      naturalBlockList.add(Blocks.HARDENED_CLAY);
      naturalBlockList.add(Blocks.NETHERRACK);
      naturalBlockList.add(Blocks.SOUL_SAND);
      naturalBlockList.add(Blocks.QUARTZ_ORE);
      naturalBlockList.add(Blocks.END_STONE);
      liquidBlockList.add(Blocks.WATER);
      liquidBlockList.add(Blocks.FLOWING_WATER);
      liquidBlockList.add(Blocks.LAVA);
      liquidBlockList.add(Blocks.FLOWING_LAVA);
      leavesBlockList.add(Blocks.LEAVES);
      leavesBlockList.add(Blocks.LEAVES2);
   }
}
