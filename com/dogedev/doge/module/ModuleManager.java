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
        addModule(new KillAura());
        addModule(new AntiBot());
        addModule(new Criticals());
        addModule(new Velocity());
        addModule(new AutoPot());
        addModule(new FastBow());
        addModule(new MoreKB());
        addModule(new BowAimbot());

        //MOVEMENT
        addModule(new Sprint());
        addModule(new Fly());
        addModule(new Step());
        addModule(new LongJump());
        addModule(new Speed());
        addModule(new Phase());
        addModule(new Jesus());
        addModule(new Teleport());
        addModule(new KeepSprint());
        addModule(new FastLadder());

        //RENDER
        addModule(new Fullbright());
        addModule(new ESP());
        addModule(new Projectiles());
        addModule(new ESP2());
        addModule(new XRay());
        addModule(new HUD());
        addModule(new Chams());

        //PLAYER
        addModule(new NoFall());
        addModule(new AntiFire());
        addModule(new Zoot());
        addModule(new AntiLagback());
        addModule(new FlagDetector());

        //MISC
        addModule(new NoSwing());
        addModule(new ChestStealer());
        addModule(new MurdererFinder());
        addModule(new Panic());

        //WORLD
        addModule(new Scaffold());
        addModule(new Hecker());

        //Register modules above here --^

        //GUI
        addModule(new ClickGUI());
    }

    public static ArrayList<Module> getModules() {
        return modules;
    }
    public Module getModuleByName(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static void addModule(Module m) {
        modules.add(m);
    }

    public Module getModule(String name, boolean caseSensitive) {
        return modules.stream().filter(mod -> !caseSensitive && name.equalsIgnoreCase(mod.getName()) || name.equals(mod.getName())).findFirst().orElse(null);
    }

    public <T extends Module> T getModule(Class<T> clazz) {
        return (T) modules.stream().filter(mod -> mod.getClass() == clazz).findFirst().orElse(null);
    }
}
