package spring.httpparking.model;

import spring.httpparking.manager.GestoreParcheggio;

public class IotEntrata extends Thread{
    
    private final int idParcheggio;
    private GestoreParcheggio p;
    private int iot;
	

	public IotEntrata(int idParcheggio) {
        this.idParcheggio = idParcheggio;
        iot=0;
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
