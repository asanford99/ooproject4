import javax.swing.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.awt.event.*;
import java.util.List;

public class CustomerView {
    private JLabel lblName;
    private JLabel lblPhone;
    private JLabel lblEmail;
    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtEmail;

    private JLabel lblStreet;
    private JLabel lblCity;
    private JLabel lblState;
    private JLabel lblZip;
    private JTextField txtStreet;
    private JTextField txtCity;
    private JTextField txtState;
    private JTextField txtZip;

    private JButton btnSearch;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnBack;

    private Customer activeCustomer;

    public CustomerView(){
        lblName = new JLabel("Name");
        lblPhone = new JLabel("Phone");
        lblEmail = new JLabel("Email");
        txtName = new JTextField();
        txtPhone = new JTextField();
        txtEmail = new JTextField();

        lblStreet = new JLabel("Street");
        lblCity = new JLabel("City");
        lblState = new JLabel("State");
        lblZip = new JLabel("ZIP Code");
        txtStreet = new JTextField();
        txtCity = new JTextField();
        txtState = new JTextField();
        txtZip = new JTextField();

        btnSearch = new JButton("Search");
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnBack = new JButton("Back");

        activeCustomer = null;

        setUpButtonListeners();
    }

    public JPanel GetPanel(){
        JPanel returnPanel = new JPanel();
        returnPanel.setLayout(null);

        lblName.setBounds(10, 10, 80, 20);
        txtName.setBounds(10, 30, 1000, 20);

        lblPhone.setBounds(10, 80, 80, 20);
        txtPhone.setBounds(10, 100, 300, 20);

        lblEmail.setBounds(330, 80, 80, 20);
        txtEmail.setBounds(330, 100, 680, 20);

        returnPanel.add(lblName);
        returnPanel.add(txtName);
        returnPanel.add(lblPhone);
        returnPanel.add(txtPhone);
        returnPanel.add(lblEmail);
        returnPanel.add(txtEmail);

        returnPanel.add(GetMiddlePanel());
        returnPanel.add(GetBottomPanel());

        return returnPanel;
    }

    private JPanel GetMiddlePanel(){
        JPanel returnPanel = new JPanel();
        returnPanel.setLayout(null);
        returnPanel.setBounds(10, 150, 1000, 200);
        returnPanel.setBorder(BorderFactory.createTitledBorder("Address"));

        lblStreet.setBounds(20, 30, 80, 20);
        txtStreet.setBounds(20, 50, 350, 20);

        lblState.setBounds(20, 130, 80, 20);
        txtState.setBounds(20, 150, 350, 20);

        lblCity.setBounds(550, 30, 80, 20);
        txtCity.setBounds(550, 50, 350, 20);

        lblZip.setBounds(550, 130, 80, 20);
        txtZip.setBounds(550, 150, 350, 20);

        returnPanel.add(lblStreet);
        returnPanel.add(txtStreet);
        returnPanel.add(lblState);
        returnPanel.add(txtState);
        returnPanel.add(lblCity);
        returnPanel.add(txtCity);
        returnPanel.add(lblZip);
        returnPanel.add(txtZip);

        return returnPanel;
    }

    private JPanel GetBottomPanel(){
        JPanel returnPanel = new JPanel();
        returnPanel.setLayout(null);
        returnPanel.setBounds(10, 375, 1000, 20);

        btnSearch.setBounds(620, 0, 80, 20);
        btnAdd.setBounds(720, 0, 80, 20);
        btnUpdate.setBounds(820, 0, 80, 20);
        btnDelete.setBounds(920, 0, 80, 20);
        btnBack.setBounds(10, 0, 80, 20);

        returnPanel.add(btnSearch);
        returnPanel.add(btnAdd);
        returnPanel.add(btnUpdate);
        returnPanel.add(btnDelete);
        returnPanel.add(btnBack);

        return returnPanel;
    }

