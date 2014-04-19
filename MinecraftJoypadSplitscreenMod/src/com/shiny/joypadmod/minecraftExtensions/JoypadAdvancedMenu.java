package com.shiny.joypadmod.minecraftExtensions;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import com.shiny.joypadmod.ControllerSettings;
import com.shiny.joypadmod.helpers.LogHelper;
import com.shiny.joypadmod.helpers.McObfuscationHelper;

public class JoypadAdvancedMenu extends GuiScreen
{

	private int buttonWidth = 150;
	private int buttonsPerRow = 2;
	private int buttonYSpacing = 25;
	private int buttonXSpacing = 5;
	private int buttonXStart_top;
	private int buttonYStart_top;

	private int joyIndex;
	private JoypadConfigMenu parent;

	public JoypadAdvancedMenu(JoypadConfigMenu parent, int joyIndex)
	{
		super();
		this.parent = parent;
		this.joyIndex = joyIndex;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		buttonXStart_top = (this.width - buttonWidth * buttonsPerRow - buttonXSpacing * buttonsPerRow) / 2;
		buttonYStart_top = 50;

		int buttonNum = 0;

		addButton(buttonNum++, 300, "controlMenu.invert", true, ControllerSettings.getInvertYAxis());
		addButton(buttonNum++, 310, "controlMenu.calibrate", false, false);
		addButton(buttonNum++, 320, "-Global-.SharedProfile", true, ControllerSettings.isUsingSharedProfiles());

		buttonList.add(new GuiButton(500, width / 2 - parent.bottomButtonWidth / 2, height - 20,
				parent.bottomButtonWidth, 20, parent.sGet("gui.done")));
	}

	@Override
	protected void actionPerformed(GuiButton guiButton)
	{
		LogHelper.Info("Action performed on " + guiButton.id);
		switch (guiButton.id)
		{
		case 300: // invert
			ControllerSettings.setInvertYAxis(!ControllerSettings.getInvertYAxis());
			toggleOnOffButton(ControllerSettings.getInvertYAxis(), guiButton);
			break;
		case 310: // calibrate
			mc.displayGuiScreen(new JoypadCalibrationMenu(this, joyIndex));
			break;
		case 320: // shared profiles
			ControllerSettings.setSharedProfile(!ControllerSettings.isUsingSharedProfiles());
			toggleOnOffButton(ControllerSettings.isUsingSharedProfiles(), guiButton);
			break;
		case 500: // Done
			mc.displayGuiScreen(this.parent);
			break;
		}

	}

	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		drawDefaultBackground();

		int labelYStart = 5;

		String titleText = String.format("Joypad Mod %s", parent.sGet("controlMenu.advanced"));
		this.drawCenteredString(parent.getFontRenderer(), titleText, width / 2, labelYStart, -1);

		super.drawScreen(par1, par2, par3);
	}

	@SuppressWarnings("unchecked")
	private void addButton(int buttonNum, int id, String code, boolean isToggle, boolean toggleValue)
	{
		int buttonBase = buttonNum % buttonsPerRow;
		String buttonString = isToggle ? createToggleString(code, toggleValue) : McObfuscationHelper.lookupString(code);
		buttonList.add(new GuiButton(id, buttonXStart_top + buttonWidth * buttonBase + buttonXSpacing * buttonBase,
				buttonYStart_top + (buttonNum / buttonsPerRow) * buttonYSpacing, buttonWidth, 20, buttonString));
	}

	private String createToggleString(String code, boolean on)
	{
		String toggleString = on ? "options.on" : "options.off";
		return String.format("%s: %s", McObfuscationHelper.lookupString(code),
				McObfuscationHelper.lookupString(toggleString));
	}

	private void toggleOnOffButton(boolean b, GuiButton button)
	{
		String s1 = b ? McObfuscationHelper.lookupString("options.off")
				: McObfuscationHelper.lookupString("options.on");
		String s2 = b ? McObfuscationHelper.lookupString("options.on")
				: McObfuscationHelper.lookupString("options.off");
		button.displayString = button.displayString.replace(s1, s2);
	}

}
