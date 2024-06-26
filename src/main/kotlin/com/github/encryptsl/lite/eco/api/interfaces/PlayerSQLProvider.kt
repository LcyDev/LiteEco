package com.github.encryptsl.lite.eco.api.interfaces

import java.util.*

interface PlayerSQLProvider {
    fun createPlayerAccount(username: String, uuid: UUID, money: Double)
    fun deletePlayerAccount(uuid: UUID)
    fun getExistPlayerAccount(uuid: UUID): Boolean
    fun getTopBalance(top: Int): MutableMap<String, Double>
    fun getTopBalance(): MutableMap<String, Double>
    fun getBalance(uuid: UUID): Double
    fun depositMoney(uuid: UUID, money: Double)
    fun withdrawMoney(uuid: UUID, money: Double)
    fun setMoney(uuid: UUID, money: Double)
    fun purgeAccounts()
    fun purgeDefaultAccounts(defaultMoney: Double)
    fun purgeInvalidAccounts()
}