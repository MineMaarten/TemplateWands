package com.minemaarten.templatewands;

import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.minemaarten.templatewands.capabilities.CapabilityTemplateWand;
import com.minemaarten.templatewands.capabilities.DefaultCapabilityStorage;
import com.minemaarten.templatewands.lib.Constants;
import com.minemaarten.templatewands.network.NetworkHandler;
import com.minemaarten.templatewands.proxy.CommonProxy;
import com.minemaarten.templatewands.templates.ingredients.providers.IngredientProviderManager;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MODVERSION, dependencies = "required-after:forge@[14.23.0.2551,)", acceptedMinecraftVersions = "1.12")
public class TemplateWands{

    @SidedProxy(clientSide = "com.minemaarten.templatewands.proxy.ClientProxy", serverSide = "com.minemaarten.templatewands.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Instance(Constants.MOD_ID)
    public static TemplateWands instance;
    private ASMDataTable asmData;
    private IngredientProviderManager providerManager;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        CapabilityManager.INSTANCE.register(CapabilityTemplateWand.class, new DefaultCapabilityStorage<>(), CapabilityTemplateWand::new);
        asmData = event.getAsmData();
        NetworkHandler.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        APIHandler apiHandler = new APIHandler();
        apiHandler.initializeAPIImplementors(asmData);
        providerManager = new IngredientProviderManager(apiHandler);
    }

    public IngredientProviderManager getProviderManager(){
        return providerManager;
    }
}
