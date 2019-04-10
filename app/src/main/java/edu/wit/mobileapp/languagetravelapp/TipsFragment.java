package edu.wit.mobileapp.languagetravelapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;

public class TipsFragment extends Fragment {
    private static String TAG = "myTips";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.travel_fragment_tips, container, false);
        // Inflate the layout for this fragment
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_list_item_1);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String Location = prefs.getString("country", "Portugal");
        ArrayList<String> tips;
        tips = getData(Location);
        for(int i = 0; i<tips.size(); i++){
            adapter.add(tips.get(i));
        }
        ListView listView = (ListView)view.findViewById(R.id.ListViewTips);
        listView.setAdapter(adapter);


        return view;
    }

    public ArrayList<String> getData(String Location){
        ArrayList<String> data = new ArrayList<String>();
        switch (Location) {
            case "Portugal":
                data = setPortugal();
                break;
            case "France":
                data = setFrance();
                break;
            case "Brazi":
                data = setBrazil();
                break;
            default:
                data.add("");
                Log.v(TAG,"default");
                break;
        }
        return data;
    }

    public ArrayList<String> setPortugal(){
        ArrayList<String> data = new ArrayList<String>();
        data.add("Family in Portugal:\n" +"In Portugal, the family is the foundation of the social structure. Individuals derive a social network and assistance from the family.");
        data.add("Portuguese greetings:\n" +"It is appropriate to shake hands with everyone present in formal situations, which generally means that you haven’t met the person before; this applies to men, women and older children.");
        data.add("Body language:\n" +"The Portuguese do not use a lot of gestures. Being overly demonstrative with hand gestures or body language gives a bad impression.");
        data.add("Communication style:\n" +"The Portuguese tend to be direct in their communication style. Usually they will tell you the truth but in a polite manner");
        data.add("Portuguese dress code:\n" +"The Portuguese dress conservatively. Women usually wear dresses, and men’s clothing is based primarily around a jacket and tie. Business etiquette dictates suits and ties or sports jackets and ties for men. Women wear dresses, skirts and jackets or trouser suits.");
        data.add("Personal space in Portugal:\n" +"An arm’s length is usually the appropriate amount of personal space to hold during conversations. The Portuguese tend to touch a bit when conversing with good friends and family, but such displays are quite inappropriate in business or formal situations.");
        data.add("Gift giving etiquette:\n" +"When invited to a Portuguese home for dinner, bring flowers, good quality chocolates or candy for the hostess. Do not bring wine unless you know which wines your hosts prefer.");
        data.add("Dining etiquette:\n" +"If you are invited to a dinner, try to arrive no more than 15 minutes after the stipulated time. Being late between half an hour and an hour is accepted when you go to a party or larger social gathering.");
        data.add("Expat women in Portugal:\n" +"Foreign business women are treated with respect. Keep in mind that going to a bar alone is frowned upon. It may attract unwelcome attention.");
        return data;
    }
    public ArrayList<String> setFrance(){
        ArrayList<String> data = new ArrayList<String>();
        data.add("Joie de vivre:\n" +"In France, quality of life is important. The French don’t walk around carrying a takeaway Starbucks. They’ll sit in their local café with an espresso instead. People will take a lunch break and it’s perfectly normal to go to a restaurant and even drink a glass of wine.");
        data.add("Formality:\n" +"Forms of address are much more formal in France than in the USA, face to face, in email correspondence and on the phone. In offices, superiors are referred to as Monsieur or Madame by low-ranking workers.");
        data.add("Identity:\n" +"Americans often define themselves by their profession and will put people in context according to their job and where they come from. Seeking common ground is important; profession and self-identity are inextricably linked. ");
        data.add("Building relationships:\n" +"Building a relationship in France is important but can take time. It is not common to discuss your personal life and family with a stranger in France.");
        data.add("Security:\n" +"In France, people are more likely to aspire to working for a large company or the family business. Being an entrepreneur is not seen as prestigious, unless you are incredibly successful. Job security is valued, as are the perks that come with long-term service like a decent pension.");
        data.add("Confrontation, feedback and firing:\n" +"In France, confrontation and open debate are seen as positives and perfectly normal. Conversations can appear heated as views are exchanged. Feedback can appear harsh; a French superior is more likely to talk about an individual’s failings than their successes.");
        data.add("Fitting in:\n" +"France is very conformist. Visitors and expatriates are expected to speak French and adopt French habits and customs. In the workplace and in daily life, solidarité – sharing a viewpoint – is important.");
        data.add("Love of process:\n" +"French executives tend to question everything and go into detail over theory, concepts and methodology, while Americans are more likely to make a statement, have their audience accept it and move on to the next point.");
        data.add("Authority:\n" +"People at all levels of society tend to be politically engaged in France. Lives at every economic level are affected by the government, as education and healthcare are free and the state pension relatively generous. ");
        data.add("Smoking:\n" +"Smoking is much more prevalent in France than in the USA. Although it’s banned in restaurants and cafes, a lot of French people smoke outside, or on outdoor terraces of restaurants, which visiting Americans can find surprising and offensive. ");
        data.add("Service:\n" +"In the USA, restaurant waiters are generally low paid and depend on tips to make up their income. They’re usually attentive and chatty, sometimes overly so, and in some instances, will take offence if you don’t tip generously.");
        return data;
    }

    public ArrayList<String> setBrazil(){
        ArrayList<String> data = new ArrayList<String>();
        data.add("Sense of Space:\n" +
                "Brazilians, as other Latino cultures, are very friendly and affectionate towards each other. They will greet you with a kiss on the cheek and will come really close while they talk to you. The concept of personal space is not as common as in the US. Although it might be shocking at first for a foreigner, it’s also nice to feel from the very beginning you meet them.");
        data.add("Time:\n" + "A lot of Brazilians don’t have a sense of hurry and are more patient than Americans. Their concept of time is different, so a lot of people are not as punctual as in the US. For example, some professors and students may come 15 to 20 minutes after class was supposed to start.");
        data.add("Free Health Care:\n" +"Isn’t this amazing? And it covers foreigners too!! Contrary to the US, in Brazil you don’t need any type of health care plan, you can just go to a public hospital or doctor and they will attend you free of charge.");
        data.add("Teeth Are Important:\n" + "Most Brazilians have a toothbrush and toothpaste in their bag to take it whenever they go. So don’t be weirded out if there is someone in the public restroom brushing their teeth after they had a meal.");
        data.add("No Tipping:\n" +"In the US, although not mandatory, most people feel obligated to pay a tip for some services. Here in Brazil, people usually don’t tip for anything. Some restaurants might charge a service fee, but most people (taxi driver, delivery man, etc.) won’t expect anything from you.");
        data.add("Lunch & Dinner:\n" +"Lunch is the most important meal of the day for Brazilians. It’s also more common to have dinner at around 9pm, so don’t be surprised if a restaurant is completely empty at 6pm.");
        data.add("Lanches:\n" +"These small restaurants are my favorite because they are one of the cheapest option, the food is delicious and you get to eat a lot. They usually sell rice, beans and your choice of meat or chicken.");
        return data;
    }

}
