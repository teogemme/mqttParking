package spring.httpparking.controller.API;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import spring.httpparking.model.Parcheggio;
import spring.httpparking.database.Database;

@RestController
public class utenteAPI {
	
    
    private Database db;
	
	
	public utenteAPI() {
        db=new Database();
    }


	@RequestMapping("/user/api/parkings")
    public Iterable<Parcheggio> getAll() {
        return db.getAllParcheggi();
    }
	

	@RequestMapping("/api/parkings/{id}")
	public Parcheggio getParkingById(@PathVariable Integer id) {
		Parcheggio parcheggio = db.getParcheggioById(id);
		return parcheggio;
	}
}