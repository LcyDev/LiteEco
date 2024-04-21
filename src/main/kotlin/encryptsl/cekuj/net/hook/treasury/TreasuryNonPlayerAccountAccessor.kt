package encryptsl.cekuj.net.hook.treasury

import me.lokka30.treasury.api.common.misc.FutureHelper
import me.lokka30.treasury.api.economy.account.NonPlayerAccount
import me.lokka30.treasury.api.economy.account.accessor.NonPlayerAccountAccessor
import java.util.concurrent.CompletableFuture

class TreasuryNonPlayerAccountAccessor : NonPlayerAccountAccessor() {

    override fun getOrCreate(context: NonPlayerAccountCreateContext): CompletableFuture<NonPlayerAccount> {
        return FutureHelper.failedFuture(TreasuryFailureReasons.FEATURE_NOT_SUPPORTED.toException())
    }
}
