package encryptsl.cekuj.net.hook.treasury

import encryptsl.cekuj.net.LiteEco
import encryptsl.cekuj.net.extensions.isApproachingZero
import me.lokka30.treasury.api.common.misc.FutureHelper
import me.lokka30.treasury.api.economy.account.PlayerAccount
import me.lokka30.treasury.api.economy.currency.Currency
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionType
import org.bukkit.Bukkit
import java.math.BigDecimal
import java.time.temporal.Temporal
import java.util.*
import java.util.concurrent.CompletableFuture


class TreasuryAccount(private val liteEco: LiteEco, private val uuid: UUID) : PlayerAccount {


    override fun identifier(): UUID {
        return uuid
    }

    override fun getName(): Optional<String> {
        return Optional.ofNullable(Bukkit.getOfflinePlayer(uuid).name)
    }

    override fun retrieveBalance(currency: Currency): CompletableFuture<BigDecimal> {
        return if (currency.identifier != TreasuryEconomyAPI.CURRENCY_IDENTIFIER) {
            FutureHelper.failedFuture(TreasuryFailureReasons.CURRENCY_NOT_FOUND.toException())
        } else {
            CompletableFuture.supplyAsync {
                BigDecimal.valueOf(liteEco.api.getBalance(Bukkit.getOfflinePlayer(uuid)))
            }
        }
    }

    override fun doTransaction(economyTransaction: EconomyTransaction): CompletableFuture<BigDecimal> {
        return CompletableFuture.supplyAsync {
            if (economyTransaction.currencyId != TreasuryEconomyAPI.CURRENCY_IDENTIFIER) {
                throw TreasuryFailureReasons.CURRENCY_NOT_FOUND.toException()
            }
            val type = economyTransaction.type
            val amount = economyTransaction.amount
            val amountDouble = amount.toDouble()

            if (amountDouble < 0) {
                throw TreasuryFailureReasons.NEGATIVE_BALANCES_NOT_SUPPORTED.toException()
            }

            val offlinePlayer = Bukkit.getOfflinePlayer(uuid)

            when (type) {
                EconomyTransactionType.DEPOSIT -> liteEco.api.depositMoney(offlinePlayer, amountDouble)
                EconomyTransactionType.WITHDRAWAL -> liteEco.api.withDrawMoney(offlinePlayer, amountDouble)
                EconomyTransactionType.SET -> {
                    if (amountDouble.isApproachingZero()) {
                        throw TreasuryFailureReasons.NEGATIVE_BALANCES_NOT_SUPPORTED.toException()
                    }
                    liteEco.api.setMoney(offlinePlayer, amountDouble)
                }
            }

            BigDecimal.valueOf(liteEco.api.getBalance(offlinePlayer))
        }
    }

    override fun deleteAccount(): CompletableFuture<Boolean> {
        return CompletableFuture.supplyAsync {
            liteEco.api.deleteAccount(Bukkit.getOfflinePlayer(uuid))
        }
    }

    override fun retrieveHeldCurrencies(): CompletableFuture<Collection<String>> {
        return CompletableFuture.completedFuture(listOf(TreasuryEconomyAPI.CURRENCY_IDENTIFIER))
    }

    override fun retrieveTransactionHistory(
        transactionCount: Int,
        from: Temporal,
        to: Temporal
    ): CompletableFuture<Collection<EconomyTransaction>> {
        return FutureHelper.failedFuture(
            TreasuryFailureReasons.FEATURE_NOT_SUPPORTED.toException()
        )
    }
}