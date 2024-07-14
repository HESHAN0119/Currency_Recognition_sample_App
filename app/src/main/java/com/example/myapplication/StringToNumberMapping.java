package com.example.myapplication;
import java.util.HashMap;

public class StringToNumberMapping {
    private static HashMap<String, Integer> stringToNumber = new HashMap<>();

    public static void initializeMapping() {
        String[][] arrays = {
                {
                        "bandhuaa", "bdinu", "Bdinu", "been", "beenduwa", "bhindu", "bhinduwa", "bhinduwak",
                        "Bin", "Bin Dua", "bind", "bindi", "bindiya", "bindiyaa", "Bindu", "bindu", "Bindu ki",
                        "Bindu walk", "Binduwaa", "BinduwaAr", "Binduwaf", "binduwak", "Binduwal", "Binduwali",
                        "Binduwalit", "Binduwalk", "Binduwalkind", "Binduwaq", "Binduwaw", "binduwe", "Bing",
                        "bing duwa", "bing wa", "bingduwa", "bingduwa", "Bingduwa", "Bingwa", "bini", "Dua",
                        "Hindu", "vindu", "Vindu", "vinduwa", "Vinduwa", "winduwa", "Winduwa", "zero", "zeero",
                        "note"
                },
                {
                        "eka", "eeka", "ek", "aka", "ehka", "eeka", "ekh", "ekar", "ekr", "eke", "ekeh", "ekai",
                        "one", "wan", "van", "ekaha", "akei", "aka", "Rekha", "a car", "e car", "e kar", "ekar",
                        "eca", "ecar", "akka", "rekha", "hek", "heka", "hec", "hekaa", "ekaa", "ee", "ae", "ea",
                        "heka", "ekahh", "akah", "ec", "ekar", "ecar", "ekarr", "ekaii", "ekee", "e kee", "a kee",
                        "a k", "ela", "el", "es"
                },
                {
                        "Dekha", "deka", "dheka", "theka", "deeka", "de","dekho", "du", "dek", "de", "da", "dee",
                        "deee", "de kaa", "de ka", "dhe", "d hek", "dhekaa", "deko", "two", "desa", "theka", "tea",
                        "de car", "de cap", "desa", "de saa", "d eh", "da k", "dad", "dak", "dako", "deko", "dheko",
                        "dem", "der", "dr", "del", "delete", "destination", "desk", "dekh", "the car", "dekhen",
                        "dekha", "dece", "de da", "de de", "da da", "dada", "dweka"
                },
                {
                        "Tune", "tuna", "thu", "thuna", "tana", "tu na", "nu ne", "thur", "thum", "thumna", "th",
                        "t", "tuba", "thuba", "thubha", "thu ba", "three", "tree", "thn", "tna", "tunaa", "thena",
                        "thu naa", "tu", "tu a", "tuaa", "thuaa", "thuwa", "thuwaa", "thuh", "thur", "thum", "tu m",
                        "tum", "to", "to not", "to hot", "too", "tohi", "toshi", "tush", "thur", "turkey", "twuna",
                        "to net", "tunar", "tuh na", "to na", "too na", "too nah"
                },
                {
                        "hathara", "atra", "athara", "Fateh", "hathras", "hath", "hara", "haratha", "ha thara",
                        "ethara", "ethera", "athar", "ha", "hater", "hatar", "hatera", "hathara atha", "Hathar",
                        "Hatharath", "Hethara", "Hathraat", "Hatharana", "Hatharathon", "Hatharal", "Hatharathi",
                        "Hithara", "Hatharaka", "Hatharada", "Hatharasha", "Hatharava", "Hathariza", "Hatharina",
                        "Hatharatus", "Hatharax", "Hatharay", "Hatharador", "Hatharondo", "Hatharumba", "Hatharagua",
                        "Hatharab", "Hatharock", "Hatharush", "Hatharave", "Hatharolia", "Hatharify", "Hatharumo",
                        "Hathariste", "Hatharop", "Hatharow", "hatharaaa"
                },
                {
                        "paha", "vahan", "pa", "Pahad", "baha", "papa", "pa pa", "ba ha", "p", "ph", "phaa",
                        "pa aa", "p haa", "p ha", "put", "par", "party", "Pahana", "Pahar", "Pahal", "Pahata",
                        "Pahanga", "Pahalla", "Pahama", "Pahari", "Pahadora", "Pahalax", "Paharuna", "Pahav",
                        "Pahab", "Paharaza", "Pahaloo", "Pahachi", "Pahatoo", "Pahash", "Pahagra", "Pahalta",
                        "Paharum", "Pahadori", "Pahadoom", "Pahadra", "Pahancy", "Paharish", "Pahadex",
                        "Pahalooza", "Pahant", "Pahakka", "Pahalify", "Paharn", "Pahaterra"
                },
                {
                        "higher", "haya", "ayar", "hire", "hi", "aaya", "hay", "six", "sex", "sick", "Hayara",
                        "Hayal", "Hayato", "Hayani", "Hayasha", "Hayador", "Hayari", "Hayanka", "Hayakka",
                        "Hayamma", "Hayaza", "Hayapex", "Hayavista", "Hayarina", "Hayakuna", "Hayaloo",
                        "Hayatika", "Hayambo", "Hayabara", "Hayathon", "Hayadora", "Hayatoo", "Hayaruba",
                        "Hayalify", "Hayant", "Hayasto", "Hayarish", "Hayanka", "Hayadex", "Hayaluna",
                        "Hayantara", "Hayalor", "Hayaruna", "Hayafari", "Hayaterra", "Hayalux", "Hayakka",
                        "hayya", "ha aa", "haiiya"
                },
                {
                        "hatha", "hot", "Hath", "hata", "hatta", "haa", "hath", "hathiya", "hatthth", "hath thaa",
                        "hoe", "how", "haa da", "ha da", "habbit", "has", "Hathar", "Hathana", "Hatho", "Hathal",
                        "Hathira", "Hathatha", "Hathasha", "Hathari", "Hathanka", "Hatharo", "Hathaya",
                        "Hathaka", "Hathavia", "Hathessa", "Hathaz", "Hatharika", "Hathadia", "Hathanta",
                        "Hathaloo", "Hathandra", "hathaa", "ha th", "have", "ha the", "hah the", "hah", "hahah",
                        "hat", "haath", "haat", "heart", "hard", "haa th", "hata"
                },
                {
                        "at", "ata", "ta", "Aate", "aftre", "after", "if", "iftar", "ifthar", "utter", "atter",
                        "ut", "eight", "eit", "ait", "e it", "aaat", "atur", "ather", "atar", "ta", "taa",
                        "ataaa", "etaa", "eta", "ah", "ah t", "ahta", "ah ta", "Atar", "Atal", "Atara",
                        "Atania", "Atasma", "Ataldo", "Atavio", "Atapha", "Atapta", "Atamba", "Atasha",
                        "Atarix", "Atanua", "Atapri", "Atakka", "Atalix", "Atambo", "Atavia", "Atanka",
                        "Atandra", "aaat"
                },
                {
                        "Navodaya", "Navya", "Nagaur", "na waya", "nawaya", "navaya", "nav", "newy", "newya",
                        "newaya", "naave", "na", "Nawar", "Nawalla", "Nawari", "Nawatha", "Nawango", "Nawasha",
                        "Nawayra", "Nawanza", "Nawambo", "Nawayne", "Nawaloo", "Nawarix", "Nawanka", "Nawanda",
                        "Nawaliza", "Nawarni", "Nawargo", "Nawatho", "Nawango", "Nawakka", "Nawanta", "Nawarish",
                        "Nawavista", "Nawatoo", "Nawayla", "Nawaruna", "Nawashi", "Nawalux", "Nawaysha",
                        "Nawasoo", "Nawakara", "Nawarina", "Nawangra", "Nawazoo", "Nawatara", "Nawalya", "Nawaro"
                }
        };

        // Populate the hashmap with mappings
        for (int i = 0; i < arrays.length; i++) {
            for (String word : arrays[i]) {
                stringToNumber.put(word.toLowerCase(), i);
            }
        }
    }

    // Method to find corresponding number for a given string
    public static Integer findNumber(String inputString) {
        return stringToNumber.get(inputString.toLowerCase());
    }
}
