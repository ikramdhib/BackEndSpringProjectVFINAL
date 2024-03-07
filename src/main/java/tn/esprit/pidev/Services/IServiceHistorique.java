package tn.esprit.pidev.Services;

import tn.esprit.pidev.entities.Historique;

import java.util.List;

public interface IServiceHistorique {
    public void saveHistorique(Historique historique);
    public List<Historique> findBuUserId(String userId);
    public void delete(String historiqueId);
}
