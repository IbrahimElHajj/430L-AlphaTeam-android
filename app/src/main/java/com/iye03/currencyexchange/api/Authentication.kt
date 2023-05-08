import android.content.Context
import android.content.SharedPreferences
import com.iye03.currencyexchange.api.model.Token

object Authentication {
    private var token: String? = null
    private var role: String? = null
    private var preferences: SharedPreferences? = null
    public fun initialize(context: Context) {
        preferences = context.getSharedPreferences("SETTINGS",
            Context.MODE_PRIVATE)
        token = preferences?.getString("TOKEN", null)
        role = preferences?.getString("ROLE", null)
    }
    public fun getToken(): String? {
        return token
    }
    public fun saveToken(token: Token) {
        this.token = token.token
        this.role = token.role
        preferences?.edit()?.putString("TOKEN", token.token)?.apply()
        preferences?.edit()?.putString("ROLE",token.role)?.apply()
    }
    public fun canSendNews(): Boolean {
        return role=="newscaster"
    }
    public fun clearToken() {
        this.token = null
        preferences?.edit()?.remove("TOKEN")?.apply()
        preferences?.edit()?.remove("ROLE")?.apply()
    }
}
