package iShamrock.Postal.wear;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Tong on 03.06.
 */
public class ChooseActivityActivity extends Activity {
    private TextView text_0, text_1, text_2;
    private ImageView image_0, image_1, image_2;
    private ViewPager mPager;
    private List<View> listViews;
    private Socket socket;
    private DataOutputStream fromClient;

    private int activityStarted = -1;
    private List<String> postalWearDataStrings;
    private long startedTimeStamp;
    private int[] activityStartedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_activity);
        initListViews();

        postalWearDataStrings = new ArrayList<String>();
        bindActiviy();
    }

    private void initListViews() {
        mPager = (ViewPager) findViewById(R.id.vPager);

        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        listViews.add(mInflater.inflate(R.layout.page_sleeping, null));
        listViews.add(mInflater.inflate(R.layout.page_reading, null));
        listViews.add(mInflater.inflate(R.layout.page_running, null));
        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setCurrentItem(1);
    }

    private class MyPagerAdapter extends PagerAdapter {
        private final List<View> mListViews;

        public MyPagerAdapter(List<View> listViews) {
            this.mListViews = listViews;
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }
    }

    /*发送广播端的socket*/
    private MulticastSocket ms;
    /*发送广播的按钮*/
    private Button sendUDPBrocast;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {

            new Thread("ss") {
                @Override
                public void run() {
                    try {
            /*创建socket实例*/
                        ms = new MulticastSocket();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DatagramPacket dataPacket;

                    String dataString = "";
                    for (String string : postalWearDataStrings) {
                        dataString += string;
                    }
                    String host = "255.255.255.255";//广播地址
                    int port = 8003;//广播的目的端口
                    String message = dataString;//用于发送的字符串
                    try {
                        InetAddress adds = InetAddress.getByName(host);
                        DatagramSocket ds = new DatagramSocket();
                        DatagramPacket dp = new DatagramPacket(message.getBytes(),
                                message.length(), adds, port);
                        ds.send(dp);
                        ds.close();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (SocketException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }.start();
        }

        return super.onKeyDown(keyCode, event);
    }

    private void bindActiviy() {
        text_0 = (TextView) listViews.get(1).findViewById(R.id.txt_reading);
        image_0 = (ImageView) listViews.get(1).findViewById(R.id.img_reading);
        image_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activityStarted == 1) {
                    text_0.setText(getApplicationContext().getString(R.string.read));
                    image_0.setImageDrawable(getResources().getDrawable(R.drawable.icon_book_red));

                    activityStarted = -1;

                    String castedTimeString = TimeCastUtil.getCastedTime(
                            (System.currentTimeMillis() - startedTimeStamp) / 1000);
                    String castedPeriodString = TimeCastUtil.getCastedPeriod(activityStartedTime);
                    postalWearDataStrings.add("I read " + castedTimeString + castedPeriodString);
                } else if (activityStarted == -1) {
                    text_0.setText(getApplicationContext().getString(R.string.reading));
                    image_0.setImageDrawable(getResources().getDrawable(R.drawable.icon_book_white));

                    startedTimeStamp = System.currentTimeMillis();
                    activityStartedTime = new int[]{Calendar.HOUR, Calendar.MINUTE};

                    activityStarted = 1;
                }
            }
        });

        text_1 = (TextView) listViews.get(0).findViewById(R.id.txt_sleeping);
        image_1 = (ImageView) listViews.get(0).findViewById(R.id.img_sleeping);
        image_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activityStarted == 0) {
                    text_1.setText(getApplicationContext().getString(R.string.sleep));
                    image_1.setImageDrawable(getResources().getDrawable(R.drawable.icon_sleeping_red));

                    String castedTimeString = TimeCastUtil.getCastedTime(
                            (System.currentTimeMillis() - startedTimeStamp) / 1000);
                    String castedPeriodString = TimeCastUtil.getCastedPeriod(activityStartedTime);
                    postalWearDataStrings.add("I slept " + castedTimeString + castedPeriodString);

                    activityStarted = -1;
                } else if (activityStarted == -1) {
                    text_1.setText(getApplicationContext().getString(R.string.sleeping));
                    image_1.setImageDrawable(getResources().getDrawable(R.drawable.icon_sleeping_white));

                    startedTimeStamp = System.currentTimeMillis();
                    activityStartedTime = new int[]{Calendar.HOUR, Calendar.MINUTE};

                    activityStarted = 0;
                }
            }
        });

        text_2 = (TextView) listViews.get(2).findViewById(R.id.txt_running);
        image_2 = (ImageView) listViews.get(2).findViewById(R.id.img_running);
        image_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activityStarted == 2) {
                    text_2.setText(getApplicationContext().getString(R.string.run));
                    image_2.setImageDrawable(getResources().getDrawable(R.drawable.icon_running_red));

                    String castedTimeString = TimeCastUtil.getCastedTime(
                            (System.currentTimeMillis() - startedTimeStamp) / 1000);
                    String castedPeriodString = TimeCastUtil.getCastedPeriod(activityStartedTime);
                    postalWearDataStrings.add("I ran " + castedTimeString + castedPeriodString);

                    activityStarted = -1;
                } else if (activityStarted == -1) {
                    text_2.setText(getApplicationContext().getString(R.string.running));
                    image_2.setImageDrawable(getResources().getDrawable(R.drawable.icon_running_white));

                    startedTimeStamp = System.currentTimeMillis();
                    activityStartedTime = new int[]{Calendar.HOUR, Calendar.MINUTE};

                    activityStarted = 2;
                }
            }
        });
    }

}
