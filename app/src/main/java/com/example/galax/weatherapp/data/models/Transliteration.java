package com.example.galax.weatherapp.data.models;

import java.util.ArrayList;


public class Transliteration {


    private final ArrayList<String[]> russian;
    private final ArrayList<String[]> ukrainian;

    Transliteration() {


        russian = new ArrayList<String[]>();


        russian.add(new String[]{"Ѣ", "Ie", "ѣ", "ie"});
        russian.add(new String[]{"Ю", "Iu", "ю", "iu"});
        russian.add(new String[]{"Я", "Ia", "я", "ia"});


        russian.add(new String[]{"А", "A", "а", "a"});
        russian.add(new String[]{"Б", "B", "б", "b"});
        russian.add(new String[]{"В", "V", "в", "v"});
        russian.add(new String[]{"Г", "G", "г", "g"});
        russian.add(new String[]{"Д", "D", "д", "d"});
        russian.add(new String[]{"Е", "E", "е", "e"});
        russian.add(new String[]{"Ё", "Yo", "ё", "yo"});
        russian.add(new String[]{"Ж", "Zh", "ж", "zh"});
        russian.add(new String[]{"З", "Z", "з", "z"});
        russian.add(new String[]{"И", "I", "и", "i"});
        russian.add(new String[]{"Й", "Y", "й", "y"});
        russian.add(new String[]{"К", "K", "к", "k"});
        russian.add(new String[]{"Л", "L", "л", "l"});
        russian.add(new String[]{"М", "M", "м", "m"});
        russian.add(new String[]{"Н", "N", "н", "n"});
        russian.add(new String[]{"О", "O", "о", "o"});
        russian.add(new String[]{"П", "P", "п", "p"});
        russian.add(new String[]{"Р", "R", "р", "r"});
        russian.add(new String[]{"С", "S", "с", "s"});
        russian.add(new String[]{"Т", "T", "т", "t"});
        russian.add(new String[]{"У", "U", "у", "u"});
        russian.add(new String[]{"Ф", "F", "ф", "f"});
        russian.add(new String[]{"Х", "Kh", "х", "kh"});
        russian.add(new String[]{"Ц", "Ts", "ц", "ts"});
        russian.add(new String[]{"Ч", "Ch", "ч", "ch"});
        russian.add(new String[]{"Ш", "Sh", "ш", "sh"});
        russian.add(new String[]{"Щ", "Shch", "щ", "shch"});

        russian.add(new String[]{"Ъ", "ʺ", "ъ", "ʺ"});
        russian.add(new String[]{"Ы", "Y", "ы", "y"});
        russian.add(new String[]{"Ь", "ʹ", "ь", "ʹ"});
        russian.add(new String[]{"Э", "Ė", "э", "ė"});




        ukrainian = new ArrayList<String[]>();
        ukrainian.add(new String[]{"Щ", "Shch", "щ", "shch"});
        ukrainian.add(new String[]{"Є", "I͡E", "є", "i͡e"});
        ukrainian.add(new String[]{"Ж", "Z͡H", "ж", "z͡h"});
        ukrainian.add(new String[]{"Ц", "T͡S", "ц", "t͡s"});
        ukrainian.add(new String[]{"Ю", "I͡U", "ю", "i͡u"});
        ukrainian.add(new String[]{"Я", "I͡A", "я", "i͡a"});
        ukrainian.add(new String[]{"Х", "Kh", "х", "kh"});
        ukrainian.add(new String[]{"Ч", "Ch", "ч", "ch"});
        ukrainian.add(new String[]{"Ш", "Sh", "ш", "sh"});
        ukrainian.add(new String[]{"А", "A", "а", "a"});
        ukrainian.add(new String[]{"Б", "B", "б", "b"});
        ukrainian.add(new String[]{"В", "V", "в", "v"});
        ukrainian.add(new String[]{"Г", "H", "г", "h"});
        ukrainian.add(new String[]{"Ґ", "G", "ґ", "g"});
        ukrainian.add(new String[]{"Д", "D", "д", "d"});
        ukrainian.add(new String[]{"Е", "E", "е", "e"});
        ukrainian.add(new String[]{"З", "Z", "з", "z"});
        ukrainian.add(new String[]{"И", "Y", "и", "y"});
        ukrainian.add(new String[]{"І", "I", "і", "i"});
        ukrainian.add(new String[]{"Ї", "Ï", "ї", "ï"});
        ukrainian.add(new String[]{"Й", "Ĭ", "й", "ĭ"});
        ukrainian.add(new String[]{"К", "K", "к", "k"});
        ukrainian.add(new String[]{"Л", "L", "л", "l"});
        ukrainian.add(new String[]{"М", "M", "м", "m"});
        ukrainian.add(new String[]{"Н", "N", "н", "n"});
        ukrainian.add(new String[]{"О", "O", "о", "o"});
        ukrainian.add(new String[]{"П", "P", "п", "p"});
        ukrainian.add(new String[]{"Р", "R", "р", "r"});
        ukrainian.add(new String[]{"С", "S", "с", "s"});
        ukrainian.add(new String[]{"Т", "T", "т", "t"});
        ukrainian.add(new String[]{"У", "U", "у", "u"});
        ukrainian.add(new String[]{"Ф", "F", "ф", "f"});
        ukrainian.add(new String[]{"Ь", "ʹ", "ь", "ʹ"});

    }


    public ArrayList<String[]> getRussian() {
        return russian;
    }


    public ArrayList<String[]> getUkrainian() {
        return ukrainian;
    }

}

