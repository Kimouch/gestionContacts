package model.dao.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.business.Formation;
import model.business.Manifestation;

import model.dao.DAOInterface;
import model.dao.XMLChemin;
import model.dao.sql.SQLDepartementDAO;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.*;

public class XMLFormationDAO implements DAOInterface<Formation> {

    private static XMLFormationDAO instance = null;

    public static XMLFormationDAO getInstance() {
        if (XMLFormationDAO.instance == null) {
            XMLFormationDAO.instance = new XMLFormationDAO();
        }
        return XMLFormationDAO.instance;
    }

    private final XMLChemin chemin;
    private final String nomFichier;
    private Element racine;

    public String getNomFicher() {
        return nomFichier;
    }

    public Element getRacine() {
        return racine;
    }

    public XMLFormationDAO() {
        this.chemin = new XMLChemin();
        this.nomFichier = "formations.xml";
    }

    @Override
    public ArrayList<Formation> readAll() {
        ArrayList<Formation> liste = new ArrayList<>();

        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new FileInputStream(this.chemin.getChemin() + "/" + this.nomFichier));
            Element root = doc.getRootElement();

            List list = root.getChildren();

            for (int i = 0; i < list.size(); i++) {
                Element node = (Element) list.get(i);
                liste.add(new Formation(node.getAttribute("id").getIntValue(),
                        node.getChildText("libelle_form"),
                        SQLDepartementDAO.getInstance().readById(Integer.parseInt(node.getChildText("id_dpt")))));
            }

        } catch (IOException | JDOMException io) {
            System.out.println(io.getMessage());
        }
        return liste;
    }

    @Override
    public int create(Formation formation) {

        racine = new Element("formations");
        Element form = new Element("formation");

        Attribute id = new Attribute("id", Integer.toString(formation.getIdFormation()));
        form.setAttribute(id);
        racine.addContent(form);

        Element libelle_form = new Element("libelle_form");
        libelle_form.setText(formation.getLibelleFormation());
        form.addContent(libelle_form);

        Element id_dpt = new Element("id_dpt");
        id_dpt.setText(Integer.toString(formation.getDepartement().getIdDepartement()));
        form.addContent(id_dpt);
        this.sauvegarde();

        return 1;
    }

    private void sauvegarde() {
        try {
            XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
            sortie.output(this.racine, new FileOutputStream(this.chemin.getChemin() + "/" + this.nomFichier));
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void ajouter(Formation formation) {
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(this.chemin.getChemin() + "/" + this.getNomFicher());

        try {
            Document doc = (Document) builder.build(xmlFile);
            Element root = doc.getRootElement();

            Element form = new Element("formation");
            Attribute id = new Attribute("id", Integer.toString(formation.getIdFormation()));
            form.setAttribute(id);
            root.addContent(form);

            Element libelle_form = new Element("libelle_form");
            libelle_form.setText(formation.getLibelleFormation());
            form.addContent(libelle_form);

            Element id_dpt = new Element("id_dpt");
            id_dpt.setText(Integer.toString(formation.getDepartement().getIdDepartement()));
            form.addContent(id_dpt);

            XMLOutputter outputter1 = new XMLOutputter(Format.getPrettyFormat());
            outputter1.output(doc, new FileWriter(this.chemin.getChemin() + "/" + this.getNomFicher()));

        } catch (IOException | JDOMException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean update(Formation formation) {
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(this.chemin.getChemin() + "/" + this.getNomFicher());

        try {
            Document doc = (Document) builder.build(xmlFile);
            Element root = doc.getRootElement();

            String numForm = Integer.toString(formation.getIdFormation());

            for (Element element : root.getChildren()) {

                if (numForm.equals(element.getAttributeValue("id"))) {
                    element.getChild("libelle_form").setText(formation.getLibelleFormation());
                    break;
                }

            }

            XMLOutputter outputter1 = new XMLOutputter(Format.getPrettyFormat());
            outputter1.output(doc, new FileWriter(this.chemin.getChemin() + "/" + this.getNomFicher()));

        } catch (IOException | JDOMException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Formation formation) {
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(this.chemin.getChemin() + "/" + this.getNomFicher());

        try {
            Document doc = (Document) builder.build(xmlFile);
            Element root = doc.getRootElement();

            String numForm = Integer.toString(formation.getIdFormation());

            for (Element element : root.getChildren()) {

                if (numForm.equals(element.getAttributeValue("id"))) {
                    root.removeContent(element);
                    break;
                }
            }

            XMLOutputter outputter1 = new XMLOutputter(Format.getPrettyFormat());
            outputter1.output(doc, new FileWriter(this.chemin.getChemin() + "/" + this.getNomFicher()));

        } catch (IOException | JDOMException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void deleteList(ArrayList<Formation> liste) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void creerListe(ArrayList<Formation> liste) {
        this.sauvegarde();
        for (Formation formation : liste) {
            this.ajouter(formation);
        }
    }
}
