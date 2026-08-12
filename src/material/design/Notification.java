/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package material.design;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
//import ui.menufr.MenuItem;

/**
 *
 * @author farid
 */
public class Notification {
    
    public static void SendNofification() throws AWTException, MalformedURLException{
    displayTray();
    }
    public static void displayTray() throws AWTException, MalformedURLException {

        //انشاء مثيل للكائن SystemTray
        SystemTray tray = SystemTray.getSystemTray();

        //وضع ايقون من ملف
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");

        //اذا كانت الايقون فوجودة فى مجلد المصادر
        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

        TrayIcon trayIcon = new TrayIcon(image, "Sauvegarde");

        //دع النظام يغير حجم الصورة إذا لزم الأمر
        trayIcon.setImageAutoSize(true);

        //وضح نص تلميح عند مرور الماوس على ايقونة 
        trayIcon.setToolTip("Succes sauvgared");
        tray.add(trayIcon);
        
        trayIcon.displayMessage("la sauvegared avec succeés", "Sauvegarde", MessageType.INFO);
    }
    
    
    
    private static SystemTray tray;
    private static TrayIcon trayIcon;

    public static void SystemTrayExample() {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }

        tray = SystemTray.getSystemTray();

        // Load your icon image here
        Image image = Toolkit.getDefaultToolkit().getImage("path/to/your/icon.png");

        // Create a popup menu for the tray icon
        PopupMenu popup = new PopupMenu();
        MenuItem exitItem = new MenuItem("Exit");
        
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        popup.add(exitItem);

        trayIcon = new TrayIcon(image, "Your Title", popup);
        trayIcon.setToolTip("");
        // Add the tray icon to the system tray
        try {
            tray.add(trayIcon);
            trayIcon.displayMessage("Message For User", "My Action ", MessageType.INFO);
            
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }

        // Add mouse listener to handle clicks on the tray icon
        trayIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // Show your custom notification window here
                // This window should mimic the behavior of a notification without a title
            }
        });
    }
    
    
    
    public static void main(String[] args) {
        //Notification.SendNofification();
        Notification.SystemTrayExample();
        
    }
    
}
