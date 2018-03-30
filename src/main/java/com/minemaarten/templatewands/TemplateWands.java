package com.minemaarten.templatewands;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;

import com.minemaarten.templatewands.lib.Constants;
import com.minemaarten.templatewands.proxy.CommonProxy;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MODVERSION, dependencies = "required-after:forge@[14.23.0.2551,)", acceptedMinecraftVersions = "1.12")
public class TemplateWands{

    @SidedProxy(clientSide = "com.minemaarten.templatewands.proxy.ClientProxy", serverSide = "com.minemaarten.templatewands.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Instance(Constants.MOD_ID)
    public static TemplateWands instance;
}
