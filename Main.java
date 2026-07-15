 class Chef implements Runnable{
    private Kitchen kitchen;
    public Chef(Kitchen kitchen){
        this.kitchen = kitchen;
    }
    public void run(){
        for(int i=1;i<=5;i++){
            Order order = kitchen.takeOrder();
            System.out.println("chef is preparing the food"+order);
            try{
               Thread.sleep(1000); 
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
class Customer implements Runnable{
    private Kitchen kitchen;
    public Customer(Kitchen kitchen){
        this.kitchen = kitchen;
    }
    public void run(){
        for(int i=1;i<=5;i++){
            Order order = new Order(i,"biryani");
            kitchen.addOrder(order);
            try{
                Thread.sleep(500);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
import java.util.*;
 class Kitchen {
     private Queue<Order> q = new LinkedList<>();
     private int capacity = 3;
    public synchronized void addOrder(Order order){
        while(q.size()==capacity){
            try{
                wait();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
            q.add(order);
            System.out.println("customer added :"+order);
            notify();
            }
            
    public synchronized Order takeOrder(){
        while(q.isEmpty()) {

            try {
                wait();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }


        Order order = q.remove();

        notify();

        return order;
    }
    }
class Order{
    private int orderId;
    private String foodName;
    Order(int orderId,String foodName){
        this.orderId = orderId;
        this.foodName = foodName;
    }
    public String toString(){
        return "order "+orderId+":"+foodName;
    }
}
public class Main{
    public static void main(String[] args) throws InterruptedException{
        Kitchen kitchen  = new Kitchen();
        Thread t1 = new Thread(new Customer(kitchen));
        Thread chef = new Thread(new Chef(kitchen));
        t1.start();
        chef.start();
        t1.join();
        chef.join();
        System.out.println("restaurant closed");
    }
}
