package com.client.headlineshay.network.enums



class AvailableCountries{

    companion object {

        val countriesMap = HashMap<String,String>()

        fun getCountriesHashMap():HashMap<String,String> {
            for(country in Country.values()){
            countriesMap.put(country.value,country.name)
        }
            return countriesMap }

        fun isPresent(code: String):Boolean?{
            return getCountriesHashMap().containsKey(code)
        }
    }

}

enum class Country(val value: String) {
    ARAB_EMIRATES("ae"),
    ARGENTINA("ar"),
    AUSTRIA("at"),
    AUSTRALIA("au"),
    BELGIUM("be"),
    BULGARIA("bg"),
    BRAZIL("br"),
    CANADA("ca"),
    SWITZERLAND("ch"),
    CHINA("cn"),
    COLUMBIA("co"),
    CUBA("cu"),
    CZECH("cz"),
    DE("de"),
    EGYPT("eg"),
    FRANCE("fr"),
    UNITED_KINGDOM("gb"),
    GREECE("gr"),
    HONG_KONG("hk"),
    HUNGARY("hu"),
    INDONESIA("id"),
    IRELAND("ie"),
    ISRAEL("il"),
    IN("in"),
    ITALY("it"),
    JAPAN("jp"),
    SOUTH_KOREA("kr"),
    LITHUANIA("lt"),
    LATVIA("lv"),
    MOROCCO("ma"),
    MEXICO("mx"),
    MALAYSIA("my"),
    NIGERIA("ng"),
    NETHERLANDS("nl"),
    NO("no"),
    NEW_ZEALAND("nz"),
    PHILIPPINES("ph"),
    POLAND("pl"),
    PORTUGAL("pt"),
    ROMANIA("ro"),
    SERBIA("rs"),
    RUSSIA("ru"),
    SAUDI_ARABIA("sa"),
    SWEDEN("se"),
    SINGAPORE("sg"),
    SLOVENIA("si"),
    SLOVAKIA("sk"),
    THAILAND("th"),
    TURKEY("tr"),
    TAIWAN("tw"),
    UKRAINE("ua"),
    UNITED_STATES("us"),
    VENEZUELA("ve"),
    SOUTH_AFRICA("za")

}