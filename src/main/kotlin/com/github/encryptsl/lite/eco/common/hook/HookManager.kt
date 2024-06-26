package com.github.encryptsl.lite.eco.common.hook

import com.github.encryptsl.lite.eco.LiteEco
import com.github.encryptsl.lite.eco.common.hook.bettereconomy.BetterEconomyHook
import com.github.encryptsl.lite.eco.common.hook.miniplaceholder.EconomyMiniPlaceholder
import com.github.encryptsl.lite.eco.common.hook.placeholderapi.EconomyPlaceholderAPI
import com.github.encryptsl.lite.eco.common.hook.vault.AdaptiveEconomyVaultAPI
import net.milkbowl.vault.economy.Economy
import org.bukkit.plugin.ServicePriority

class HookManager(private val liteEco: LiteEco) {

    /**
     * Method for disable plugin if is detected unsupported plugin.
     * @param pluginName - String name of plugin is CaseSensitive.
     */
    fun blockPlugin(pluginName: String) {
        if (liteEco.pluginManager.isPluginEnabled(pluginName)) {
            liteEco.logger.severe("Please don't use $pluginName, because there can be conflict.")
            liteEco.pluginManager.disablePlugin(liteEco)
        }
    }

    /**
     * Method for check if plugin is installed
     * @param pluginName - String name of plugin is CaseSensitive
     * @return Boolean
     */
    private fun isPluginInstalled(pluginName: String): Boolean {
        return liteEco.pluginManager.getPlugin(pluginName) != null
    }

    /**
     * Method of registering Placeholders if plugin PlaceholderAPI is enabled.
     */
    fun hookPAPI() {
        if (isPluginInstalled("PlaceholderAPI")) {
            EconomyPlaceholderAPI(liteEco, LiteEco.PAPI_VERSION).register()
            liteEco.logger.info("PlaceholderAPI found, placeholders are registered !")
        } else {
            liteEco.logger.warning("Warning plugin PlaceholderAPI not found !")
            liteEco.logger.warning("Keep in mind without PlaceholderAPI, you can't use LiteEco placeholders.")
        }
    }

    fun hookMiniPlaceholders() {
        if (isPluginInstalled("MiniPlaceholders")) {
            EconomyMiniPlaceholder(liteEco).register()
            liteEco.logger.info("MiniPlaceholders found, placeholders are registered !")
        } else {
            liteEco.logger.warning("Warning plugin MiniPlaceholders not found !")
            liteEco.logger.warning("Keep in mind without MiniPlaceholders, you can't use LiteEco placeholders.")
        }
    }

    fun hookVault() {
        if (isPluginInstalled("Vault")) {
            liteEco.server.servicesManager.register(Economy::class.java, AdaptiveEconomyVaultAPI(liteEco), liteEco, ServicePriority.Highest)
            liteEco.logger.info("Vault is registered, LiteEco now working like a provider !")
        } else {
            liteEco.logger.warning("Warning plugin Vault not found !")
            liteEco.logger.warning("For better experience please download Vault.")
            liteEco.logger.warning("Keep in mind without Vault, LiteEco can't use API from Vault.")
        }
    }

    fun hookBetterEconomy() {
        if (BetterEconomyHook().getBetterEconomy() || isPluginInstalled("BetterEconomy")) {
            liteEco.logger.info("Plugin BetterEconomy found.")
            liteEco.logger.info("You can now convert from BetterEconomy to LiteEco, with /eco convert BetterEconomy.")
        }
    }

    fun hookTreasury() {
        if (isPluginInstalled("Treasury")) {
            liteEco.logger.warning("Please use Vault instead of Treasury")
            liteEco.logger.warning("LiteEco in this version not supporting Treasury... because support is not ready.")
        }
    }
}