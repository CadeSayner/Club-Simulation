
package clubSimulation;

//This class represents the club as a grid of GridBlocks
public class ClubGrid {
	private GridBlock [][] Blocks;
	private final int x;
	private final int y;
	public  final int bar_y;
	
	private GridBlock exit;
	private GridBlock entrance; //hard coded entrance
	private final static int minX =5;//minimum x dimension
	private final static int minY =5;//minimum y dimension
	
	private PeopleCounter counter;
	
	ClubGrid(int x, int y, int [] exitBlocks,PeopleCounter c) throws InterruptedException {
		if (x<minX) x=minX; //minimum x
		if (y<minY) y=minY; //minimum x
		this.x=x;
		this.y=y;
		this.bar_y=y-3;
		Blocks = new GridBlock[x][y];
		this.initGrid(exitBlocks);
		entrance=Blocks[getMaxX()/2][0];
		counter=c;
		}
	
	//initialise the grsi, creating all the GridBlocks
	private  void initGrid(int []exitBlocks) throws InterruptedException {
		for (int i=0;i<x;i++) {
			for (int j=0;j<y;j++) {
				boolean exit_block=false;
				boolean bar=false;
				boolean dance_block=false;
				if ((i==exitBlocks[0])&&(j==exitBlocks[1])) {exit_block=true;}
				else if (j>=(y-3)) bar=true; 
				else if ((i>x/2) && (j>3) &&(j< (y-5))) dance_block=true;
				//bar is hardcoded two rows before  the end of the club
				Blocks[i][j]=new GridBlock(i,j,exit_block,bar,dance_block);
				if (exit_block) {this.exit = Blocks[i][j];}
			}
		}
	}
	
		public  int getMaxX() {
		return x;
	}
	
		public int getMaxY() {
		return y;
	}

	public GridBlock whereEntrance() { 
		return entrance;
	}

	public  boolean inGrid(int i, int j) {
		if ((i>=x) || (j>=y) ||(i<0) || (j<0)) 
			return false;
		return true;
	}
	
	public  boolean inPatronArea(int i, int j) {
		if ((i>=x) || (j>bar_y) ||(i<0) || (j<0)) 
			return false;
		return true;
	}

	public GridBlock enterClubBarman(PeopleLocation barmanLocation){
		barmanLocation.setLocation(Blocks[5][8]);
		return Blocks[5][8];
	}
	
	public GridBlock enterClub(PeopleLocation myLocation) throws InterruptedException  { // More than one thread should not be able to enter the club at the same time
		counter.personArrived(); //add to counter of people waiting 
		synchronized(entrance){
			while(!entrance.get(myLocation.getID()) || counter.getInside() >= counter.getMax()){
				// Wait while either the entrance is occupied or the club is too full
				entrance.wait();
			}
			// Entrance is clear and can move there
			counter.personEntered(); //add to counter
			myLocation.setLocation(entrance);
			myLocation.setInRoom(true); // will immediately have to wait if can't get in but just to be safe
			return entrance;
		}
	}
	
	
	public GridBlock move(GridBlock currentBlock,int step_x, int step_y,PeopleLocation myLocation) throws InterruptedException {  //try to move in 
		int c_x= currentBlock.getX();
		int c_y= currentBlock.getY();
		
		int new_x = c_x+step_x; //new block x coordinates
		int new_y = c_y+step_y; // new block y  coordinates
		
		//restrict i an j to grid
		if (!inPatronArea(new_x,new_y) && c_y != 8) { // Check that not barman here
			//Invalid move to outside  - ignore
			return currentBlock;
		}

		if ((new_x==currentBlock.getX())&&(new_y==currentBlock.getY())) //not actually moving
		return currentBlock;
		
		GridBlock newBlock = Blocks[new_x][new_y]; // What happens if two threads want to move to the same empty block, need synchronization
	
		// We know that newBlock is atomic and won't allow simultaneous access, no two threads will both get a true.
		if (!newBlock.get(myLocation.getID())) return currentBlock; //stay where you are, prevents two barGoers from entering the same block, including the exit block, // Not compound since not doing anything else with the object, open for other threads after this.
		currentBlock.release(); //must release current block, would release the entrance if that is where it was should not be a data race since will never be called by different threads on the same object
		myLocation.setLocation(newBlock);
		
		// If it gets here defintely moved somewhere so if we started at the entrance we can let the waiting threads that they can try again.
		if(currentBlock == entrance){
			synchronized(entrance){
				entrance.notifyAll();
			}
		}
		
		return newBlock;
	} 
	

	public  void leaveClub(GridBlock currentBlock,PeopleLocation myLocation)   {
			currentBlock.release();
			counter.personLeft(); //add to counter
			myLocation.setInRoom(false);
			synchronized(entrance){ // Need to obtain the entrance lock to notify other threads and avoid exception being thrown
				entrance.notifyAll();
			}
	}

	public GridBlock getExit() {
		return exit;
	}

	public GridBlock whichBlock(int xPos, int yPos) {
		if (inGrid(xPos,yPos)) {
			return Blocks[xPos][yPos];
		}
		System.out.println("block " + xPos + " " +yPos + "  not found");
		return null;
	}
	
	public void setExit(GridBlock exit) {
		this.exit = exit;
	}

	public int getBar_y() {
		return bar_y;
	}

}


	

	

