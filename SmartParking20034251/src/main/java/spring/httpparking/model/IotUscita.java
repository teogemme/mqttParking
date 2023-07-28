package spring.httpparking.model;


import spring.httpparking.manager.GestoreParcheggio;


public class IotUscita extends Thread{
    
    private final int idParcheggio;
    private GestoreParcheggio p;
    private int iot;
	

	public IotUscita(int idParcheggio) {
        this.idParcheggio = idParcheggio;
        iot=2;
    }

    public void setParkingService(GestoreParcheggio parkingService) {
	    this.p = parkingService;
	}
	

    public void run() {
        try {
			p.MessaggioParcheggio(idParcheggio,iot);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}


