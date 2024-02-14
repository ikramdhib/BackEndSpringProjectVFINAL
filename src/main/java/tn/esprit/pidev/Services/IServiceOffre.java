package tn.esprit.pidev.Services;

import tn.esprit.pidev.entities.Offre;

import java.util.List;

public interface IServiceOffre {

    public Offre addOffre(Offre o);
    public Offre updateOffre(Offre o);
    public List<Offre> getAllOffre();
    public Offre getOffreById(String id);
    public void deleteOffre(String id);
}
