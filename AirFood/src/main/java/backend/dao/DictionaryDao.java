package backend.dao;

import backend.model.Dictionary;

public class DictionaryDao {

    public Dictionary getAllData(){
        Dictionary dictionary = new Dictionary();
        dictionary.setAirports(new AirportDao().getAllAirports());
        dictionary.setAirlines(new AirlineDao().getAllAirlines());
        dictionary.setBoards(new BoardDao().getAllBoards());
        dictionary.setFlights(new FlightDao().getAllFlights());
        return dictionary;
    }
}
