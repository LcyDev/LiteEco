package encryptsl.cekuj.net.hook.treasury

import encryptsl.cekuj.net.LiteEco
import me.lokka30.treasury.api.economy.account.accessor.AccountAccessor
import me.lokka30.treasury.api.economy.account.accessor.NonPlayerAccountAccessor
import me.lokka30.treasury.api.economy.account.accessor.PlayerAccountAccessor

class TreasuryAccountAccessor(private val instance: LiteEco) : AccountAccessor {

    override fun player(): PlayerAccountAccessor {
        return TreasuryPlayerAccountAccessor(instance)
    }

    override fun nonPlayer(): NonPlayerAccountAccessor {
        return TreasuryNonPlayerAccountAccessor()
    }
}