package encryptsl.cekuj.net.hook.treasury

import encryptsl.cekuj.net.LiteEco
import me.lokka30.treasury.api.common.NamespacedKey
import me.lokka30.treasury.api.common.misc.FutureHelper
import me.lokka30.treasury.api.common.misc.TriState
import me.lokka30.treasury.api.economy.EconomyProvider
import me.lokka30.treasury.api.economy.account.AccountData
import me.lokka30.treasury.api.economy.account.accessor.AccountAccessor
import me.lokka30.treasury.api.economy.currency.Currency
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors

class TreasuryEconomyAPI(private val liteEco: LiteEco, private val currency: Currency, private val accountAccessor: TreasuryAccountAccessor) : EconomyProvider {

    companion object {
        const val CURRENCY_IDENTIFIER = "lite_eco_economy"
    }

    override fun accountAccessor(): AccountAccessor {
        return accountAccessor
    }

    override fun hasAccount(accountData: AccountData): CompletableFuture<Boolean> {
        return if (accountData.isPlayerAccount && accountData.playerIdentifier.isPresent) {
            CompletableFuture.supplyAsync {
                liteEco.api.hasAccount(Bukkit.getOfflinePlayer(accountData.playerIdentifier.get()))
            }
        } else {
            FutureHelper.failedFuture(TreasuryFailureReasons.FEATURE_NOT_SUPPORTED.toException())
        }
    }

    override fun retrievePlayerAccountIds(): CompletableFuture<MutableCollection<UUID>> {
        return CompletableFuture.supplyAsync {
            val uuids: List<UUID> = Arrays.stream(Bukkit.getOfflinePlayers())
                .parallel()
                .map(OfflinePlayer::getUniqueId)
                .collect(Collectors.toList())

            uuids.parallelStream()
                .filter { uuid -> liteEco.api.hasAccount(Bukkit.getOfflinePlayer(uuid)) }
                .collect(Collectors.toList())
        }
    }

    override fun retrieveNonPlayerAccountIds(): CompletableFuture<Collection<NamespacedKey>> {
        return CompletableFuture.completedFuture(Collections.emptyList())
    }

    override fun getPrimaryCurrency(): Currency {
        return currency
    }

    override fun findCurrency(identifier: String): Optional<Currency> {
        return if (currency.identifier == CURRENCY_IDENTIFIER) Optional.of(currency) else Optional.empty()
    }

    override fun getCurrencies(): Set<Currency> {
        return Collections.singleton(currency)
    }

    override fun registerCurrency(currency: Currency): CompletableFuture<TriState> {
        return CompletableFuture.completedFuture(TriState.FALSE)
    }

    override fun unregisterCurrency(currency: Currency): CompletableFuture<TriState> {
        return CompletableFuture.completedFuture(TriState.FALSE)
    }
}
