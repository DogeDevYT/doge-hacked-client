package de.Hero.clickgui.util;

import java.awt.Color;

//Deine Imports
import com.dogedev.doge.Doge;

/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit me
 *
 *  @author HeroCode
 */
public class ColorUtil {
	
	public static Color getClickGUIColor(){
		return new Color((int) Doge.instance.settingsManager.getSettingByName("GuiRed").getValDouble(), (int)Doge.instance.settingsManager.getSettingByName("GuiGreen").getValDouble(), (int)Doge.instance.settingsManager.getSettingByName("GuiBlue").getValDouble());
	}
}
