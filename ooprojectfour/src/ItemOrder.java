import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class ItemOrder {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="number")
    private int number;

    @Column(name="date")
    private Date date;

    @Column(name="item")
    private String item;

    @Column(name="price")
    private double price;

    @Column(name="customer_id")
    private int customer_id;

    public ItemOrder(){

    }

    public ItemOrder(Date date, String item, double price, Integer customer_id){
        this.date = date;
        this.item = item;
        this.price = price;
        this.customer_id = customer_id;
    }

    public int getNumber(){
        return number;
    }

    public Date getDate(){
        return date;
    }

    public String getItem(){
        return item;
    }

    public double getPrice(){
        return price;
    }

    public int getCustomer_ID(){
        return customer_id;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public void setItem(String item){
        this.item = item;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void setCustomer_ID(int customer_id){
        this.customer_id = customer_id;
    }
}
