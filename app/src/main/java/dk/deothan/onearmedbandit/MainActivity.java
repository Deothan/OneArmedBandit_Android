package dk.deothan.onearmedbandit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;

public class MainActivity extends Activity {
    ImageView image1, image2, image3;
    Button stop1, stop2, stop3, roll;
    TextView money, bet;
    BanditData banditData;
    MyHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        banditData = (BanditData)getApplication();

        image1  = (ImageView) findViewById(R.id.image1);
        image2  = (ImageView) findViewById(R.id.image2);
        image3  = (ImageView) findViewById(R.id.image3);

        stop1 = (Button) findViewById(R.id.stop1);
        stop1.setAlpha(0);
        stop1.setEnabled(false);

        stop2 = (Button) findViewById(R.id.stop2);
        stop2.setAlpha(0);
        stop2.setEnabled(false);

        stop3 = (Button) findViewById(R.id.stop3);
        stop3.setAlpha(0);
        stop3.setEnabled(false);

        roll = (Button) findViewById(R.id.roll);

        money = (TextView) findViewById(R.id.currentMoney);
        bet = (TextView) findViewById(R.id.currentBet);

        handler = new MyHandler();

        updateTextViews();
    }

    @Override
    public void onResume(){
        super.onResume();
        updateTextViews();
    }

    private void update(){
        if(stop1.getAlpha() + stop2.getAlpha() + stop3.getAlpha()  < 1){
            changeButtons();
            checkWinning();
        }

        updateTextViews();
    }

    public void stopBtnClicked(View view) {
        switch (view.getId()){
            case R.id.stop1:
                ((AnimationDrawable) image1.getBackground()).stop();
                stop1.setAlpha(0);
                stop1.setEnabled(false);
                break;

            case R.id.stop2:
                ((AnimationDrawable) image2.getBackground()).stop();
                stop2.setAlpha(0);
                stop2.setEnabled(false);
                break;

            case R.id.stop3:
                ((AnimationDrawable) image3.getBackground()).stop();
                stop3.setAlpha(0);
                stop3.setEnabled(false);
                break;

            default:
                System.out.println("Button not found!");
                break;
        }

        update();
    }

    public void rollBthClicked(View view) {
        stop1.setAlpha(1);
        stop1.setEnabled(true);
        stop2.setAlpha(1);
        stop2.setEnabled(true);
        stop3.setAlpha(1);
        stop3.setEnabled(true);

        ((AnimationDrawable) image1.getBackground()).start();
        ((AnimationDrawable) image2.getBackground()).start();
        ((AnimationDrawable) image3.getBackground()).start();

        roll.setAlpha(0);
        roll.setEnabled(false);

        new Thread(new Runnable() {
            public void run() {
                try{
                    synchronized (this) {
                        wait(10000);
                        handler.dispatchMessage(new Message());
                        wait(3000);
                        handler.dispatchMessage(new Message());
                        wait(2000);
                        handler.dispatchMessage(new Message());
                    }
                }
                catch (InterruptedException e) { e.printStackTrace(); }
            }
        }).start();
    }

    // android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            if(((AnimationDrawable) image1.getBackground()).isRunning()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stop1.performClick();
                    }
                });
            }
            else if(((AnimationDrawable) image2.getBackground()).isRunning()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stop2.performClick();
                    }
                });
            }
            else if(((AnimationDrawable) image3.getBackground()).isRunning())
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stop3.performClick();
                    }
                });
        }
    }

    public void changeBetBthClicked(View view) {
        Intent intent = new Intent(this, BetActivity.class);
        startActivity(intent);
    }

    private void changeButtons(){
        roll.setAlpha(1);
        roll.setEnabled(true);
    }

    private void checkWinning(){
        Bitmap pic1 = ((BitmapDrawable)image1.getBackground().getCurrent()).getBitmap();
        Bitmap pic2 = ((BitmapDrawable)image2.getBackground().getCurrent()).getBitmap();
        Bitmap pic3 = ((BitmapDrawable)image3.getBackground().getCurrent()).getBitmap();

        if(pic1.equals(pic2) && pic2.equals(pic3))
            calculateWinning(3);
        else if(pic1.equals(pic2) || pic2.equals(pic3) || pic1.equals(pic3) || pic2.equals(pic3))
            calculateWinning(2);
        else
            calculateWinning(1);
    }

    private void calculateWinning(int same){
        if(same == 1)
            banditData.setMoney(banditData.getMoney() - banditData.getBet());
        else if(same == 2)
            banditData.setMoney((banditData.getBet() * 5) + banditData.getMoney());
        else
            banditData.setMoney((banditData.getBet() * 50) + banditData.getMoney());
    }

    private void updateTextViews(){
        bet.setText(Integer.toString(banditData.getBet()));
        money.setText(Integer.toString(banditData.getMoney()));
    }
}
