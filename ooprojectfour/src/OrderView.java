import javax.swing.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.sql.Date;


public class OrderView {
    private JLabel lblNumber;
    private JLabel lblDate;
    private JLabel lblCustomer;
    private JLabel lblItem;
    private JLabel lblPrice;

    private JTextField txtNumber;
    private JTextField txtDate;
    private JTextField txtPrice;

    private JComboBox cmbCustomer;
    private JComboBox cmbItem;

    private JButton btnSearch;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnBack;

    private ItemOrder activeOrder;

    private Map<String, Integer> customerNums;

    public OrderView(){
        lblNumber = new JLabel("Number");
        lblDate = new JLabel("Date");
        lblCustomer = new JLabel("Customer");
        lblItem = new JLabel("Item");
        lblPrice = new JLabel("Price");

        txtNumber = new JTextField();
        txtDate = new JTextField();
        txtPrice = new JTextField();

        cmbCustomer = new JComboBox<>();
        cmbItem = new JComboBox<>();
        cmbItem.addItem("Caesar Salad");
        cmbItem.addItem("Greek Salad");
        cmbItem.addItem("Cobb Salad");

        btnSearch = new JButton("Search");
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnBack = new JButton("Back");

        activeOrder = null;

        populateCustomers();

        setUpButtonListeners();
    }

    public JPanel GetPanel(){
        JPanel returnPanel = new JPanel();
        returnPanel.setLayout(null);

        lblNumber.setBounds(10, 10, 80, 20);
        txtNumber.setBounds(10, 30, 70, 20);

        lblDate.setBounds(850, 10, 80, 20);
        txtDate.setBounds(850, 30, 150, 20);

        lblCustomer.setBounds(10, 80, 80, 20);
        cmbCustomer.setBounds(10,100,1000,20);

        lblItem.setBounds(10,150,80,20);
        cmbItem.setBounds(10,170,600,20);

        lblPrice.setBounds(850,150,80,20);
        txtPrice.setBounds(850,170,150,20);

        btnSearch.setBounds(620, 375, 80, 20);
        btnAdd.setBounds(720, 375, 80, 20);
        btnUpdate.setBounds(820, 375, 80, 20);
        btnDelete.setBounds(920, 375, 80, 20);
        btnBack.setBounds(10, 375, 80, 20);

        returnPanel.add(lblNumber);
        returnPanel.add(txtNumber);
        returnPanel.add(lblDate);
        returnPanel.add(txtDate);
        returnPanel.add(lblCustomer);
        returnPanel.add(cmbCustomer);
        returnPanel.add(lblItem);
        returnPanel.add(cmbItem);
        returnPanel.add(lblPrice);
        returnPanel.add(txtPrice);

        returnPanel.add(btnSearch);
        returnPanel.add(btnAdd);
        returnPanel.add(btnUpdate);
        returnPanel.add(btnDelete);
        returnPanel.add(btnBack);

        return returnPanel;
    }

    @SuppressWarnings("unchecked")
    private void populateCustomers(){
        customerNums = new Hashtable<>();

        SessionFactory factory = new Configuration().
				                 configure("hibernate.cfg.xml").
				                 addAnnotatedClass(Customer.class).
				                 buildSessionFactory();
		
		Session session = factory.getCurrentSession();
		
		try {
			
			session.beginTransaction();
			
			List<Customer> customers = session.createQuery("from Customer").getResultList();
			
			for (Customer customer: customers) {
				cmbCustomer.addItem(customer.getName());
                customerNums.put(customer.getName(), customer.getID());
			}

		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			factory.close();
		}
    }

