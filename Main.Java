import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Trade;

import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.input.Keyboard;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.event.listeners.ChatMessageListener;
import org.rspeer.runetek.event.types.ChatMessageEvent;
import org.rspeer.runetek.event.types.ChatMessageType;

import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;


import java.util.ArrayList;
import java.util.List;

import static org.rspeer.runetek.api.component.chatter.Chat.send;

class Global{
    private static boolean var = false;

    public static boolean getVar(){
        return Global.var;
    }

    //If you do not want to change the var ever then do not include this
    public static void setVar(){
        Global.var = !var;
    }


}

@ScriptMeta(developer = "Connor", name = "Gambling Bot", desc = "A terrible gambling bot")
public class Main extends Script implements ChatMessageListener {
    Global G = new Global();
    List<String> Names = new ArrayList<String>();


    public void notify(ChatMessageEvent event) {
        if (event.getType().equals(ChatMessageType.TRADE)) {
            String name = event.getMessage().replaceAll(" wishes to trade with you.", "");
            Player toTrade = Players.getNearest(name);
            if (toTrade != null) {
                toTrade.interact("Trade with");
                Time.sleep(4000);
                Names.add(name);


            }
        }
    }


    public static final String Message = "flash1: Original Host | Roll over a 55+ | 100k Max | Game 2709";


    @Override
    public void onStart() {


    }

    @Override
    public int loop() {


        if (Trade.isOpen()) {


            Time.sleepUntil(() -> Trade.hasOtherAccepted(), 25000);
            Trade.accept();
            int Pre_Trade = Inventory.getCount(true, "Coins");
            Time.sleep(600);
            if (Trade.isOpen(true)) {
                Time.sleepUntil(() -> Trade.hasOtherAccepted(), 25000);
                Trade.accept();
                Time.sleep(500);
                int Post_Trade = Inventory.getCount(true, "Coins");
                int Amount_Given = Post_Trade - Pre_Trade;
                send("Rolling");
                int random_int = (int) (Math.random() * (100 - 0 + 1) + 0);
                Time.sleep(600);
                if (random_int > 55) {
                    send("Winner");
                    Time.sleep(600);
                    Player toTrade = Players.getNearest(Names.get(Names.size() - 1));
                    if (toTrade != null) {
                        toTrade.interact("Trade with");
                        Time.sleep(1000);
                        toTrade.interact("Trade with");
                        Time.sleep(4000);
                    }
                        if (Trade.isOpen()) {
                            Trade.offer("Coins", s -> s.matches("Offer-X"));
                            Time.sleep(1000);
                            Keyboard.sendText(Integer.toString(Amount_Given * 2));
                            Time.sleep(600);
                            Keyboard.pressEnter();
                            Time.sleep(600);
                            Trade.accept();
                            Time.sleepUntil(() -> Trade.hasOtherAccepted(), 25000);
                            Trade.accept();
                            Time.sleepUntil(() -> Trade.hasOtherAccepted(), 25000);
                            send("Congratulations you Won" + (Amount_Given * 2));
                            Time.sleep(1000);


                        }
                } else {
                        send("You rolled a " + (random_int));
                        Time.sleep(600);
                        send("Try Again");
                    }


            } else {
                Trade.decline();
            }

        }else{
            send(Message);
            Time.sleep(1000);
        }

        return Random.nextInt(400,600);
    }

    @Override
    public void onStop() {

    }
}

