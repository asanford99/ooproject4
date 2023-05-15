import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MainView{
    private JButton btnCustomer;
    private JButton btnOrder;
    //private App app;

    public MainView(){
        btnCustomer = new JButton("Customer");
        btnOrder = new JButton("Order");
        
        btnCustomer.setPreferredSize(new Dimension(100, 75));
        btnOrder.setPreferredSize(new Dimension(100, 75));
        
        setUpButtonListeners();
    }

    public JPanel GetPanel(){
        JPanel returnPanel = new JPanel();

        returnPanel.add(btnCustomer);
        returnPanel.add(btnOrder);
        
        return returnPanel;
    }

    private void setUpButtonListeners(){

        ActionListener customerListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                App.homeWindow.setVisible(false);
                App.customerWindow = App.getCustomerWindow();
                App.customerWindow.setVisible(true);
            }
        };

        ActionListener orderListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                App.homeWindow.setVisible(false);
                App.orderWindow = App.getOrderWindow();
                App.orderWindow.setVisible(true);
            }
        };

        btnCustomer.addActionListener(customerListener);
        btnOrder.addActionListener(orderListener);
    }


}