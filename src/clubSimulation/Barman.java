
package clubSimulation;
/* Implementation of Andre the Barman. */

public class Barman extends Thread {
    
    private PeopleLocation barmanLocation;
    private PeopleLocation customerLocation;
    private ClubGrid club;
    private GridBlock currentBlock;
    private int speed = 500;

    public Barman(ClubGrid clubGrid, PeopleLocation location){
        // Constructor initialises class instance variables
        this.club = clubGrid;
        barmanLocation = location;
        currentBlock = club.enterClubBarman(barmanLocation);
    }

    public void run(){
        while(true){
            try {
                if(customerLocation == null){
                    sleep(1000); // If no customer yet, this thread can sleep for one second. Avoids constantly spinning.
                }
                else{
                    int x_mv = Integer.signum(customerLocation.getX() - barmanLocation.getX());
                    System.out.println(x_mv);
                    if(x_mv == 0){
                        // We are done moving to the customer
                        customerLocation = null;
                        continue;
                    }
                    currentBlock = club.move(currentBlock, x_mv, 0, barmanLocation);
                    sleep(speed);
                }
            } 
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void callBarman(PeopleLocation cust) throws InterruptedException{
        this.customerLocation = cust;
        while(!(customerLocation == null)){// wait for the barman to finish serving the customer
            sleep(500);
        }
        // Done serving the customer
        this.notify(); // Let the waiting threads know they can try again
    }

    public boolean hasCustomer(){
        return !(customerLocation == null);
    }


}
