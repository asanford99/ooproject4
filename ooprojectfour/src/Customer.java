import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Customer {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="phone")
    private String phone;

    @Column(name="email")
    private String email;

    @Column(name="address_id")
    private int address_id;

    public Customer(){

    }

    public Customer(String name, String phone, int address_id){
        this.name = name;
        this.phone = phone;
        this.address_id = address_id;
    }

    public Customer(String name, String phone, String email, int address_id){
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address_id = address_id;
    }

    public int getID(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getPhone(){
        return phone;
    }

    public String getEmail(){
        if(email == null){
            return "";
        }
        else{
            return email;
        }
    }

    public int getAddress_ID(){
        return address_id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public void setEmail(String email){
        this.email = email;
    }
}
