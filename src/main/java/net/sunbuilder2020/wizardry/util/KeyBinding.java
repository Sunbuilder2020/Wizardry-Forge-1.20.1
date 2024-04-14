package net.sunbuilder2020.wizardry.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_WIZARDRY = "key.category.wizardry";
    public static final String KEY_OPEN_SPELL_GUI = "key.wizardry.open_spell_gui";
    public static final String KEY_SWITCH_SPELL_LEFT = "key.wizardry.switch_spell_left";
    public static final String KEY_SWITCH_SPELL_RIGHT = "key.wizardry.switch_spell_right";


    public static final KeyMapping KEY_MAPPING_OPEN_SPELL_GUI = new KeyMapping(KEY_OPEN_SPELL_GUI, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, KEY_CATEGORY_WIZARDRY);

    public static final KeyMapping KEY_MAPPING_SWITCH_SPELL_LEFT = new KeyMapping(KEY_SWITCH_SPELL_LEFT, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT, KEY_CATEGORY_WIZARDRY);

    public static final KeyMapping KEY_MAPPING_SWITCH_SPELL_RIGHT = new KeyMapping(KEY_SWITCH_SPELL_RIGHT, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT, KEY_CATEGORY_WIZARDRY);
}