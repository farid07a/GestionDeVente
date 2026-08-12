/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Clock {

    private Timer timer;

    public void start(JLabel lblTime, JLabel lblDate) {

        timer = new Timer(1000, e -> {

            Date now = new Date();

            SimpleDateFormat timeFormat =
                    new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat dateFormat =
    new SimpleDateFormat("EEEE yyyy/MM/dd", new Locale("ar", "DZ"));

//            SimpleDateFormat dateFormat =
//                    new SimpleDateFormat("EEEE dd/MM/yyyy", new Locale("ar"));

//             "EEEE dd MMMM yyyy",
//        new Locale("ar")
            lblTime.setText(timeFormat.format(now));
            lblDate.setText(dateFormat.format(now));

        });

        timer.setInitialDelay(0);
        timer.start();
    }

    public void stop() {
        if (timer != null) {
            timer.stop();
        }
    }
}
