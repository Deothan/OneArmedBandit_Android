package dk.deothan.onearmedbandit;

import android.app.Application;

/**
 * Created by Mads on 28-02-2016.
 */
public class BanditData extends Application {
    private int bet = 0, money = 100;

    public int getBet() { return bet; }
    public void setBet(int bet) { this.bet = bet; }
    public int getMoney() { return money; }
    public void setMoney(int money) { this.money = money; }

}
