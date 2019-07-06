package com.dogedev.doge.module;

import com.dogedev.doge.module.modules.combat.*;
import com.dogedev.doge.module.modules.misc.ChestStealer;
import com.dogedev.doge.module.modules.misc.MurdererFinder;
import com.dogedev.doge.module.modules.misc.NoSwing;
import com.dogedev.doge.module.modules.misc.Panic;
import com.dogedev.doge.module.modules.movement.*;
import com.dogedev.doge.module.modules.player.*;
import com.dogedev.doge.module.modules.render.*;
import com.dogedev.doge.module.modules.world.Hecker;
import com.dogedev.doge.module.modules.world.Scaffold;

import java.util.ArrayList;

public class ModuleManager {
    public static ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager() {
        //COMBAT
        modules.add(new KillAura());
        modules.add(new AntiBot());
        modules.add(new Criticals());
        modules.add(new Velocity());
        modules.add(new AutoPot());
        modules.add(new FastBow());
        modules.add(new MoreKB());
        modules.add(new BowAimbot());

        //MOVEMENT
        modules.add(new Sprint());
        modules.add(new Fly());
        modules.add(new Step());
        modules.add(new LongJump());
        modules.add(new Speed());
        modules.add(new Phase());
        modules.add(new Jesus());
        modules.add(new Teleport());

        //RENDER
        modules.add(new Fullbright());
        modules.add(new ESP());
        modules.add(new Projectiles());
        modules.add(new ESP2());

        //PLAYER
        modules.add(new NoFall());
        modules.add(new AntiFire());
        modules.add(new Zoot());
        modules.add(new AntiLagback());
        modules.add(new FlagDetector());

        //MISC
        modules.add(new NoSwing());
        modules.add(new ChestStealer());
        modules.add(new MurdererFinder());
        modules.add(new Panic());

        //WORLD
        modules.add(new Scaffold());
        modules.add(new Hecker());

        //Register modules above here --^

        //GUI
        modules.add(new ClickGUI());
    }

    public static ArrayList<Module> getModules() {
        return modules;
    }
    public Module getModuleByName(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Module getModule(String name, boolean caseSensitive) {
        return modules.stream().filter(mod -> !caseSensitive && name.equalsIgnoreCase(mod.getName()) || name.equals(mod.getName())).findFirst().orElse(null);
    }
}
