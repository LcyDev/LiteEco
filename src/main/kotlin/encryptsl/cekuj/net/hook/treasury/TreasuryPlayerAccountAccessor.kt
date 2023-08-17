package encryptsl.cekuj.net.hook.treasury

import encryptsl.cekuj.net.LiteEco
import me.lokka30.treasury.api.economy.account.PlayerAccount
import me.lokka30.treasury.api.economy.account.accessor.PlayerAccountAccessor
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap

class TreasuryPlayerAccountAccessor(private val instance: LiteEco) : PlayerAccountAccessor() {

    companion object {
        private val accountMap: MutableMap<UUID, TreasuryAccount> = ConcurrentHashMap()
    }

    override fun getOrCreate(context: PlayerAccountCreateContext): CompletableFuture<PlayerAccount> {
        return CompletableFuture.supplyAsync {
            accountMap.computeIfAbsent(context.uniqueId) { uuid -> TreasuryAccount(instance, uuid) }
        }
    }
}