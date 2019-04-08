package edu.wit.mobileapp.languagetravelapp;

import android.util.Log;

public class Conjugator {

    public static String[][] conjugate(String infinitive, VerbForm[] verbForms, boolean portugal) {
        infinitive = infinitive.toLowerCase();
        int infLen = infinitive.length();
        String[][] verbsToReturn = null;
        if (!infinitive.contains(" ") && !infinitive.contains("-") && !infinitive.contains("+") && infLen > 1 && verbForms != null && verbForms.length > 0 && infinitive.charAt(infLen - 1) == 'r') {
            String ending = infinitive.substring(infLen - 2, infLen);
            String stem = infinitive.substring(0, infLen - 2);
            String eAndIStem = stem;
            String aAndOStem = stem;
            if (infLen > 2) {
                switch (infinitive.substring(infLen - 3, infLen)) {
                    case "car":
                        eAndIStem = infinitive.substring(0, infLen - 3) + "qu";
                        break;
                    case "çar":
                        eAndIStem = infinitive.substring(0, infLen - 3) + "c";
                        break;
                    case "gar":
                        eAndIStem = infinitive.substring(0, infLen - 3) + "gu";
                        break;
                    case "cer":
                        aAndOStem = infinitive.substring(0, infLen - 3) + "ç";
                        break;
                    case "ger":
                    case "gir":
                        aAndOStem = infinitive.substring(0, infLen - 3) + "j";
                        break;
                }
                if (infLen > 3) {
                    switch (infinitive.substring(infLen - 4, infLen)) {
                        case "guir":
                        case "guer":
                            aAndOStem = infinitive.substring(0, infLen - 3);
                            break;
                    }

                }
            }
            Log.v("conjugationdebugging", infinitive + ", eAndIStem: " + eAndIStem + ", aAndOStem: " + aAndOStem);
            if (ending.equals("er") && infLen > 2) {
                if (infinitive.charAt(infLen - 3) == 'c') {
                    eAndIStem = infinitive.substring(0, infLen - 3) + "qu";
                } else if (infinitive.charAt(infLen - 3) == 'ç') {
                    eAndIStem = infinitive.substring(0, infLen - 3) + "c";
                } else if (infinitive.charAt(infLen - 3) == 'g') {
                    eAndIStem = infinitive.substring(0, infLen - 3) + "gu";
                }
            }
            String specialStem = stem;
            if (ending.equals("ir")) {
                switch (infinitive) {
                    case "conseguir":
                        specialStem = "consig";
                        break;
                    case "divertir":
                        specialStem = "divirt";
                        break;
                    case "mentir":
                        specialStem = "mint";
                        break;
                    case "repetir":
                        specialStem = "repit";
                        break;
                    case "seguir":
                        specialStem = "sig";
                        break;
                    case "sentir":
                        specialStem = "sint";
                        break;
                    case "servir":
                        specialStem = "sirv";
                        break;
                    case "vestir":
                        specialStem = "vist";
                        break;
                    case "cobrir":
                        specialStem = "cubr";
                        break;
                    case "descobrir":
                        specialStem = "descubr";
                        break;
                    case "dormir":
                        specialStem = "durm";
                        break;
                    case "erguer":
                        specialStem = "erg";

                }
            }
            String participle = getParticiple(infinitive);
            String gerund = getGerund(infinitive);
            String subjRoot1;
            String subjRoot2;
            verbsToReturn = new String[verbForms.length][6];
            switch (infinitive) {
                case "dizer":
                    subjRoot1 = "dig";
                    subjRoot2 = "disseram";
                    break;
                case "crer":
                    subjRoot1 = "crei";
                    subjRoot2 = stem;
                    break;
                case "ler":
                    subjRoot1 = "lei";
                    subjRoot2 = stem;
                    break;
                case "medir":
                    subjRoot1 = "meç";
                    subjRoot2 = stem;
                    break;
                case "ouvir":
                    subjRoot1 = "ouç";
                    subjRoot2 = stem;
                    break;
                case "pedir":
                    subjRoot1 = "peç";
                    subjRoot2 = stem;
                    break;
                case "perder":
                    subjRoot1 = "perc";
                    subjRoot2 = stem;
                    break;
                case "rir":
                    subjRoot1 = "ri";
                    subjRoot2 = stem;
                    break;
                case "valer":
                    subjRoot1 = "valh";
                    subjRoot2 = stem;
                    break;
                default:
                    subjRoot1 = stem;
                    subjRoot2 = stem;
            }
            checkForSpecialVerbs(infinitive, verbForms, verbsToReturn);

            for (int i = 0; i < verbForms.length; i++) {
                if (verbsToReturn[i] == null || verbsToReturn[i][0] == null) {
                    switch (verbForms[i]) {
                        case PRES_IND:
                            switch (ending) {
                                case "ar":
                                    verbsToReturn[i] = new String[]{specialStem + "o", aAndOStem + "as", aAndOStem + "a", aAndOStem + "amos", aAndOStem + "ais", aAndOStem + "am"};
                                case "er":
                                    verbsToReturn[i] = new String[]{specialStem + "o", eAndIStem + "es", eAndIStem + "e", eAndIStem + "emos", eAndIStem + "eis", eAndIStem + "em"};
                                case "ir":
                                    verbsToReturn[i] = new String[]{specialStem + "o", eAndIStem + "es", eAndIStem + "e", eAndIStem + "imos", eAndIStem + "is", eAndIStem + "em"};
                            }
                            break;
                        case PRET_IND:
                            switch (ending) {
                                case "ar":
                                    verbsToReturn[i] = new String[]{stem + "ei", aAndOStem + "aste", aAndOStem + "ou", aAndOStem + (portugal ? "ámos" : "amos"), aAndOStem + "astes", aAndOStem + "aram"};
                                case "er":
                                    verbsToReturn[i] = new String[]{stem + "i", eAndIStem + "este", eAndIStem + "eu", eAndIStem + "emos", eAndIStem + "estes", eAndIStem + "eram"};
                                case "ir":
                                    verbsToReturn[i] = new String[]{stem + "i", eAndIStem + "iste", eAndIStem + "iu", eAndIStem + "imos", eAndIStem + "istes", eAndIStem + "iram"};
                            }
                            break;
                        case IMP_IND:
                            switch (ending) {
                                case "ar":
                                    verbsToReturn[i] = new String[]{stem + "ava", aAndOStem + "avas", aAndOStem + "ava", aAndOStem + "ávamos", aAndOStem + "áveis", aAndOStem + "avam"};
                                case "er":
                                    verbsToReturn[i] = new String[]{stem + "ia", eAndIStem + "ias", eAndIStem + "ia", eAndIStem + "íamos", eAndIStem + "íeis", eAndIStem + "iam"};
                                case "ir":
                                    verbsToReturn[i] = new String[]{stem + "ia", eAndIStem + "ias", eAndIStem + "ia", eAndIStem + "íamos", eAndIStem + "íeis", eAndIStem + "iam"};
                            }
                            break;
                        case SIMP_PLUP_IND:
                            switch (ending) {
                                case "ar":
                                    verbsToReturn[i] = new String[]{stem + "ara", aAndOStem + "aras", aAndOStem + "ara", aAndOStem + "áramos", aAndOStem + "áreis", aAndOStem + "aram"};
                                case "er":
                                    verbsToReturn[i] = new String[]{stem + "era", eAndIStem + "eras", eAndIStem + "era", eAndIStem + "éramos", eAndIStem + "éreis", eAndIStem + "eram"};
                                case "ir":
                                    verbsToReturn[i] = new String[]{stem + "ira", eAndIStem + "iras", eAndIStem + "ira", eAndIStem + "íramos", eAndIStem + "íreis", eAndIStem + "iram"};
                            }
                            break;
                        case FUT_IND:
                            if (ending.equals("ar") || ending.equals("er") || ending.equals("ir")) {
                                verbsToReturn[i] = new String[]{infinitive + "ei", infinitive + "ás", infinitive + "á", infinitive + "emos", infinitive + "eis", infinitive + "ão"};
                            }
                            break;
                        case COND_IND:
                            if (ending.equals("ar") || ending.equals("er") || ending.equals("ir")) {
                                verbsToReturn[i] = new String[]{infinitive + "ia", infinitive + "ias", infinitive + "ia", infinitive + "íamos", infinitive + "íeis", infinitive + "iam"};
                            }
                            break;
                        case PRES_PERF:
                            if (participle != null) {
                                verbsToReturn[i] = new String[]{"tenho " + participle, "tens " + participle, "tem " + participle, "temos " + participle, "tendes " + participle, "têm " + participle};
                            }
                            break;
                        case PLUP:
                            if (participle != null) {
                                verbsToReturn[i] = new String[]{"tinha " + participle, "tinhas " + participle, "tinha " + participle, "tínhamos " + participle, "tínheis " + participle, "tinham " + participle};
                            }
                            break;
                        case FUT_PERF:
                            if (participle != null) {
                                verbsToReturn[i] = new String[]{"terei " + participle, "terás " + participle, "terá " + participle, "teremos " + participle, "tereis " + participle, "terão " + participle};
                            }
                            break;
                        case COND_PERF:
                            if (participle != null) {
                                verbsToReturn[i] = new String[]{"teria " + participle, "terias " + participle, "teria " + participle, "teríamos " + participle, "teríeis " + participle, "teriam " + participle};
                            }
                            break;
                        case PRES_PROG:
                            if (portugal || gerund == null) {
                                verbsToReturn[i] = new String[]{"estou a " + infinitive, "estás a " + infinitive, "está a " + infinitive, "estamos a " + infinitive, "estais a " + infinitive, "estão a " + infinitive};
                            } else {
                                verbsToReturn[i] = new String[]{"estou " + gerund, "estás " + gerund, "está " + gerund, "estamos " + gerund, "estais " + gerund, "estão " + gerund};
                            }
                            break;
                        case IMP_PROG:
                            if (portugal || gerund == null) {
                                verbsToReturn[i] = new String[]{"estava a " + infinitive, "estavas a " + infinitive, "estava a " + infinitive, "estávamos a " + infinitive, "estáveis a " + infinitive, "estavam a " + infinitive};
                            } else {
                                verbsToReturn[i] = new String[]{"estava " + gerund, "estavas " + gerund, "estava " + gerund, "estávamos " + gerund, "estáveis " + gerund, "estavam " + gerund};
                            }
                            break;
                        case PRET_PROG:
                            if (portugal || gerund == null) {
                                verbsToReturn[i] = new String[]{"estive a " + infinitive, "estiveste a " + infinitive, "esteve a " + infinitive, "estivemos a " + infinitive, "estivestes a " + infinitive, "estiveram a " + infinitive};
                            } else {
                                verbsToReturn[i] = new String[]{"estive " + gerund, "estiveste " + gerund, "esteve " + gerund, "estivemos " + gerund, "estivestes " + gerund, "estiveram " + gerund};
                            }
                            break;
                        case SIMP_PLUP_PROG:
                            if (portugal || gerund == null) {
                                verbsToReturn[i] = new String[]{"estivera a " + infinitive, "estiveras a " + infinitive, "estivera a " + infinitive, "estivéramos a " + infinitive, "estivéreis a " + infinitive, "estiveram a " + infinitive};
                            } else {
                                verbsToReturn[i] = new String[]{"estivera " + gerund, "estiveras " + gerund, "estivera " + gerund, "estivéramos " + gerund, "estivéreis " + gerund, "estiveram " + gerund};
                            }
                            break;
                        case FUT_PROG:
                            if (portugal || gerund == null) {
                                verbsToReturn[i] = new String[]{"estarei a " + infinitive, "estarás a " + infinitive, "estará a " + infinitive, "estaremos a " + infinitive, "estareis a " + infinitive, "estarão a " + infinitive};
                            } else {
                                verbsToReturn[i] = new String[]{"estarei " + gerund, "estarás " + gerund, "estará " + gerund, "estaremos " + gerund, "estareis " + gerund, "estarão " + gerund};
                            }
                            break;
                        case COND_PROG:
                            if (portugal || gerund == null) {
                                verbsToReturn[i] = new String[]{"estaria a " + infinitive, "estarias a " + infinitive, "estaria a " + infinitive, "estaríamos a " + infinitive, "estaríeis a " + infinitive, "estariam a " + infinitive};
                            } else {
                                verbsToReturn[i] = new String[]{"estaria " + gerund, "estarias " + gerund, "estaria " + gerund, "estaríamos " + gerund, "estaríeis " + gerund, "estariam " + gerund};
                            }
                            break;
                        case PRES_PERF_PROG:
                            if (portugal || gerund == null) {
                                verbsToReturn[i] = new String[]{"tenho estado a " + infinitive, "tens estado a " + infinitive, "tem estado a " + infinitive, "temos estado a " + infinitive, "tendes estado a " + infinitive, "têm estado a " + infinitive};
                            } else {
                                verbsToReturn[i] = new String[]{"tenho estado " + gerund, "tens estado " + gerund, "tem estado " + gerund, "temos estado " + gerund, "tendes estado " + gerund, "têm estado " + gerund};
                            }
                            break;
                        case PLUP_PROG:
                            if (portugal || gerund == null) {
                                verbsToReturn[i] = new String[]{"tinha estado a " + infinitive, "tinhas estado a " + infinitive, "tinha estado a " + infinitive, "tínhamos estado a " + infinitive, "tínheis estado a " + infinitive, "tinham estado a " + infinitive};
                            } else {
                                verbsToReturn[i] = new String[]{"tinha estado " + gerund, "tinhas estado " + gerund, "tinha estado " + gerund, "tínhamos estado " + gerund, "tínheis estado " + gerund, "tinham estado " + gerund};
                            }
                            break;
                        case FUT_PERF_PROG:
                            if (portugal || gerund == null) {
                                verbsToReturn[i] = new String[]{"terei estado a " + infinitive, "terás estado a " + infinitive, "terá estado a " + infinitive, "teremos estado a " + infinitive, "tereis estado a " + infinitive, "terão estado a " + infinitive};
                            } else {
                                verbsToReturn[i] = new String[]{"terei estado " + gerund, "terás estado " + gerund, "terá estado " + gerund, "teremos estado " + gerund, "tereis estado " + gerund, "terão estado " + gerund};
                            }
                            break;
                        case COND_PERF_PROG:
                            if (portugal || gerund == null) {
                                verbsToReturn[i] = new String[]{"teria estado a " + infinitive, "terias estado a " + infinitive, "teria estado a " + infinitive, "teríamos estado a " + infinitive, "teríeis estado a " + infinitive, "teriam estado a " + infinitive};
                            } else {
                                verbsToReturn[i] = new String[]{"teria estado " + gerund, "terias estado " + gerund, "teria estado " + gerund, "teríamos estado " + gerund, "teríeis estado " + gerund, "teriam estado " + gerund};
                            }
                            break;
                        case PRES_SUBJ:
                            if (subjRoot1 != null) {
                                switch (ending) {
                                    case "ar":
                                        verbsToReturn[i] = new String[]{subjRoot1 + "e", subjRoot1 + "es", subjRoot1 + "e", subjRoot1 + "emos", subjRoot1 + "eis", subjRoot1 + "em"};
                                    case "er":
                                        verbsToReturn[i] = new String[]{subjRoot1 + "a", subjRoot1 + "as", subjRoot1 + "a", subjRoot1 + "amos", subjRoot1 + "ais", subjRoot1 + "am"};
                                    case "ir":
                                        verbsToReturn[i] = new String[]{subjRoot1 + "a", subjRoot1 + "as", subjRoot1 + "a", subjRoot1 + "amos", subjRoot1 + "ais", subjRoot1 + "am"};
                                }
                            }
                            break;
                        case PRES_PERF_SUBJ:
                            if (participle != null) {
                                verbsToReturn[i] = new String[]{"tenha " + participle, "tenhas " + participle, "tenha " + participle, "tenhamos " + participle, "tenhais " + participle, "tenham " + participle};
                            }
                            break;
                        case IMP_SUBJ:
                            if (subjRoot2 != null) {
                                switch (ending) {
                                    case "ar":
                                        verbsToReturn[i] = new String[]{subjRoot2 + "asse", subjRoot2 + "asses", subjRoot2 + "asse", subjRoot2 + "ássemos", subjRoot2 + "ásseis", subjRoot2 + "ássem"};
                                    case "er":
                                        verbsToReturn[i] = new String[]{subjRoot2 + "esse", subjRoot2 + "esses", subjRoot2 + "esse", subjRoot2 + "éssemos", subjRoot2 + "ésseis", subjRoot2 + "essem"};
                                    case "ir":
                                        verbsToReturn[i] = new String[]{subjRoot2 + "isse", subjRoot2 + "isses", subjRoot2 + "isse", subjRoot2 + "íssemos", subjRoot2 + "ísseis", subjRoot2 + "íssem"};
                                }
                            }
                            break;
                        case PLUP_SUBJ:
                            if (participle != null) {
                                verbsToReturn[i] = new String[]{"tivesse " + participle, "tivesses " + participle, "tivesse " + participle, "tivéssemos " + participle, "tivésseis " + participle, "tivessem " + participle};
                            }
                            break;
                        case FUT_SUBJ:
                            if (subjRoot2 != null) {
                                switch (ending) {
                                    case "ar":
                                        verbsToReturn[i] = new String[]{subjRoot2 + "ar", subjRoot2 + "ares", subjRoot2 + "ar", subjRoot2 + "armos", subjRoot2 + "ardes", subjRoot2 + "arem"};
                                    case "er":
                                        verbsToReturn[i] = new String[]{subjRoot2 + "er", subjRoot2 + "eres", subjRoot2 + "er", subjRoot2 + "ermos", subjRoot2 + "erdes", subjRoot2 + "erem"};
                                    case "ir":
                                        verbsToReturn[i] = new String[]{subjRoot2 + "ir", subjRoot2 + "ires", subjRoot2 + "ir", subjRoot2 + "irmos", subjRoot2 + "irdes", subjRoot2 + "irem"};
                                }
                            }
                            break;
                        case FUT_PERF_SUBJ:
                            if (participle != null) {
                                verbsToReturn[i] = new String[]{"tiver " + participle, "tiveres " + participle, "tiver " + participle, "tivermos " + participle, "tiverdes " + participle, "tiveram " + participle};
                            }
                        break;
                    }
                }
            }
        }
        if (verbsToReturn != null) {
            for (String[] verbToReturn : verbsToReturn) {
                if (verbToReturn != null && verbToReturn.length == 6) {
                    Log.v("conjugationdebugging", "" + verbToReturn[0] + ", " + verbToReturn[1] + ", " + verbToReturn[2] + ", " + verbToReturn[3] + ", " + verbToReturn[4] + ", " + verbToReturn[5]);
                }
            }
        }
        return verbsToReturn;
    }

