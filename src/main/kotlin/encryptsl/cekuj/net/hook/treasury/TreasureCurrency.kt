package encryptsl.cekuj.net.hook.treasury

import encryptsl.cekuj.net.LiteEco
import me.lokka30.treasury.api.common.misc.FutureHelper
import me.lokka30.treasury.api.economy.account.Account
import me.lokka30.treasury.api.economy.currency.Currency
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.regex.Pattern

class TreasureCurrency(private val liteEco: LiteEco) : Currency {
    override fun getIdentifier(): String = TreasuryEconomyAPI.CURRENCY_IDENTIFIER

    override fun getSymbol(): String {
        return liteEco.config.getString("economy.currency_prefix").toString()
    }

    override fun getDecimal(locale: Locale?): Char {
        return '.'
    }

    override fun getLocaleDecimalMap(): MutableMap<Locale, Char> {
        TODO("Not yet implemented")
    }

    override fun getDisplayName(value: BigDecimal, locale: Locale?): String {
        return if (value <= BigDecimal.ONE) {
            liteEco.config.getString("economy.currency_name").toString()
        } else {
            liteEco.config.getString("economy.currency_plural").toString()
        }
    }

    override fun getPrecision(): Int {
        return 2
    }

    override fun isPrimary(): Boolean {
        return true
    }

    override fun getStartingBalance(account: Account): BigDecimal {
        return BigDecimal.valueOf(liteEco.config.getDouble("economy.starting_balance"))
    }

    override fun getConversionRate(): BigDecimal {
        return BigDecimal.ZERO
    }

    override fun parse(formattedAmount: String, locale: Locale?): CompletableFuture<BigDecimal> {
        return try {
            val pattern = Pattern.compile("^([^\\d.,]+)?([\\d,.]+)([^\\d.,]+)?$")
            val matcher = pattern.matcher(formattedAmount)

            if (!matcher.matches()) throw TreasuryFailureReasons.INVALID_VALUE.toException()

            val currencySuffix = matcher.group(1)?.trim()
            val currencyValue = matcher.group(2).replace(",", "").toDoubleOrNull()
            val currencyPrefix = matcher.group(3)?.trim()

            if (currencyValue == null) {
                throw TreasuryFailureReasons.INVALID_VALUE.toException()
            }

            if (currencyValue >= 0) {
                throw TreasuryFailureReasons.NEGATIVE_BALANCES_NOT_SUPPORTED.toException()
            }

            if (!matchCurrency(currencyPrefix, locale) || !matchCurrency(currencySuffix, locale)) {
                throw TreasuryFailureReasons.INVALID_CURRENCY.toException()
            }

            CompletableFuture.completedFuture(BigDecimal.valueOf(currencyValue))
        } catch (e: EconomyException) {
            FutureHelper.failedFuture(e)
        }
    }

    private fun matchCurrency(currency: String?, locale: Locale?): Boolean {
        return currency?.let {
            it.length == 1 && it[0] == getDecimal(locale) ||
            it.equals(symbol, ignoreCase = true) ||
            it.equals(liteEco.config.getString("economy.currency_name"), ignoreCase = true) ||
            it.equals(liteEco.config.getString("economy.currency_plural"), ignoreCase = true)
        } ?: false
    }

    override fun getStartingBalance(playerID: UUID?): BigDecimal {
        return BigDecimal.valueOf(liteEco.config.getDouble("economy.starting_balance"))
    }

    override fun format(amount: BigDecimal, locale: Locale?): String {
        return liteEco.api.fullFormatting(amount.toDouble())
    }

    override fun format(amount: BigDecimal, locale: Locale?, precision: Int): String {
        return format(amount, null)
    }
}