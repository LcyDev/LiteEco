package encryptsl.cekuj.net.hook.treasury

enum class TreasuryFailureReasons(val description: String) {

    INVALID_VALUE("Invalid value inputted!"),
    INVALID_CURRENCY("Invalid currency inputted!"),
    NEGATIVE_BALANCES_NOT_SUPPORTED("Negative balances are not supported!"),
    FEATURE_NOT_SUPPORTED("Unsupported feature!"),
    CURRENCY_NOT_FOUND("Currency not found!");

    fun toException(): EconomyException {
        return EconomyException(this)
    }

    fun toException(cause: Throwable): EconomyException {
        return EconomyException(this, cause)
    }
}
