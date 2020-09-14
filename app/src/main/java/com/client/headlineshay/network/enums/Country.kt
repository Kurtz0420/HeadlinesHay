package com.client.headlineshay.network.enums




enum class Country(val value: String){


    UNITED_ARAB_EMIRATES("ae"),
    ARGENTINA("ar"),
    AUSTRIA("at"),
    AUSTRALIA("au"),
    BELGIUM("be"),
    BULGARIA("bg"),
    BRAZIL("br"),
    CANADA("ca"),
    SWITZERLAND("ch"),
    CHINA("cn"),
    COLOMBIA("co"),
    CUBA("cu"),
    CZECHIA("cz"),
    GERMANY("de"),
    EGYPT("eg"),
    FRANCE("fr"),
    UNITED_KINGDOM("gb"),
    GREECE("gr"),
    HONG_KONG("hk"),
    HUNGARY("hu"),
    INDONESIA("id"),
    IRELAND("ie"),
    ISRAEL("il"),
    INDIA("in"),
    ITALY("it"),
    JAPAN("jp"),
    KOREA("kr"),
    LITHUANIA("lt"),
    LATVIA("lv"),
    MOROCCO("ma"),
    MEXICO("mx"),
    MALAYSIA("my"),
    NIGERIA("ng"),
    NETHERLANDS("nl"),
    NORWAY("no"),
    NEW_ZEALAND("nz"),
    PHILIPPINES("ph"),
    POLAND("pl"),
    PORTUGAL("pt"),
    ROMANIA("ro"),
    SERBIA("rs"),
    RUSSIA("ru"),
    SOUTH_AFRICA("sa"),
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
    ZUID_AFRICA("za"),

}


class AvailableCountries{

    companion object{
        fun isPresent(value:String):Boolean{

            enumValues<Country>().forEach {
                if(it.value == value){
                    return true
                }
            }
            return false
        }
    }
}
