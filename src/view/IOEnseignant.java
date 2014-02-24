package view;

import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import model.business.Departement;

public interface IOEnseignant extends IObservable {

    public void setListeDepartement(ArrayList<Departement> liste);

    public Departement getDptSelected();

    public void filtrer(String[] search, String dpt);

}
