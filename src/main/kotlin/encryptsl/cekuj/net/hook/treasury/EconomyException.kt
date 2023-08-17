package encryptsl.cekuj.net.hook.treasury

class EconomyException(val failureReason: TreasuryFailureReasons) : RuntimeException(failureReason.description) {

    constructor(failureReason: TreasuryFailureReasons, cause: Throwable) : super(failureReason.description, cause)

}