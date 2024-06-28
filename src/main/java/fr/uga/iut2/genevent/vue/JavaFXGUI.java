package fr.uga.iut2.genevent.vue;

import fr.uga.iut2.genevent.controleur.Controleur;
import fr.uga.iut2.genevent.util.Utilitaire;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * La classe JavaFXGUI est responsable des interactions avec
 * l'utilisa·teur/trice en mode graphique.
 * <p>
 * Attention, pour pouvoir faire le lien avec le
 * {@link fr.uga.iut2.genevent.controleur.Controleur}, JavaFXGUI n'est pas une
 * sous-classe de {@link javafx.application.Application} !
 * <p>
 * Le démarrage de l'application diffère des exemples classiques trouvés dans
 * la documentation de JavaFX : l'interface est démarrée à l'initiative du
 * {@link fr.uga.iut2.genevent.controleur.Controleur} via l'appel de la méthode
 * {@link #demarrerInteraction()}.
 */
public class JavaFXGUI extends IHM {

    private static Logger LOGGER = Logger.getLogger(JavaFXGUI.class.getPackageName());

    private final Controleur controleur;
    private final CountDownLatch eolBarrier;  // /!\ ne pas supprimer /!\ : suivi de la durée de vie de l'interface

    private Stage mainStage;

    private static InfosRepresentation tempRep;
    // --- Eléments fxml ---
    // éléments de la page login.fxml
    @FXML private TextField tfEmailConnexion;
    @FXML private PasswordField pfMDPConnexion;
    @FXML private Label lbErreurConnexion;

    @FXML private TextField tfNomComplet;
    @FXML private TextField tfEmailInscription;
    @FXML private TextField tf1MdpInscription;
    @FXML private TextField tf2MdpInscription;
    @FXML private PasswordField pf1MdpInscription;
    @FXML private PasswordField pf2MdpInscription;
    @FXML private CheckBox cbxAfficherPass;

    @FXML private Button btnSinscrire;
    @FXML private Label lbErreurMdp;

    // éléments du menu latéral se trouvant sur les pages reservation.fxml, reservationRecap.fxml, theaters.fxml, agenda.fxml et pieces.fxml
    @FXML private Label lbSalles;
    @FXML private Label lbAgenda;


    // éléments de la page reservation.fxml
    @FXML private ComboBox<String> comboBoxSalles;
    @FXML private DatePicker date;
    @FXML private RadioButton radioBtnGaucheRegieLumiere; @FXML private RadioButton radioBtnDroiteRegieLumiere;
    @FXML private RadioButton radioBtnGaucheRegieSon; @FXML private RadioButton radioBtnDroiteRegieSon;
    @FXML private RadioButton radioBtnGaucheSecurite; @FXML private RadioButton radioBtnDroiteSecurite;
    @FXML private RadioButton radioBtnGauchePublicite; @FXML private RadioButton radioBtnDroitePublicite;
    @FXML private ComboBox<String> comboBoxPieces;

    @FXML private Label lbErreurDate;
    @FXML private Label lbChampsManquants;
    @FXML private Button btnSuivantReservation;

    // éléments de la page réservationRecap.fxml
    @FXML private TextField tfSalleReservationRecap;
    @FXML private TextField tfDateReservationRecap;
    @FXML private TextField tfPieceReservationRecap;
    @FXML private Label lbRegieLumiereReservationRecap;
    @FXML private Label lbRegieSonReservationRecap;
    @FXML private Label lbSecuriteReservationRecap;
    @FXML private Label lbPubliciteReservationRecap;
    
    // éléments de la page theaters.fxml
    @FXML VBox vbListeSalles;
    
    // éléments de la page agenda.fxml
    @FXML VBox vbListeRepresentations;

    // éléments de la page pieces.fxml
    @FXML private VBox vbListePieces;
    @FXML private Button btnAjoutePiece;
    

    // éléments de la page play-form.fxml
    @FXML private TextField tfNomPiece;
    @FXML private TextField tfDureePiece;
    @FXML private TextArea taResumeePiece;
    @FXML private HBox hbModifPiece;


    // --- Constructeur ---
    public JavaFXGUI(Controleur controleur) {
        this.controleur = controleur;
        this.eolBarrier = new CountDownLatch(1);  // /!\ ne pas supprimer /!\
    }

    /**
     * Point d'entrée principal pour le code de l'interface JavaFX.
     *
     * @param primaryStage stage principale de l'interface JavaFX, sur laquelle
     *     définir des scenes.
     *
     * @throws IOException si le chargement de la vue FXML échoue.
     *
     * @see javafx.application.Application#start(Stage)
     */
    private void start(Stage primaryStage) throws IOException {
        this.mainStage = primaryStage;
        this.mainStage.setTitle("Amadis");
        changerSceneMainStage("pages/login.fxml");
    }

//-----  Éléments du dialogue  -------------------------------------------------

    private void exitAction() {
        // fermeture de l'interface JavaFX : on notifie sa fin de vie
        this.eolBarrier.countDown();
    }

    private void changerSceneMainStage(String fxmlFile) {
        try {
            // Chargement de la nouvelle interface
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
                fxmlLoader.setController(this);
                Parent scene = fxmlLoader.load();
                Scene mainScene = new Scene(scene);

                // Gestion de la touche Entree
                mainScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (event.getCode() == KeyCode.ENTER) {
                            LOGGER.log(Level.INFO, "Enter key pressed in " + event.getSource() + " at " + mainScene.focusOwnerProperty().get());
                            if (fxmlFile.equals("pages/login.fxml")) {
                                if (mainScene.focusOwnerProperty().get() == tfEmailConnexion || mainScene.focusOwnerProperty().get() == pfMDPConnexion) {
                                    seConnecterAction(null);
                                } else if (mainScene.focusOwnerProperty().get() == tfEmailInscription || mainScene.focusOwnerProperty().get() == pf1MdpInscription
                                        || mainScene.focusOwnerProperty().get() == pf2MdpInscription || mainScene.focusOwnerProperty().get() == tf1MdpInscription
                                        || mainScene.focusOwnerProperty().get() == tf2MdpInscription || mainScene.focusOwnerProperty().get() == tfNomComplet) {
                                    try {
                                        actionbtnSinscrire(null);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else if (fxmlFile.equals("pages/reservation.fxml")) {
                                actionbtnSuivantReservation(null);
                            } else if (fxmlFile.equals("pages/play-form.fxml")) {
                                confirmerFormulaireNouvellePiece();
                            } else if (fxmlFile.equals("pages/reservationRecap.fxml")) {
                                actionbtnConfirmerResRecap(null);
                            }
                        }
                    }
                });

                if (!fxmlFile.equals("pages/login.fxml")) {
                    tf1MdpInscription.textProperty().unbindBidirectional(pf1MdpInscription.textProperty());
                    tf2MdpInscription.textProperty().unbindBidirectional(pf2MdpInscription.textProperty());
                }


            
            this.mainStage.setScene(mainScene);
            this.mainStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void ouvrirNewStage(String fxmlFile) {
        try {
            // Chargement de la nouvelle interface
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            fxmlLoader.setController(this);
            Parent scene = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    // ----- Méthodes de gestion d'event ------------------------------------
    @FXML
    public void deconnexionAction(ActionEvent event) {
        this.controleur.deconnexion();
        tempRep = null;
    }

    // METHODES POUR LA NAVIGATION

    @FXML
    public void actionbtnAmadis (ActionEvent event) {
        afficherMenu();
    }

    @FXML
    public void actionbtnNouvelleReservation (ActionEvent event) {
        if(controleur.verifierOrganisateurConnecteAPiece()){
            changerSceneMainStage("pages/reservation.fxml");
            comboBoxSalles.requestFocus();

            if (tempRep != null) {
                remplirChampsReservation();
            }

            ArrayList<String> listeSallesArrayList = new ArrayList<>();
            for(InfosSalle salle : controleur.recupererInfosAllSAlles()) {
                listeSallesArrayList.add(salle.nom);
            }
            ObservableList<String> listSalles = FXCollections.observableArrayList(listeSallesArrayList);
            comboBoxSalles.setItems(listSalles);

            ArrayList<String> listePieceArrayList = new ArrayList<>();
            for (InfosPiece piece : controleur.recupererInfosAllPieces()){
                listePieceArrayList.add(piece.nom);
            }
            ObservableList<String> listPiece = FXCollections.observableArrayList(listePieceArrayList);
            comboBoxPieces.setItems(listPiece);
        } else {
            informerUtilisateur("Veuillez d'abord ajouter une pièce à votre répertoire !", false);
        }
    }

    @FXML
    public void actionbtnNosSalles (ActionEvent event) {
        changerSceneMainStage("pages/theaters.fxml");
        lbSalles.requestFocus();

        controleur.afficherSalles();
    }

    @FXML
    public void actionbtnAgenda (ActionEvent event) {
        changerSceneMainStage("pages/agenda.fxml");
        lbAgenda.requestFocus();

        controleur.afficherRepresentations();
    }

    @FXML
    public void actionbtnMesPieces (ActionEvent event) {
        this.controleur.afficherPieces();
    }

    @FXML
    public void actionbtnSuivantReservation(ActionEvent event) {

        if (!(comboBoxPieces.getValue() == null || comboBoxSalles.getValue() == null || date.getValue() == null)) {

            if (checkDateSeptJours(date.getValue())) {
                lbErreurDate.setVisible(true);
                lbErreurDate.setText("La réservation doit être dans \nplus d'une semaine");
            } else if (checkOrganisateurIndisponible(date.getValue())) {
                lbErreurDate.setVisible(true);
                lbErreurDate.setText("Cette date vous est déjà réservée");
            } else if (checkSalleIndisponible(date.getValue(), comboBoxSalles.getValue())) {
                lbErreurDate.setVisible(true);
                lbErreurDate.setText("Cette salle est déjà réservée \nà cette date");
            } else {
                lbErreurDate.setVisible(false);
                tempRep = creationTempRep();

                changerSceneMainStage("pages/reservationRecap.fxml");
                tfSalleReservationRecap.requestFocus();

                // On remplit la page de récapitulatif avec les infos de tempRep

                tfSalleReservationRecap.setText(tempRep.salle.nom);
                tfDateReservationRecap.setText(tempRep.date.toString());
                tfPieceReservationRecap.setText(tempRep.piece.nom);

                String oui = "Oui";
                String non = "Non";

                if (tempRep.regieL) {
                    lbRegieLumiereReservationRecap.setText(oui);
                } else {
                    lbRegieLumiereReservationRecap.setText(non);
                }
                if (tempRep.regieS) {
                    lbRegieSonReservationRecap.setText(oui);
                } else {
                    lbRegieSonReservationRecap.setText(non);
                }
                if (tempRep.secu) {
                    lbSecuriteReservationRecap.setText(oui);
                } else {
                    lbSecuriteReservationRecap.setText(non);
                }
                if (tempRep.pub) {
                    lbPubliciteReservationRecap.setText(oui);
                } else {
                    lbPubliciteReservationRecap.setText(non);
                }
            }
        } else {
            lbChampsManquants.setVisible(true);
            btnSuivantReservation.setStyle("-fx-background-color: #fa1c34;");

            LOGGER.log(Level.INFO, "Erreur : champs non remplis");
        }

    }

    @FXML
    public void actionbtnRetourResRecap () {
        changerSceneMainStage("pages/reservation.fxml");
        comboBoxSalles.requestFocus();

        ArrayList<String> listeSallesArrayList = new ArrayList<>();
        for(InfosSalle salle : controleur.recupererInfosAllSAlles()) {
            listeSallesArrayList.add(salle.nom);
        }
        ObservableList<String> listSalles = FXCollections.observableArrayList(listeSallesArrayList);
        comboBoxSalles.setItems(listSalles);

        ArrayList<String> listePieceArrayList = new ArrayList<>();
        for (InfosPiece piece : controleur.recupererInfosAllPieces()){
            listePieceArrayList.add(piece.nom);
        }
        ObservableList<String> listPiece = FXCollections.observableArrayList(listePieceArrayList);
        comboBoxPieces.setItems(listPiece);

        // On remet les champs dans leur était rempli

        remplirChampsReservation();


    }

    @FXML
    public void actionbtnConfirmerResRecap (ActionEvent event) {
        changerSceneMainStage("pages/agenda.fxml");
        lbAgenda.requestFocus();
        this.controleur.enregistrerRepresentation(tempRep);
        this.controleur.afficherRepresentations();
    }

    // METHODES PAGE LOGIN


    /**
     * On superpose le PasswordField et le TextField avec le même contenu, puis on fait apparaître l'un ou autre dépendemment de l'état de la CheckBox
     * @param event
     */
    @FXML
    public void actionCbxAfficherPass (ActionEvent event){

        if (cbxAfficherPass.isSelected()) {

            tf1MdpInscription.setVisible(true);
            tf2MdpInscription.setVisible(true);
            tf1MdpInscription.setFocusTraversable(true); // Pour autoriser/empêcher le focus avec la touche TAB
            tf1MdpInscription.setFocusTraversable(true);

            pf1MdpInscription.setVisible(false);
            pf2MdpInscription.setVisible(false);
            pf1MdpInscription.setFocusTraversable(true);
            pf2MdpInscription.setFocusTraversable(true);

        } else {


            tf1MdpInscription.setVisible(false);
            tf2MdpInscription.setVisible(false);
            tf1MdpInscription.setFocusTraversable(false);
            tf1MdpInscription.setFocusTraversable(false);

            pf1MdpInscription.setVisible(true);
            pf2MdpInscription.setVisible(true);
            pf1MdpInscription.setFocusTraversable(true);
            pf2MdpInscription.setFocusTraversable(true);
        }
    }

    // METHODES POUR S'INSCRIRE

    @FXML
    public void actionbtnSinscrire (ActionEvent event) throws IOException {
        if (verifNom(tfNomComplet) & verifEmail(tfEmailInscription) & verifMDP()) {
            InfosInscription data = null;
            try {
                data = new InfosInscription(tfEmailInscription.getText(), tfNomComplet.getText(), Utilitaire.hash(pf2MdpInscription.getText()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            controleur.enregistrerOrganisateur(data);

            afficherMenu();
        }
    }

    @FXML public void seConnecterAction (ActionEvent event) {
        // vérification de la validité des champs coté IHM

        if (!(verifEmail(tfEmailConnexion)) | !(verifMDPConnexion(pfMDPConnexion)) | !controleur.verifierConnexion(new InfosConnexion(tfEmailConnexion.getText(), pfMDPConnexion.getText()))) {
            lbErreurConnexion.setVisible(true);
            LOGGER.log(Level.SEVERE, "Erreur de connexion : email ou mot de passe incorrect");
        } else {
            afficherMenu();
        }
    }

    /**
     * Evénement qui génère l'ouverture de la page des salles.
     *
     * @param event L'événement fait sur la vue.
     */
    @FXML
    private void afficherSalle (ActionEvent event)  {
        this.controleur.afficherSalles();
    }

    //--- Pages des pièces ---//

    @FXML
    private void creerNouvellePieceAction () {
        this.controleur.saisirNouvellePiece();
    }

    // page formulaire de création d'une pièce

    @FXML
    private void confirmerFormulaireNouvellePiece() {
        boolean estValide = true;

        estValide &= valideNonVideTextField(tfNomPiece);
        estValide &= verifierIntTextField(tfDureePiece);
        estValide &= valideNonVideTextArea(taResumeePiece);

        if (estValide) {
            InfosPiece data = new InfosPiece(
                    tfNomPiece.getText(),
                    Integer.parseInt(tfDureePiece.getText()),
                    taResumeePiece.getText());

            this.controleur.enregistrerNouvellePiece(data);
            Stage stageFormulaireNouvellePiece = (Stage) tfNomPiece.getScene().getWindow();
            stageFormulaireNouvellePiece.close();
            this.controleur.afficherPieces();
        }
    }

    @FXML
    private void confirmerFormulaireModifPiece(InfosPiece piece) {
        boolean estValide = true;

        estValide &= valideNonVideTextField(tfNomPiece);
        estValide &= verifierIntTextField(tfDureePiece);
        estValide &= valideNonVideTextArea(taResumeePiece);

        if (estValide) {
            InfosPiece data = new InfosPiece(
                    tfNomPiece.getText(),
                    Integer.parseInt(tfDureePiece.getText()),
                    taResumeePiece.getText());

            this.controleur.modifierPiece(piece.nom, data);
            Stage stageFormulaireNouvellePiece = (Stage) tfNomPiece.getScene().getWindow();
            stageFormulaireNouvellePiece.close();
            this.controleur.afficherPieces();
        }
    }

    @FXML
    public void retourFormulaireNouvellePiece (ActionEvent event){
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

    //-----  Implémentation des méthodes abstraites  -------------------------------

    @Override
    public void demarrerInteraction() {
        // démarrage de l'interface JavaFX
        Platform.startup(() -> {
            Stage primaryStage = new Stage();
            primaryStage.setOnCloseRequest((WindowEvent t) -> this.exitAction());
            try {
                this.start(primaryStage);
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        });

        // attente de la fin de vie de l'interface JavaFX
        try {
            this.eolBarrier.await();
        } catch (InterruptedException exc) {
            System.err.println("Erreur d'exécution de l'interface.");
            System.err.flush();
        }
    }

    @Override
    public void informerUtilisateur(String msg, boolean succes) {
        final Alert alert = new Alert(
                succes ? Alert.AlertType.INFORMATION : Alert.AlertType.WARNING
        );
        alert.setTitle("GenEvent");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @Override
    public void afficherMenu() {
        changerSceneMainStage("pages/menu.fxml");

    }

    @Override
    public void afficherSalles() {
        changerSceneMainStage("pages/theaters.fxml");
        lbSalles.requestFocus();
        for (InfosSalle salle : this.controleur.recupererInfosAllSAlles()) {
            genererGridPaneSalle(salle);
        }
    }

    @Override
    public void afficherRepresentations() {
        changerSceneMainStage("pages/agenda.fxml");
        lbAgenda.requestFocus();
        for (InfosRepresentation representation : this.controleur.recupererInfosAllRepresentation()) {
            genererGridPaneRepresentation(representation);
        }
    }

    @Override
    public void afficherPieces() {
        changerSceneMainStage("pages/pieces.fxml");
        btnAjoutePiece.requestFocus();
        for(InfosPiece infosPiece : this.controleur.recupererInfosAllPieces()) {
            genererGridPanePiece(infosPiece);
        }
    }

    @Override
    public void saisirNouvellePiece() {
        ouvrirNewStage("pages/play-form.fxml");
    }
    @Override
    public void saisirModifPiece(InfosPiece piece) {
        ouvrirNewStage("pages/modif-play-form.fxml");
        Button btnConfirmerModif = new Button();
        btnConfirmerModif.setText("Confirmer");
        btnConfirmerModif.setStyle("-fx-background-color: #302A74; -fx-text-fill: WHITE;");
        btnConfirmerModif.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                confirmerFormulaireModifPiece(piece);
            }
        });
        hbModifPiece.getChildren().add(btnConfirmerModif);
    }

    @Override
    public void saisirConnexion() {
        changerSceneMainStage("pages/login.fxml");
    }

    @Override
    public void saisirInscription() {
        changerSceneMainStage("pages/login.fxml");
    }

    @Override
    public void supprimerPiece(InfosPiece piece) {
        this.controleur.supprimerPiece(piece);
        this.controleur.afficherPieces();
    }

    //-----  Méthodes utilitaire à l'IHM  ------------------------------------------
    private static void markTextFieldErrorStatus(TextField textField, boolean isValid) {
        if (isValid) {
            textField.setStyle(null);
        } else {
            textField.setStyle("-fx-background-color: 'E77272'");
        }
    }

    private static void markTextAreaErrorStatus(TextArea textArea, boolean isValid) {
        if (isValid) {
            textArea.setStyle(null);
        } else {
            textArea.lookup(".content").setStyle("-fx-background-color: 'E77272'");
        }
    }
    // TODO: 22/06/2022 - factoriser les méthodes de mark...ErrorStatus (les deux au dessus)

    private static boolean valideNonVideTextField(TextField textField) {
        boolean isValid = !textField.getText().isEmpty();

        markTextFieldErrorStatus(textField, isValid);

        return isValid;
    }

    private static boolean valideNonVideTextArea(TextArea textArea) {
        boolean isValid = !textArea.getText().isEmpty();

        markTextAreaErrorStatus(textArea, isValid);

        return isValid;
    }
    // TODO: 22/06/2022 - factoriser les méthodes de validateNonVide... (les deux au dessus)

    private static boolean verifierNonEmptyTextField(TextField textField) {
        boolean isValid = textField.getText().strip().length() > 0;

        markTextFieldErrorStatus(textField, isValid);

        return isValid;
    }

    private static boolean verifierEmailTextField(TextField textField) {
        EmailValidator validator = EmailValidator.getInstance(false, false);
        boolean isValid = validator.isValid(textField.getText().strip().toLowerCase());

        markTextFieldErrorStatus(textField, isValid);

        return isValid;
    }

    private static boolean verifierIntTextField(TextField textField){
        try {
            int duree = Integer.parseInt(textField.getText());
            boolean isValid = duree > 0;
            isValid &= valideNonVideTextField(textField);

            markTextFieldErrorStatus(textField, isValid);

            return isValid;
        } catch (NumberFormatException e) {
            markTextFieldErrorStatus(textField, false);
            return false;
        }
    }


    // ----- Autres méthodes & procédures --------------------------------------

    public boolean verifNom (TextField textField){
        if (textField.getText() == null || textField.getText().isEmpty()) {
            textField.setStyle("-fx-background-color: 'E77272'");
        } else {
            textField.setStyle("-fx-background-color: 'DDD7E2'");
        }
        return !(textField.getText() == null || textField.getText().isEmpty());
    }

    public boolean verifEmail (TextField tf){
        if (tf.getText() == null | tf.getText().isEmpty() || !verifierEmailTextField(tf)) {
            tf.setStyle("-fx-background-color: 'E77272'");
        } else {
            tf.setStyle("-fx-background-color: 'DDD7E2'");
        }
        return !(tf.getText() == null | tf.getText().isEmpty() || !verifierEmailTextField(tf));
    }

    public boolean verifMDP () {
        if (pf1MdpInscription.getText() == null | pf2MdpInscription.getText() == null | pf1MdpInscription.getText().isEmpty() | pf2MdpInscription.getText().isEmpty()) {
            pf1MdpInscription.setStyle("-fx-background-color: 'E77272'");
            tf1MdpInscription.setStyle("-fx-background-color: 'E77272'");
            pf2MdpInscription.setStyle("-fx-background-color: 'E77272'");
            tf2MdpInscription.setStyle("-fx-background-color: 'E77272'");
            lbErreurMdp.setVisible(true);
        } else if (!pf1MdpInscription.getText().equals(pf2MdpInscription.getText())) {
            lbErreurMdp.setVisible(true);
        } else {
            pf1MdpInscription.setStyle("-fx-background-color: 'DDD7E2'");
            tf1MdpInscription.setStyle("-fx-background-color: 'DDD7E2'");
            pf2MdpInscription.setStyle("-fx-background-color: 'DDD7E2'");
            tf2MdpInscription.setStyle("-fx-background-color: 'DDD7E2'");
            lbErreurMdp.setVisible(false);
        }
        return (!(pf1MdpInscription.getText() == null | pf2MdpInscription.getText() == null | pf1MdpInscription.getText().isEmpty() | pf2MdpInscription.getText().isEmpty()) & (pf1MdpInscription.getText().equals(pf2MdpInscription.getText())));
    }

    /**
     * A utiliser dans un event de clic sur un bouton de suppression d'élément.
     * Potentiellement à déplacer dans une classe Utilitaire.
     * @param event event de l'event listener du bouton clic
     * @return true si l'utilisateur confirme la suppression, false sinon (cancel ou croix)
     */
    public boolean verifierSuppression (ActionEvent event){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer cet élément ?");
        alert.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            LOGGER.log(Level.INFO, "L'utilisateur à confirmé la suppression");
            return true;
        } else {
            LOGGER.log(Level.INFO, "L'utilisateur a annulé la suppression.");
            return false;
        }

    }
    public boolean verifierAnnulation (ActionEvent event){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Suppression");
        alert.setHeaderText("Voulez-vous vraiment annuler cette réservation ?");
        alert.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            LOGGER.log(Level.INFO, "L'utilisateur à confirmé la suppression");
            return true;
        } else {
            LOGGER.log(Level.INFO, "L'utilisateur a annulé la suppression.");
            return false;
        }
    }


    public boolean verifMDPConnexion (PasswordField passwordField){
        if (passwordField.getText() == null | passwordField.getText().isEmpty()) {
            pfMDPConnexion.setStyle("-fx-background-color: 'E77272'");
        } else {
            pfMDPConnexion.setStyle("-fx-background-color: 'DDD7E2'");
        }
        return !(passwordField.getText() == null | passwordField.getText().isEmpty());
    }

    public boolean checkSalleIndisponible(LocalDate date, String nomSalle) {
        return controleur.checkSalleIndisponible(date, nomSalle);
    }

    public boolean checkOrganisateurIndisponible(LocalDate date) {

        return controleur.checkOrganisateurIndisponible(date);
    }

    public boolean checkDateSeptJours(LocalDate date) {
        return controleur.checkDateSeptJours(date);
    }

    /**
     * Méthode qui affiche la liste des pièces dans la page pieces.fxml.
     *
     * @param piece la pièce à afficher.
     */
    public void genererGridPanePiece (InfosPiece piece){

        GridPane gridPanePiece = null;
        try {
            gridPanePiece = FXMLLoader.load(getClass().getResource("pages/elmtsPiece.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        vbListePieces.getChildren().add(gridPanePiece);

        // mise en place des informations de infoPiece dans le gridPane
        // modification du titre de la pièce
        AnchorPane anchorPane = (AnchorPane) gridPanePiece.getChildren().get(0);
        Label labelNom = (Label) anchorPane.getChildren().get(0);
        labelNom.setText(piece.nom);

        // modification de la description de la pièce
        TextArea taDescription = (TextArea) gridPanePiece.getChildren().get(1);
        taDescription.setText(piece.description);

        // ajout d'event pour les boutons de suppression et de modification
        AnchorPane anchorPane2 = (AnchorPane) gridPanePiece.getChildren().get(2);
        Button btnSupprimer = (Button) anchorPane2.getChildren().get(0);
        btnSupprimer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(!controleur.verifierPieceUtilisee(piece)){
                    if (verifierSuppression(actionEvent)){
                        supprimerPiece(piece);
                        controleur.afficherPieces();
                    }
                } else {
                    informerUtilisateur("Cette pièce est dans une représentation à venir, impossible de la supprimer", false);
                }
            }
        });

        Button btnModifier = (Button) anchorPane2.getChildren().get(1);
        btnModifier.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                controleur.saisirModifPiece(piece);
                tfNomPiece.setText(piece.nom);
                tfDureePiece.setText(String.valueOf(piece.duree));
                taResumeePiece.setText(piece.description);
            }
        });
    }

    /**
     * Méthode pour afficher la liste des salles dans la page pieces.fxml.
     *
     * @param salle La salle à afficher.
     */
    public void genererGridPaneSalle (InfosSalle salle){

        GridPane gp1 = null;
        try {
            gp1 = FXMLLoader.load(getClass().getResource("pages/elmtsSalle.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        vbListeSalles.getChildren().add(gp1);

        Label lb1 = (Label) gp1.getChildren().get(0);
        lb1.setText(salle.nom);

        Label lb2 = (Label) gp1.getChildren().get(1);
        lb2.setText(salle.adresse + " - " + salle.ville);

        Label lb3 = (Label) gp1.getChildren().get(2);
        lb3.setText(salle.capacite + " places");

        Label lb4 = (Label) gp1.getChildren().get(3);
        lb4.setText((int)salle.prix + "€/jour");

        Button btnReserverSalle = (Button) gp1.getChildren().get(4);
        btnReserverSalle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(controleur.verifierOrganisateurConnecteAPiece()) {
                    changerSceneMainStage("pages/reservation.fxml");
                    comboBoxSalles.requestFocus();

                    ArrayList<String> listeSalleArrayList = new ArrayList<>();
                    for (InfosSalle salle : controleur.recupererInfosAllSAlles()) {
                        listeSalleArrayList.add(salle.nom);
                    }
                    ObservableList<String> listSalle = FXCollections.observableArrayList(listeSalleArrayList);
                    comboBoxSalles.setItems(listSalle);
                    comboBoxSalles.setValue(salle.nom);

                    ArrayList<String> listePieceArrayList = new ArrayList<>();
                    for (InfosPiece piece : controleur.recupererInfosAllPieces()) {
                        listePieceArrayList.add(piece.nom);
                    }
                    ObservableList<String> listPiece = FXCollections.observableArrayList(listePieceArrayList);
                    comboBoxPieces.setItems(listPiece);

                    LOGGER.log(Level.INFO, "" + comboBoxSalles.getItems());
                } else {
                    informerUtilisateur("Veuillez d'abord ajouter une pièce à votre répertoire !", false);
                }
            }
        });
    }

    /**
     * Méthode qui génère la liste des représentations de la page agenda.fxml.
     *
     * @param representation La représentation à afficher.
     */
    public void genererGridPaneRepresentation(InfosRepresentation representation) {

        GridPane gp = null;
        try {
            gp = FXMLLoader.load(getClass().getResource("pages/elmtsAgenda.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        vbListeRepresentations.getChildren().add(gp);

        Label lb1 = (Label) gp.getChildren().get(0);
        lb1.setText(representation.date.toString());

        Label lb2 = (Label) gp.getChildren().get(1);
        lb2.setText(representation.piece.nom);

        Label lb3 = (Label) gp.getChildren().get(2);
        lb3.setText(representation.salle.nom);

        Button btnSupprimerRepres = (Button) gp.getChildren().get(3);
        btnSupprimerRepres.setText("Annuler");
        btnSupprimerRepres.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!checkDateSeptJours(representation.date)){
                    if (verifierAnnulation(e)){
                        controleur.annulerRepresentation(representation);{
                            controleur.afficherRepresentations();
                        }
                    }
                }
            }
        });
    }

    /**
     *
     * @return temp, un InfosRepresentation qui contient les informations de la représentation à créer.
     */
    public InfosRepresentation creationTempRep() {

        // Création des attributs de la représentation
        LocalDate date = this.date.getValue();
        boolean regieL = this. radioBtnGaucheRegieLumiere.isSelected() ? true : false;
        boolean regieS = this.radioBtnGaucheRegieSon.isSelected() ? true : false;
        boolean secu = this.radioBtnGaucheSecurite.isSelected() ? true : false;
        boolean pub = this.radioBtnGauchePublicite.isSelected() ? true : false;
        InfosPiece piece = this.controleur.recupererInfosPiece(this.comboBoxPieces.getValue());
        InfosSalle salle = this.controleur.recupererInfosSalle(this.comboBoxSalles.getValue());

        InfosRepresentation temp = new InfosRepresentation(date, regieL, regieS, secu, pub, piece, salle);
        LOGGER.log(Level.INFO, "Création / MàJ de la représentation temporaire : " + temp);

        return temp;
    }

    /**
     * Méthode permettant de réafficher la demande de réservation en cours lorsque l'utilsateur change de page puis revient.
     */
    public void remplirChampsReservation() {

        this.comboBoxSalles.setValue(tempRep.salle.nom);
        this.date.setValue(tempRep.date);
        this.comboBoxPieces.setValue(tempRep.piece.nom);

        if (tempRep.regieL) {
            radioBtnGaucheRegieLumiere.setSelected(true);
        } else {
            radioBtnDroiteRegieLumiere.setSelected(true);
        }

        if (tempRep.regieS) {
            radioBtnGaucheRegieSon.setSelected(true);
        } else {
            radioBtnDroiteRegieSon.setSelected(true);
        }

        if (tempRep.secu) {
            radioBtnGaucheSecurite.setSelected(true);
        } else {
            radioBtnDroiteSecurite.setSelected(true);
        }

        if (tempRep.pub) {
            radioBtnGauchePublicite.setSelected(true);
        } else {
            radioBtnDroitePublicite.setSelected(true);
        }
    }


}
