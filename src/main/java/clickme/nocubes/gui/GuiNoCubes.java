package clickme.nocubes.gui;

import clickme.nocubes.NoCubes;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiNoCubes extends GuiScreen {
   public void initGui() {
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 - 30, 200, 20, "No Cubes general: " + (NoCubes.isNoCubesEnabled?"enabled":"disabled")));
      GuiButton button = new GuiButton(1, this.width / 2 - 100, this.height / 2 + 20, 200, 20, "Auto step: " + (NoCubes.isAutoStepEnabled?"enabled":"disabled"));
      button.enabled = false;
      this.buttonList.add(button);
   }

   protected void actionPerformed(GuiButton button) {
      if(button.enabled) {
         if(button.id == 0) {
            NoCubes.isNoCubesEnabled = !NoCubes.isNoCubesEnabled;
            button.displayString = "No Cubes general: " + (NoCubes.isNoCubesEnabled?"enabled":"disabled");
         }

         if(button.id == 1) {
            NoCubes.isAutoStepEnabled = !NoCubes.isAutoStepEnabled;
            button.displayString = "Auto step: " + (NoCubes.isAutoStepEnabled?"enabled":"disabled");
         }
      }

   }

   public void updateScreen() {
      super.updateScreen();
   }

   public void onGuiClosed() {
      NoCubes.saveConfig();
      this.mc.refreshResources();
   }

   public void drawScreen(int i, int j, float f) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, "No Cubes Settings", this.width / 2, this.height / 2 - 60, 16777215);
      this.drawCenteredString(this.fontRendererObj, "(overrides all other options)", this.width / 2, this.height / 2 - 5, 16777215);
      super.drawScreen(i, j, f);
   }
}