    private void setUpButtonListeners(){

        ActionListener backListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                App.customerWindow.setVisible(false);
                App.homeWindow.setVisible(true);               
            }
        };

        ActionListener addListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                activeCustomer = null;
                Address address;
                Customer customer;

                SessionFactory factory1 = new Configuration().
                configure("hibernate.cfg.xml").
                addAnnotatedClass(Address.class).
                buildSessionFactory();

                SessionFactory factory2 = new Configuration().
                configure("hibernate.cfg.xml").
                addAnnotatedClass(Customer.class).
                buildSessionFactory();

                Session session1 = factory1.getCurrentSession();
                Session session2 = factory2.getCurrentSession();

                String zipText = txtZip.getText();

                if(zipText.equals("") || zipText.equals(null)){
                    address = new Address(txtStreet.getText(), txtCity.getText(), txtState.getText());
                }
                else{
                    address = new Address(txtStreet.getText(), txtCity.getText(), txtState.getText(), Integer.parseInt(txtZip.getText()));
                }

                try {
                    

                    session1.beginTransaction();

                    session1.save(address);

                    session1.getTransaction().commit();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    factory1.close();
                }

                String emailText = txtEmail.getText();

                if(emailText.equals("") || emailText.equals(null)){
                    customer = new Customer(txtName.getText(), txtPhone.getText(), address.getID());
                }
                else{
                    customer = new Customer(txtName.getText(), txtPhone.getText(), txtEmail.getText(), address.getID());
                }
                
                try {
                    session2.beginTransaction();

                    session2.save(customer);

                    session2.getTransaction().commit();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    factory2.close();
                } 

                txtName.setText("");
                txtPhone.setText("");
                txtEmail.setText("");
                txtStreet.setText("");
                txtCity.setText("");
                txtState.setText("");
                txtZip.setText("");

                JOptionPane.showMessageDialog(null, "Customer successfully included");
            }  
        };

        ActionListener searchListener = new ActionListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void actionPerformed(ActionEvent ae){
                Address address = new Address("","","");

                SessionFactory factory1 = new Configuration().
					                 configure("hibernate.cfg.xml").
					                 addAnnotatedClass(Customer.class).
					                 buildSessionFactory();
                                    
                SessionFactory factory2 = new Configuration().
					                 configure("hibernate.cfg.xml").
					                 addAnnotatedClass(Address.class).
					                 buildSessionFactory();
			
                Session session1 = factory1.getCurrentSession();
                Session session2 = factory2.getCurrentSession();
                
                try {
                    
                    session1.beginTransaction();
                    
                    List<Customer> customers = session1.createQuery("from Customer c where c.name='" + txtName.getText() + "'").getResultList();
                    
                    if(customers.isEmpty()){
                        JOptionPane.showMessageDialog(null, "No records found");
                        return;
                    }
                    else{
                        
                        try {
				
                            //Student student = new Student("john", "11111", "john@cpp.edu");
                            
                            session2.beginTransaction();
                            
                            //session.save(student);
                            
                            //Student student = session.get(Student.class, student.getId());
                            address = session2.get(Address.class, customers.get(0).getAddress_ID());

                            session2.getTransaction().commit();
                            
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                            
                        } finally {
                            factory2.close();
                        }

                        txtName.setText(customers.get(0).getName());
                        txtPhone.setText(customers.get(0).getPhone());
                        txtStreet.setText(address.getStreet());
                        txtCity.setText(address.getCity());
                        txtState.setText(address.getState());
                        txtZip.setText(Integer.toString(address.getZip_Code()));

                        if(!customers.get(0).getEmail().equals("") && !customers.get(0).getEmail().equals(null)){
                            txtEmail.setText(customers.get(0).getEmail());
                        }

                        session1.getTransaction().commit();

                        activeCustomer = customers.get(0);    
                    }
   
                } catch (Exception e) {
                    e.printStackTrace();
                    
                } finally {
                    factory1.close();
                }             
            }
        };

        ActionListener updateListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(activeCustomer == null){
                    JOptionPane.showMessageDialog(null, "No active user. Please search for user first.");
                    return;
                }

                SessionFactory factory = new Configuration().
					                 configure("hibernate.cfg.xml").
					                 addAnnotatedClass(Customer.class).
					                 buildSessionFactory();
			
                Session session = factory.getCurrentSession();
                
                try {
                    
                    session.beginTransaction();
                    
                    Customer customer = session.get(Customer.class, activeCustomer.getID());
                    
                    customer.setName(txtName.getText());
                    customer.setPhone(txtPhone.getText());
                    
                    if(!txtEmail.getText().equals("") && !txtEmail.getText().equals(null)){
                        customer.setEmail(txtEmail.getText());
                    }
                    
                    SessionFactory factory1 = new Configuration().
                                        configure("hibernate.cfg.xml").
                                        addAnnotatedClass(Address.class).
                                        buildSessionFactory();
                
                    Session session1 = factory1.getCurrentSession();
                    
                    try {
                        
                        session1.beginTransaction();
                        
                        Address address = session1.get(Address.class, customer.getAddress_ID());
                        
                        address.setStreet(txtStreet.getText());
                        address.setCity(txtCity.getText());
                        address.setState(txtState.getText());

                        if(!txtZip.getText().equals("") && !txtZip.getText().equals(null)){
                            address.setZip_Code(Integer.parseInt(txtZip.getText()));
                        }
                        
                        session1.getTransaction().commit();
                     
                     
                     } catch (Exception e) {
                         e.printStackTrace();
                         
                     } finally {
                        factory1.close();
                    }
                        
                session.getTransaction().commit();
                        
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    
                } finally {
                    factory.close();
                }    
                
                activeCustomer = null;

                JOptionPane.showMessageDialog(null, "Customer data updated successfully");

                txtName.setText("");
                txtPhone.setText("");
                txtEmail.setText("");
                txtStreet.setText("");
                txtCity.setText("");
                txtState.setText("");
                txtZip.setText("");
            }
        };

        ActionListener deleteListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(activeCustomer == null){
                    JOptionPane.showMessageDialog(null, "No active user. Please search for user first.");
                    return;
                }

                SessionFactory factory = new Configuration().
					                 configure("hibernate.cfg.xml").
					                 addAnnotatedClass(Customer.class).
					                 buildSessionFactory();
			
                Session session = factory.getCurrentSession();
                
                try {
                    
                    session.beginTransaction();
                    
                    Customer customer = session.get(Customer.class, activeCustomer.getID());
                    
                    SessionFactory factory1 = new Configuration().
                                        configure("hibernate.cfg.xml").
                                        addAnnotatedClass(Address.class).
                                        buildSessionFactory();
                
                    Session session1 = factory1.getCurrentSession();
                    
                    try {
                        
                        session1.beginTransaction();
                        
                        Address address = session1.get(Address.class, customer.getAddress_ID());
            
                        session1.delete(address);
                        
                        session1.getTransaction().commit();
                        
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        
                    } finally {
                        factory1.close();
                    }
        
                    session.delete(customer);
                    
                    session.getTransaction().commit();
                    
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    
                } finally {
                    factory.close();
                } 
                
                activeCustomer = null;

                JOptionPane.showMessageDialog(null, "Customer successfully deleted");

                txtName.setText("");
                txtPhone.setText("");
                txtEmail.setText("");
                txtStreet.setText("");
                txtCity.setText("");
                txtState.setText("");
                txtZip.setText("");
            }
        };

        btnBack.addActionListener(backListener);
        btnAdd.addActionListener(addListener);
        btnSearch.addActionListener(searchListener);
        btnUpdate.addActionListener(updateListener);
        btnDelete.addActionListener(deleteListener);
    }
}
