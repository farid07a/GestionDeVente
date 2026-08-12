package ui.menufr;


import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Model_Menu {
    
    String icon;
    String name;
    MenuType type;
    

    public Model_Menu(String icon, String name, MenuType type) {
        this.icon = icon;
        this.name = name;
        this.type = type;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(MenuType type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public MenuType getType() {
        return type;
    }

    public Model_Menu() {
    }
   
    public  Icon toIcon(){
       // System.out.println("icons "+ getClass().getResource("/icon/"+icon+".png"));
        return new ImageIcon(getClass().getResource("/icon/"+icon+".png"));
        
    }
    public static enum MenuType{
        TITLE,MENU,EMPTY
    }
    
}
