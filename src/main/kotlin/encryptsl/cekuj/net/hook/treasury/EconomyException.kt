package encryptsl.cekuj.net.hook.treasury

class EconomyException(failureReason: TreasuryFailureReasons) : RuntimeException(failureReason.description) {

    constructor(failureReason: TreasuryFailureReasons, cause: Throwable) : this(failureReason) {
        initCause(cause)
    }
}