    private static void checkForSpecialVerbs(String infinitive, VerbForm[] verbForms, String[][] verbsToReturn) {
        switch (infinitive) {
            case "boiar":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"bóio", "bóias", "bóia", "boiamos", "boiais", "bóiam"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"bóie", "bóies", "bóie", "bóiemos", "bóieis", "bóiem"};
                            break;
                    }
                }
                break;
            case "recear":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"receio", "receias", "receia", "receamos", "receais", "receiam"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"receie", "receies", "receie", "receiemos", "receieis", "receiem"};
                            break;
                    }
                }
                break;
            case "odiar":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"odeio", "odeias", "odeia", "odiamos", "odiais", "odeiam"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"odeie", "odeies", "odeie", "odeiemos", "odeieis", "odeiem"};
                            break;
                    }
                }
                break;
            case "dar":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"dou", "dás", "dá", "damos", "dais", "dão"};
                            break;
                        case PRET_IND:
                            verbsToReturn[i] = new String[]{"dei", "deste", "deu", "demos", "destes", "deram"};
                            break;
                        case SIMP_PLUP_IND:
                            verbsToReturn[i] = new String[]{"dera", "deras", "dera", "déramos", "déreis", "deram"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"dê", "dês", "dê", "demos", "deis", "dêem"};
                            break;
                        case IMP_SUBJ:
                            verbsToReturn[i] = new String[]{"desse", "desses", "desse", "déssemos", "déssei", "dessem"};
                            break;
                        case FUT_SUBJ:
                            verbsToReturn[i] = new String[]{"der", "deres", "der", "dermos", "derdes", "derem"};
                            break;
                    }
                }
                break;
            case "dizer":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"digo", "dizes", "diz", "dizemos", "dizeis", "dizem"};
                            break;
                        case PRET_IND:
                            verbsToReturn[i] = new String[]{"disse", "disseste", "disse", "dissemos", "dissetes", "disseram"};
                            break;
                        case SIMP_PLUP_IND:
                            verbsToReturn[i] = new String[]{"dissera", "disseras", "dissera", "disséramos", "disséreis", "disseram"};
                            break;
                        case FUT_IND:
                            verbsToReturn[i] = new String[]{"direi", "dirás", "dirá", "diremos", "direis", "dirão"};
                            break;
                        case COND_IND:
                            verbsToReturn[i] = new String[]{"diria", "dirias", "diria", "diríamos", "diríeis", "diriam"};
                            break;
                    }
                }
                break;
            case "estar":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"estou", "estás", "está", "estamos", "estais", "estão"};
                            break;
                        case PRET_IND:
                            verbsToReturn[i] = new String[]{"estive", "estiveste", "esteve", "estivemos", "estivestes", "estiveram"};
                            break;
                        case SIMP_PLUP_IND:
                            verbsToReturn[i] = new String[]{"estivera", "estiveras", "estivera", "estivéramos", "estivéreis", "estivéram"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"esteja", "estejas", "esteja", "estejamos", "estejais", "estejam"};
                            break;
                        case IMP_SUBJ:
                            verbsToReturn[i] = new String[]{"estivesse", "estivesses", "estivesse", "estivéssemos", "estivésseis", "estivéssem"};
                            break;
                        case FUT_SUBJ:
                            verbsToReturn[i] = new String[]{"estiver", "estiveres", "estiver", "estivermos", "estiverdes", "estiverem"};
                            break;
                    }
                }
                break;
            case "fazer":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"faço", "fazes", "faz", "fazemos", "fazeis", "fazem"};
                            break;
                        case PRET_IND:
                            verbsToReturn[i] = new String[]{"fiz", "fizeste", "fez", "fizemos", "fizestes", "fizeram"};
                            break;
                        case SIMP_PLUP_IND:
                            verbsToReturn[i] = new String[]{"fizera", "fizeras", "fizera", "fizéramos", "fizéreis", "fizeram"};
                            break;
                        case FUT_IND:
                            verbsToReturn[i] = new String[]{"farei", "farás", "fará", "faremos", "fareis", "farão"};
                            break;
                        case COND_IND:
                            verbsToReturn[i] = new String[]{"faria", "farias", "faria", "faríamos", "faríeis", "fariam"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"faça", "faças", "faça", "façamos", "façais", "façam"};
                            break;
                        case IMP_SUBJ:
                            verbsToReturn[i] = new String[]{"fizesse", "fizesses", "fizesse", "fizéssemos", "fizésseis", "fizessem"};
                            break;
                        case FUT_SUBJ:
                            verbsToReturn[i] = new String[]{"fizer", "fizeres", "fizer", "fizermos", "fizeres", "fizerem"};
                            break;
                    }
                }
                break;
            case "haver":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"hei", "hás", "há", "havemos", "haveis", "hão"};
                            break;
                        case PRET_IND:
                            verbsToReturn[i] = new String[]{"houve", "houveste", "houve", "houvemos", "houvestes", "houveram"};
                            break;
                        case SIMP_PLUP_IND:
                            verbsToReturn[i] = new String[]{"houvera", "houveras", "houvera", "houvéramos", "houvéreis", "houveram"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"haja", "hajas", "haja", "hajamos", "hajais", "hajam"};
                            break;
                        case IMP_SUBJ:
                            verbsToReturn[i] = new String[]{"houvesse", "houvesses", "houvesse", "houvéssemos", "houvésseis", "houvessem"};
                            break;
                        case FUT_SUBJ:
                            verbsToReturn[i] = new String[]{"houver", "houveres", "houver", "houvermos", "houverdes", "houverem"};
                            break;
                    }
                }
                break;
            case "ir":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"vou", "vais", "vai", "vamos", "ides", "vão"};
                            break;
                        case PRET_IND:
                            verbsToReturn[i] = new String[]{"fui", "foste", "foi", "fomos", "fostes", "foram"};
                            break;
                        case SIMP_PLUP_IND:
                            verbsToReturn[i] = new String[]{"fora", "foras", "fora", "fôramos", "fôreis", "foram"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"vá", "vás", "vá", "vamos", "vades", "vão"};
                            break;
                        case IMP_SUBJ:
                            verbsToReturn[i] = new String[]{"fosse", "fosses", "fosse", "fôssemos", "fôsseis", "fossem"};
                            break;
                        case FUT_SUBJ:
                            verbsToReturn[i] = new String[]{"for", "fores", "for", "formos", "fordes", "forem"};
                            break;
                    }
                }
                break;
            case "poder":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"posso", "podes", "pode", "podemos", "podeis", "podem"};
                            break;
                        case PRET_IND:
                            verbsToReturn[i] = new String[]{"pude", "pudeste", "pôde", "pudemos", "pudestes", "puderam"};
                            break;
                        case SIMP_PLUP_IND:
                            verbsToReturn[i] = new String[]{"pudera", "puderas", "pudera", "pudéramos", "pudéreis", "puderam"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"possa", "possas", "possa", "possamos", "possais", "possam"};
                            break;
                        case IMP_SUBJ:
                            verbsToReturn[i] = new String[]{"pudesse", "pudesses", "pudesse", "pudéssemos", "pudésseis", "pudessem"};
                            break;
                        case FUT_SUBJ:
                            verbsToReturn[i] = new String[]{"puder", "puderes", "puder", "pudermos", "puderdes", "puderem"};
                            break;
                    }
                }
                break;
            case "pôr":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"ponho", "pões", "põe", "pomos", "pondes", "põem"};
                            break;
                        case IMP_IND:
                            verbsToReturn[i] = new String[]{"punha", "punhas", "punha", "púnhamos", "púnheis", "punham"};
                            break;
                        case PRET_IND:
                            verbsToReturn[i] = new String[]{"pus", "puseste", "pôs", "pusemos", "pusestes", "puseram"};
                            break;
                        case SIMP_PLUP_IND:
                            verbsToReturn[i] = new String[]{"pusera", "puseras", "pusera", "puséramos", "puséreis", "puseram"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"ponha", "ponhas", "ponha", "ponhamos", "ponhais", "ponham"};
                            break;
                        case IMP_SUBJ:
                            verbsToReturn[i] = new String[]{"pusesse", "pusesses", "pusesse", "puséssemos", "pusésseis", "pusessem"};
                            break;
                        case FUT_SUBJ:
                            verbsToReturn[i] = new String[]{"puser", "puseres", "puser", "pusermos", "puserdes", "puserem"};
                            break;
                    }
                }
                break;
            case "querer":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"quero", "queres", "quer", "queremos", "quereis", "querem"};
                            break;
                        case PRET_IND:
                            verbsToReturn[i] = new String[]{"quis", "quiseste", "quis", "quisemos", "quisestes", "quiseram"};
                            break;
                        case SIMP_PLUP_IND:
                            verbsToReturn[i] = new String[]{"quisera", "quiseras", "quisera", "quiséramos", "quiséreis", "quiseram"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"queira", "queiras", "queira", "queiramos", "queirais", "queiram"};
                            break;
                        case IMP_SUBJ:
                            verbsToReturn[i] = new String[]{"quisesse", "quisesses", "quisesse", "quiséssemos", "quisésseis", "quisessem"};
                            break;
                        case FUT_SUBJ:
                            verbsToReturn[i] = new String[]{"quiser", "quiseres", "quiser", "quisermos", "quiserdes", "quiserem"};
                            break;
                    }
                }
                break;
            case "saber":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"sei", "sabes", "sabe", "sabemos", "sabeis", "sabem"};
                            break;
                        case PRET_IND:
                            verbsToReturn[i] = new String[]{"soube", "soubeste", "soube", "soubemos", "soubestes", "souberam"};
                            break;
                        case SIMP_PLUP_IND:
                            verbsToReturn[i] = new String[]{"soubera", "souberas", "soubera", "soubéramos", "soubéreis", "souberam"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"saiba", "saibas", "saiba", "saibamos", "saibais", "saibam"};
                            break;
                        case IMP_SUBJ:
                            verbsToReturn[i] = new String[]{"soubesse", "soubesses", "soubesse", "soubéssemos", "soubésseis", "soubessem"};
                            break;
                        case FUT_SUBJ:
                            verbsToReturn[i] = new String[]{"souber", "souberes", "souber", "soubermos", "souberdes", "souberem"};
                            break;
                    }
                }
                break;
            case "ser":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"sou", "és", "é", "somos", "sois", "são"};
                            break;
                        case IMP_IND:
                            verbsToReturn[i] = new String[]{"era", "eras", "era", "éramos", "éreis", "eram"};
                            break;
                        case PRET_IND:
                            verbsToReturn[i] = new String[]{"fui", "foste", "foi", "fomos", "fostes", "foram"};
                            break;
                        case SIMP_PLUP_IND:
                            verbsToReturn[i] = new String[]{"fora", "foras", "fora", "fôramos", "fôreis", "foram"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"seja", "sejas", "seja", "sejamos", "sejais", "sejam"};
                            break;
                        case IMP_SUBJ:
                            verbsToReturn[i] = new String[]{"fosse", "fosses", "fosse", "fôssemos", "fôsseis", "fossem"};
                            break;
                        case FUT_SUBJ:
                            verbsToReturn[i] = new String[]{"for", "fores", "for", "formos", "fordes", "forem"};
                            break;
                    }
                }
                break;
            case "ter":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"tenho", "tens", "tem", "temos", "tendes", "têm"};
                            break;
                        case IMP_IND:
                            verbsToReturn[i] = new String[]{"tinha", "tinhas", "tinha", "tínhamos", "tínheis", "tinham"};
                            break;
                        case PRET_IND:
                            verbsToReturn[i] = new String[]{"tive", "tiveste", "tive", "tivemos", "tivestes", "tiveram"};
                            break;
                        case SIMP_PLUP_IND:
                            verbsToReturn[i] = new String[]{"tivera", "tiveras", "tivera", "tivéramos", "tivéreis", "tiveram"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"tenha", "tenhas", "tenha", "tenhamos", "tenhais", "tenham"};
                            break;
                        case IMP_SUBJ:
                            verbsToReturn[i] = new String[]{"tivesse", "tivesses", "tivesse", "tivéssemos", "tivésseis", "tivessem"};
                            break;
                        case FUT_SUBJ:
                            verbsToReturn[i] = new String[]{"tiver", "tiveres", "tiver", "tivermos", "tiverdes", "tiverem"};
                            break;
                    }
                }
                break;
            case "trazer":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"trago", "trazes", "traz", "trazemos", "trazeis", "trazem"};
                            break;
                        case PRET_IND:
                            verbsToReturn[i] = new String[]{"trouxe", "trouxes", "trouxe", "trouxemos", "trouxestes", "trouxeram"};
                            break;
                        case SIMP_PLUP_IND:
                            verbsToReturn[i] = new String[]{"trouxera", "trouxeras", "trouxera", "trouxéramos", "trouxéreis", "trouxeram"};
                            break;
                        case FUT_IND:
                            verbsToReturn[i] = new String[]{"trarei", "trarás", "trará", "traremos", "trareis", "trarão"};
                            break;
                        case COND_IND:
                            verbsToReturn[i] = new String[]{"traria", "trarias", "traria", "traríamos", "traríeis", "trariam"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"traga", "tragas", "traga", "tragamos", "tragais", "tragam"};
                            break;
                        case IMP_SUBJ:
                            verbsToReturn[i] = new String[]{"trouxesse", "trouxesses", "trouxesse", "trouxéssemos", "trouxésseis", "trouxessem"};
                            break;
                        case FUT_SUBJ:
                            verbsToReturn[i] = new String[]{"trouxer", "trouxeres", "trouxer", "trouxermos", "trouxerdes", "trouxerem"};
                            break;
                    }
                }
                break;
            case "ver":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"vejo", "vês", "vê", "vemos", "vedes", "vêem"};
                            break;
                        case PRET_IND:
                            verbsToReturn[i] = new String[]{"vi", "viste", "viu", "vimos", "vistes", "viram"};
                            break;
                        case SIMP_PLUP_IND:
                            verbsToReturn[i] = new String[]{"vira", "viras", "vira", "víramos", "víreis", "viram"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"veja", "vejas", "veja", "vejamos", "vírais", "vejam"};
                            break;
                        case IMP_SUBJ:
                            verbsToReturn[i] = new String[]{"visse", "visses", "visse", "víssemos", "vísseis", "vissem"};
                            break;
                        case FUT_SUBJ:
                            verbsToReturn[i] = new String[]{"vir", "vires", "vir", "virmos", "virdes", "virem"};
                            break;
                    }
                }
                break;
            case "vir":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"venho", "vens", "vem", "vimos", "vindes", "vêm"};
                            break;
                        case IMP_IND:
                            verbsToReturn[i] = new String[]{"vinha", "vinhas", "vinha", "vínhamos", "vínheis", "vinham"};
                            break;
                        case PRET_IND:
                            verbsToReturn[i] = new String[]{"vim", "vieste", "veio", "veimos", "viestes", "vieram"};
                            break;
                        case SIMP_PLUP_IND:
                            verbsToReturn[i] = new String[]{"viera", "vieras", "viera", "viéramos", "viéreis", "vieram"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"venha", "vehnas", "venha", "venhamos", "venhais", "venham"};
                            break;
                        case IMP_SUBJ:
                            verbsToReturn[i] = new String[]{"viesse", "viesses", "viesse", "viéssemos", "viéssemos", "viessem"};
                            break;
                        case FUT_SUBJ:
                            verbsToReturn[i] = new String[]{"vier", "vieres", "vier", "viermos", "vierdes", "vierem"};
                            break;
                    }
                }
                break;
            case "crer":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"creio", "crês", "crê", "cremos", "credes", "crêem"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"creia", "creias", "creia", "creiamos", "creiais", "creiam"};
                            break;
                    }
                }
                break;
            case "ler":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"leio", "lês", "lê", "lemos", "ledes", "lêem"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"leia", "leias", "leia", "leiamos", "leiais", "leiam"};
                            break;
                    }
                }
                break;
            case "medir":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"meço", "medes", "mede", "medimos", "medis", "medem"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"meça", "meças", "meça", "meçamos", "meçais", "meçam"};
                            break;
                    }
                }
                break;
            case "ouvir":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"ouço", "ouves", "ouve", "ouvimos", "ouvis", "ouvem"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"ouça", "ouças", "ouça", "ouçamos", "ouçais", "ouçam"};
                            break;
                    }
                }
                break;
            case "pedir":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"peço", "pedes", "pede", "pedimos", "pedis", "pedem"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"peça", "peças", "peça", "peçamos", "peçais", "peçam"};
                            break;
                    }
                }
                break;
            case "perder":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"perco", "perdes", "perde", "perdemos", "perdeis", "perdem"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"perca", "percas", "perca", "percamos", "percais", "percam"};
                            break;
                    }
                }
                break;
            case "rir":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"rio", "ris", "ri", "rimos", "rides", "riem"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"ria", "rias", "ria", "riamos", "riais", "riam"};
                            break;
                    }
                }
                break;
            case "valer":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"valho", "vales", "vale", "valemos", "valeis", "valem"};
                            break;
                        case PRES_SUBJ:
                            verbsToReturn[i] = new String[]{"valha", "valhas", "valha", "valhamos", "valhais", "valham"};
                            break;
                    }
                }
                break;
            case "subir":
                for (int i = 0; i < verbForms.length; i++) {
                    VerbForm verbForm = verbForms[i];
                    switch (verbForm) {
                        case PRES_IND:
                            verbsToReturn[i] = new String[]{"subo", "sobes", "sobe", "subimos", "subis", "sobem"};
                            break;
                    }
                }
                break;
        }
    }

    public static String getGerund(String infinitive) {
        infinitive = infinitive.toLowerCase();
        int infLen = infinitive.length();
        if (!infinitive.contains(" ") && !infinitive.contains("-") && infLen > 1) {
            String ending = infinitive.substring(infLen - 2, infLen);
            if (ending.equals("ar") || ending.equals("ir") || ending.equals("er")) {
                return infinitive.substring(0, infLen - 1) + "ndo";
            } else if (infinitive.toLowerCase().equals("pôr")) {
                return "pondo";
            }
        }
        return null;
    }

    public static String getParticiple(String infinitive) {
        infinitive = infinitive.toLowerCase();
        String regParticiple = getRegularParticiple(infinitive);
        if (regParticiple == null) {
            return getIrregularParticiple(infinitive);
        }
        return regParticiple;
    }

    private static String getIrregularParticiple(String infinitive) {
        infinitive = infinitive.toLowerCase();
        switch (infinitive) {
            case "abrir":
                return "aberto";
            case "aceitar":
                return "aceito";
            case "acender":
                return "aceso";
            case "entregar":
                return "entregue";
            case "enxugar":
                return "enxuto";
            case "escrever":
                return "escrito";
            case "expulsar":
                return "expulso";
            case "ganhar":
                return "ganho";
            case "gastar":
                return "gasto";
            case "limpar":
                return "limpo";
            case "matar":
                return "morto";
            case "omitir":
                return "omisso";
            case "pagar":
                return "pago";
            case "prender":
                return "preso";
            case "romper":
                return "roto";
            case "soltar":
                return "solto";
            case "suspender":
                return "suspenso";
            case "fazer":
                return "feito";
            case "pôr":
                return "posto";
            case "ver":
                return "visto";
            case "dizer":
                return "dito";
            default:
                return null;
        }
    }

    private static String getRegularParticiple(String infinitive) {
        int infLen = infinitive.length();
        if (!infinitive.contains(" ") && !infinitive.contains("-") && infLen > 1 &&
                !infinitive.equals("abrir") &&
                !infinitive.equals("escrever") &&
                !infinitive.equals("ganhar") &&
                !infinitive.equals("gastar") &&
                !infinitive.equals("pagar") &&
                !infinitive.equals("fazer") &&
                !infinitive.equals("pôr") &&
                !infinitive.equals("ver") &&
                !infinitive.equals("dizer")) {
            String ending = infinitive.substring(infLen - 2, infLen);
            if (ending.equals("ar")) {
                return infinitive.substring(0, infLen - 1) + "do";
            } else if (ending.equals("ir") || ending.equals("er")) {
                return infinitive.substring(0, infLen - 2) + "ido";
            }
        }
        return null;
    }
}
