/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view.departement;

import controller.ControllerDepartement;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import model.tables.ModeleDepartement;
import view.DepartementObservable;

/**
 *
 * @author Karim
 */
public class VueDepartement  extends JFrame implements DepartementObservable {

   private JFrame ajout;
    private JTable tableauDepartement;
    private ControllerDepartement monControleur;
    private JTextField tLibelle;
    private JButton bSubmit;

    public VueDepartement() {
        this.tableauDepartement = new JTable();
        this.monControleur = new ControllerDepartement(this);
        this.monControleur.remplitTableDepartement();
    }

    public ControllerDepartement getMonControleur() {
        return monControleur;
    }

    public JPanel creerPanelTableDepartement() {

        JPanel jp = new JPanel();
        jp.setLayout(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();

        gbc2.anchor = GridBagConstraints.LINE_END;
        gbc2.weightx = 1;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        this.tableauDepartement.getTableHeader().setBackground(new Color(245, 246, 247));
        this.tableauDepartement.getTableHeader().setFont(new Font("Arial", 1, 12));
        jp.add(this.tableauDepartement.getTableHeader(), gbc2);

        gbc2.anchor = GridBagConstraints.LINE_END;
        gbc2.weightx = 1;
        gbc2.weighty = 1;
        gbc2.fill = GridBagConstraints.BOTH;
        gbc2.gridx = 0;
        gbc2.gridy = 1;
        this.tableauDepartement.setIntercellSpacing(new Dimension(20, 1));
        jp.add(this.tableauDepartement, gbc2);

        JScrollPane pane = new JScrollPane(this.tableauDepartement);

        jp.add(pane, gbc2);

        return jp;

    }

    private void gereEcouteur() {
        this.bSubmit.addActionListener(monControleur);

        this.tLibelle.getDocument().addDocumentListener(this.monControleur);
    }

    @Override
    public void construitAjout() {

        ajout = new JFrame("Nouveau");
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));

        JPanel pSaisie = this.construitPanelSaisieDepartement();
        JPanel pValidation = this.construitPanelButtons();

        p.add(pSaisie);
        p.add(pValidation);
        this.gereEcouteur();
        ajout.add(p);
        ajout.setSize(400, 100);
        ajout.setResizable(false);
        ajout.setLocation(450, 300);
        ajout.setVisible(true);

    }

    private JPanel construitPanelSaisieDepartement() {

        JPanel panelAjout = new JPanel();

        JLabel lLibelle = new JLabel("Nom du département : ");
        this.tLibelle = new JTextField(255);

        panelAjout.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 2, 10);

        panelAjout.add(lLibelle, gbc);

        gbc.ipady = 4;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 0;
        panelAjout.add(this.tLibelle, gbc);

        return panelAjout;

    }

    private JPanel construitPanelButtons() {

        JPanel panelButton = new JPanel();

        panelButton.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 2, 10);
        JButton bCancel = new JButton("Annuler");
        panelButton.add(bCancel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 2, 10);
        this.bSubmit = new JButton("Ajouter");
        this.bSubmit.setEnabled(false);
        panelButton.add(this.bSubmit, gbc);

        return panelButton;
    }

    @Override
    public void remplitTable(ModeleDepartement modele) {
        this.tableauDepartement.setModel(modele);
    }

    @Override
    public String getLibelle() {
        return tLibelle.getText();
    }

    @Override
    public void activeButton() {
        this.bSubmit.setEnabled(this.tLibelle.getText().trim().length() > 0);
    }

    @Override
    public void close() {
        ajout.dispose();
    }


}