    public void setUpButtonListeners(){
        ActionListener backListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                App.orderWindow.setVisible(false);
                App.homeWindow.setVisible(true);               
            }
        };

        ActionListener addListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                activeOrder = null;

                SessionFactory factory = new Configuration().
					                 configure("hibernate.cfg.xml").
					                 addAnnotatedClass(ItemOrder.class).
					                 buildSessionFactory();
			
                Session session = factory.getCurrentSession();
                
                try {
                    
                    java.util.Date date = new SimpleDateFormat("MM/dd/yyyy").parse(txtDate.getText());
                    java.sql.Date inputDate = new java.sql.Date(date.getTime());
                    ItemOrder order = new ItemOrder(inputDate, cmbItem.getSelectedItem().toString(), Double.parseDouble(txtPrice.getText()), customerNums.get(cmbCustomer.getSelectedItem().toString()));

                    session.beginTransaction();
                    
                    session.save(order);
                    
                    session.getTransaction().commit();
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    
                } finally {
                    factory.close();
                }   
                
               JOptionPane.showMessageDialog(null, "Order successfully included");

               txtNumber.setText("");
               txtDate.setText("");
               txtPrice.setText("");
            }

        };

        ActionListener searchListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){

                if(txtNumber.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "No records found");
                    return;
                }

                SessionFactory factory = new Configuration().
                configure("hibernate.cfg.xml").
                addAnnotatedClass(ItemOrder.class).
                buildSessionFactory();

                Session session = factory.getCurrentSession();

                try {

                session.beginTransaction();

                ItemOrder order = session.get(ItemOrder.class, Integer.parseInt(txtNumber.getText()));

                if(order == null){
                    JOptionPane.showMessageDialog(null, "No records found");
                    return;
                }

                activeOrder = order;

                for (Entry<String, Integer> entry : customerNums.entrySet()){
                    if(entry.getValue() == order.getCustomer_ID()){
                        cmbCustomer.setSelectedItem(entry.getKey());
                        break;
                    }
                }
                
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

                txtDate.setText(dateFormat.format(order.getDate()));
                cmbItem.setSelectedItem(order.getItem());
                txtPrice.setText(Double.toString(order.getPrice()));

                session.getTransaction().commit();

                } catch (Exception e) {
                e.printStackTrace();

                } finally {
                factory.close();
                }               
            }
        };

        ActionListener updateListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(activeOrder == null){
                    JOptionPane.showMessageDialog(null, "No active order. Please search for order first.");
                    return;
                }

                SessionFactory factory = new Configuration().
					                 configure("hibernate.cfg.xml").
					                 addAnnotatedClass(ItemOrder.class).
					                 buildSessionFactory();
			
                Session session = factory.getCurrentSession();
                
                try {
                    
                    session.beginTransaction();
                    
                    ItemOrder order = session.get(ItemOrder.class, activeOrder.getNumber());

                    java.util.Date date = new SimpleDateFormat("MM/dd/yyyy").parse(txtDate.getText());
                    java.sql.Date inputDate = new java.sql.Date(date.getTime());

                    order.setDate(inputDate);
                    order.setItem(cmbItem.getSelectedItem().toString());
                    order.setPrice(Double.parseDouble(txtPrice.getText()));
                    order.setCustomer_ID(customerNums.get(cmbCustomer.getSelectedItem().toString()));

                    
                    session.getTransaction().commit();

                    JOptionPane.showMessageDialog(null, "Order data updated successfully");
                    
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    
                } finally {
                    factory.close();
                }   
                
                txtNumber.setText("");
                txtDate.setText("");
                txtPrice.setText("");

                activeOrder = null;
            }
        };

        ActionListener deleteListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(activeOrder == null){
                    JOptionPane.showMessageDialog(null, "No active order. Please search for order first.");
                    return;
                }

                SessionFactory factory = new Configuration().
					                 configure("hibernate.cfg.xml").
					                 addAnnotatedClass(ItemOrder.class).
					                 buildSessionFactory();
			
                Session session = factory.getCurrentSession();
                
                try {
                    
                    session.beginTransaction();
                    
                    ItemOrder order = session.get(ItemOrder.class, activeOrder.getNumber());
        
                    session.delete(order);
                    
                    session.getTransaction().commit();
                    
                    JOptionPane.showMessageDialog(null, "Order successfully deleted");
                } catch (Exception e) {
                    e.printStackTrace();
                    
                } finally {
                    factory.close();
                }  
                
                txtNumber.setText("");
                txtDate.setText("");
                txtPrice.setText("");

                activeOrder = null;
            }
        };

        btnBack.addActionListener(backListener);
        btnAdd.addActionListener(addListener);
        btnSearch.addActionListener(searchListener);
        btnUpdate.addActionListener(updateListener);
        btnDelete.addActionListener(deleteListener);
    }
}
